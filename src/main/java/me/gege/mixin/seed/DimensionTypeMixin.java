package me.gege.mixin.seed;

import me.gege.RankedPractice;
import me.gege.util.SeedUtil;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public abstract class DimensionTypeMixin {
    @Inject(at = @At("HEAD"), method = "createNetherGenerator", cancellable = true)
    private static void createNetherGenerator(long seed, CallbackInfoReturnable<ChunkGenerator> cir) {
        if (RankedPractice.DEVELOPING) {
            return;
        }

        SeedUtil.setNetherTerrain(cir);
    }
}
