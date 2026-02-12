package me.gege.mixin.rng;

import me.gege.util.RNGUtil;
import net.minecraft.entity.mob.ElderGuardianEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElderGuardianEntity.class)
public abstract class ElderGuardianEntityMixin {
    @Inject(at = @At("HEAD"), method = "mobTick", cancellable = true)
    private void mobTick(CallbackInfo ci) {
        RNGUtil.noMiningFatigue(ci);
    }
}
