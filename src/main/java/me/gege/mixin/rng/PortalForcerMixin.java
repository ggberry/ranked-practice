package me.gege.mixin.rng;

import me.gege.util.RNGUtil;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Forces "blind" portal to be on surface
 */

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {
    @Shadow @Final private ServerWorld world;

    @Inject(at = @At("HEAD"), method = "createPortal", cancellable = true)
    private void createPortal(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        RNGUtil.betterPortalCords(this.world, entity, this.world.random, cir);
    }
}