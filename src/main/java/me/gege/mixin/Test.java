package me.gege.mixin;

import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public abstract class Test {
    @Inject(at = @At("HEAD"), method = "carve", cancellable = true)
    private void shouldCarve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver carver, CallbackInfo ci) {

    }
}
