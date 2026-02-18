package me.gege.mixin.rng;

import me.gege.util.RNGUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.util.SeedUtil.isPracticing;

/**
 * Guarantees 4 iron ingots from killing Iron Golems
 */

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "dropLoot", cancellable = true)
    private void dropLoot(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        if (!isPracticing) {
            return;
        }

        RNGUtil.golemLoot((LivingEntity)(Object) this, ci);
    }
}
