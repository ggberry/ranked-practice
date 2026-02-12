package me.gege.mixin.rng;

import me.gege.util.RNGUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(GhastEntity.class)
public abstract class GhastEntityMixin {
    @Inject(at = @At("HEAD"), method = "canSpawn", cancellable = true)
    private static void canSpawn(EntityType<GhastEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        RNGUtil.ghastSpawnControl(type, world, spawnReason, pos, random, cir);
    }
}
