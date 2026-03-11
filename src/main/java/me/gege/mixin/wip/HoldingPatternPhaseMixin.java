package me.gege.mixin.wip;

import me.gege.util.DragonUtil;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.HoldingPatternPhase;
import net.minecraft.entity.boss.dragon.phase.Phase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HoldingPatternPhase.class)
public abstract class HoldingPatternPhaseMixin extends AbstractPhase {
    @Shadow private Path field_7043;

    @Shadow private Vec3d target;

    public HoldingPatternPhaseMixin(EnderDragonEntity dragon) {
        super(dragon);
    }

    @Inject(at = @At("HEAD"), method = "method_6842", cancellable = true)
    private void navigateToNextPathNode(CallbackInfo ci) {
        if (this.field_7043 != null && !this.field_7043.isFinished()) {
            this.target = DragonUtil.lowerZeroNode(this.field_7043, this.dragon);

            ci.cancel();
        }
    }
}
