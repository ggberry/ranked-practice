package me.gege.mixin.seed;

import me.gege.util.SeedUtil;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.gege.util.SeedUtil.*;

@Mixin(StructureFeature.class)
public abstract class StructureFeatureMixin {
    @Shadow public abstract ConfiguredStructureFeature<FeatureConfig, ? extends StructureFeature<FeatureConfig>> configure(FeatureConfig config);

    @Inject(at = @At("HEAD"), method = "shouldStartAt", cancellable = true)
    private void generateStructure(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, FeatureConfig featureConfig, CallbackInfoReturnable<Boolean> cir) {
        if (!isPracticing) {
            return;
        }

        if (!SeedUtil.isOceanSeed()) {
            return;
        }

        StructureFeature<?> feature = this.configure(featureConfig).field_24835;

        if (seedType == 2 && (feature == StructureFeature.SHIPWRECK || feature == StructureFeature.RUINED_PORTAL)) {
            int dx = sourcePos.x - chunkPos.x;
            int dz = sourcePos.z - chunkPos.z;
            int squareDist = dx * dx + dz * dz;

            if (squareDist < 20 * 20) {
                cir.setReturnValue(false);
            }
        }
    }
}
