package me.gege.mixin.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.LukewarmOceanBiome;
import net.minecraft.world.gen.GenerationStep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.worldgen.ModCarvers.CONFIGURED_RANKED_RAVINE;

@Mixin(LukewarmOceanBiome.class)
public abstract class LukewarmOceanBiomeMixin extends Biome {
    protected LukewarmOceanBiomeMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo ci) {
        this.addCarver(GenerationStep.Carver.LIQUID,  CONFIGURED_RANKED_RAVINE);
    }
}
