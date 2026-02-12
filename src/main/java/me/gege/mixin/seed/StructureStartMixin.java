package me.gege.mixin.seed;

import me.gege.util.SeedUtil;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static me.gege.util.SeedUtil.isPracticing;
import static me.gege.util.SeedUtil.sourcePos;

@Mixin(StructureStart.class)
public abstract class StructureStartMixin {
    @Shadow @Final private StructureFeature<?> feature;

    @Inject(at = @At("HEAD"), method = "generateStructure", cancellable = true)
    private void generateStructure(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox blockBox, ChunkPos chunkPos, CallbackInfo ci) {
        if (!isPracticing) {
            return;
        }

        if (!SeedUtil.isOceanSeed() || this.feature != StructureFeature.MINESHAFT) {
            return;
        }

        int dx = sourcePos.x - chunkPos.x;
        int dz = sourcePos.z - chunkPos.z;
        int squareDist = dx * dx + dz * dz;

        if (squareDist < 20 * 20) {
            ci.cancel();
        }
    }
}
