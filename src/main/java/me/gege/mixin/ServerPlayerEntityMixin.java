package me.gege.mixin;

import me.gege.util.GeneralUtil;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Keeps player NBT data on death (non default)
 * Changes player game mode to spectator on completion
 */

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow public abstract ServerWorld getServerWorld();

    @Shadow public abstract void setGameMode(GameMode gameMode);

    // NBT restoration
    @Inject(at = @At("HEAD"), method = "copyFrom")
    private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        GeneralUtil.restorePlayerData(oldPlayer, (ServerPlayerEntity)(Object) this);
    }

    @Inject(at = @At("HEAD"), method = "changeDimension")
    private void changeDimension(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        if (this.getServerWorld().getRegistryKey() == ServerWorld.END && destination.getRegistryKey() == ServerWorld.OVERWORLD) {
            this.setGameMode(GameMode.SPECTATOR);
        }
    }
}
