package me.gege.mixin.screen;

import net.fabricmc.fabric.api.event.EventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EventFactory.class)
public abstract class EventFactoryMixin {
    @Inject(at = @At("RETURN"), method = "isProfilingEnabled", cancellable = true)
    private static void isProfilingEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
