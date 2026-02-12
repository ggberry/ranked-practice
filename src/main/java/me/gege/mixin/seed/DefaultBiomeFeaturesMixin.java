package me.gege.mixin.seed;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.worldgen.ModCarvers.CONFIGURED_RANKED_RAVINE;

@Mixin(DefaultBiomeFeatures.class)
public abstract class DefaultBiomeFeaturesMixin {
    @Inject(at = @At("HEAD"), method = "addOceanCarvers")
    private static void addOceanCarvers(Biome biome, CallbackInfo ci) {
        biome.addCarver(GenerationStep.Carver.LIQUID, CONFIGURED_RANKED_RAVINE);
    }
}
