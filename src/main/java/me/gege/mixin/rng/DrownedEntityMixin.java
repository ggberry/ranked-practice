package me.gege.mixin.rng;

import me.gege.util.RNGUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin extends Entity {
    public DrownedEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "initEquipment", cancellable = true)
    private void initEquipment(LocalDifficulty difficulty, CallbackInfo ci) {
        RNGUtil.noTridentDrowned((DrownedEntity)(Object) this, this.random, ci);
    }
}
