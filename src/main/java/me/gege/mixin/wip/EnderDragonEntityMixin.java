package me.gege.mixin.wip;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.phase.HoldingPatternPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends Entity {
    @Shadow @Final private PhaseManager phaseManager;

    @Shadow @Final private @Nullable EnderDragonFight fight;

    @Shadow @Final private PathNode[] pathNodes;

    public EnderDragonEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "getNearestPathNodeIndex(DDD)I", cancellable = true)
    private void getNearestPathNodeIndex(double x, double y, double z, CallbackInfoReturnable<Integer> cir) {
        if (!(this.phaseManager.getCurrent() instanceof HoldingPatternPhase)) {
            return;
        }

        //RNGUtil.lowerFlyChance(x, y, z, this.fight, this.pathNodes, cir);
    }
}
