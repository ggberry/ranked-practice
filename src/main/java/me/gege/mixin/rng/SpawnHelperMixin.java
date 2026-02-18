package me.gege.mixin.rng;

import me.gege.util.RNGUtil;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Prevents hostile entities from spawning in Desert Temples & Zombified Piglins from spawning in Bastions
 */

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {
    @Inject(at = @At("HEAD"), method = "isValidSpawn", cancellable = true)
    private static void isValidSpawn(ServerWorld world, MobEntity entity, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
        RNGUtil.noStructureSpawns(world, entity, cir);
    }
}
