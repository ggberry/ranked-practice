package me.gege.mixin.rng;

import me.gege.util.RNGUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.util.SeedUtil.isPracticing;

@Mixin(EyeOfEnderEntity.class)
public abstract class EyeOfEnderEntityMixin extends Entity {
    @Shadow private boolean dropsItem;

    public EyeOfEnderEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("TAIL"), method = "moveTowards")
    private void moveTowards(BlockPos pos, CallbackInfo ci) {
        if (!isPracticing) {
            return;
        }

        if (!RNGUtil.shouldEyeBreak(this.world)) {
            this.dropsItem = true;
        }
    }
}
