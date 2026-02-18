package me.gege.mixin.seed;

import me.gege.util.SeedUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DungeonFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static me.gege.util.SeedUtil.isPracticing;
import static me.gege.util.SeedUtil.sourcePos;

/**
 * Prevents Dungeon obstruction for mapless
 */

@Mixin(DungeonFeature.class)
public abstract class DungeonFeatureMixin {
    @Inject(at = @At("HEAD"), method = "generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/DefaultFeatureConfig;)Z", cancellable = true)
    private void generate(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig, CallbackInfoReturnable<Boolean> cir) {
        if (!isPracticing) {
            return;
        }

        if (!SeedUtil.isOceanSeed()) {
            return;
        }

        ChunkPos chunkPos = new ChunkPos(blockPos.getX() >> 4, blockPos.getZ() >> 4);

        int dx = sourcePos.x - chunkPos.x;
        int dz= sourcePos.z - chunkPos.z;
        int squareDist = dx * dx + dz * dz;

        if (squareDist < 15) {
            cir.cancel();
        }
    }
}
