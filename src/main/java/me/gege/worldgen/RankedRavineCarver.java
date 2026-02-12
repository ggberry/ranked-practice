package me.gege.worldgen;

import com.mojang.serialization.Codec;
import me.gege.util.SeedUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.UnderwaterRavineCarver;

import java.util.*;
import java.util.function.Function;

import static me.gege.util.SeedUtil.*;

public class RankedRavineCarver extends UnderwaterRavineCarver {
    public static final ProbabilityConfig config = new ProbabilityConfig(0.05F);
    private static final int RADIUS = 10;

    public RankedRavineCarver(Codec<ProbabilityConfig> codec) {
        super(codec);
    }

    @Override
    public boolean shouldCarve(Random random, int chunkX, int chunkZ, ProbabilityConfig probabilityConfig) {
        if (!isPracticing) {
            return false;
        }

        if (sourcePos == null || (seedType != 1 && seedType != 2)) {
            return false;
        }

        int dx = sourcePos.x - chunkX;
        int dz = sourcePos.z - chunkZ;
        int squareDist = dx * dx + dz * dz;

        boolean randomCap = random.nextFloat() < probabilityConfig.probability;
        boolean goodDistance = squareDist <= RADIUS * RADIUS;

        return randomCap && goodDistance && allowRavine(chunkX, chunkZ);
    }

    public boolean allowRavine(int chunkX, int chunkZ) {
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);

        if (magmaRavines.contains(chunkPos)) {
            return true;
        }

        if (magmaRavines.size() < 2) {
            System.out.println(chunkPos);
            magmaRavines.add(chunkPos);
            return true;
        }

        return false;
    }

//    public boolean allowRavine(int chunkX, int chunkZ, int centerX, int centerZ) {
//        long salt = 987654321L;
//        Random rand = new Random(overworldSeed + salt);
//
//        for (int i = 0; i < 2; i++) {
//            int x = centerX + rand.nextInt(RADIUS * 2 + 1) - RADIUS;
//            int z = centerZ + rand.nextInt(RADIUS * 2 + 1) - RADIUS;
//
//            if (x == chunkX && z == chunkZ) {
//                return true;
//            }
//        }
//
//        return false;
//    }

    @Override
    protected boolean carveRegion(Chunk chunk, Function<BlockPos, Biome> posToBiome, long seed, int seaLevel, int chunkX, int chunkZ, double startX, double startY, double startZ, double yaw, double pitch, BitSet carvingMask) {
        Random random = new Random();
        pitch = 40 + random.nextFloat() * (80 - 40);

        return super.carveRegion(chunk, posToBiome, seed, seaLevel, chunkX, chunkZ, startX, startY, startZ, yaw, pitch, carvingMask);
    }

    @Override
    public int getBranchFactor() {
        return 3;
    }

    @Override
    protected boolean canCarveBlock(BlockState state, BlockState stateAbove) {
        return true;
    }

    @Override
    public boolean canAlwaysCarveBlock(BlockState state) {
        return true;
    }

    @Override
    protected boolean canCarveBranch(int mainChunkX, int mainChunkZ, double x, double z, int branch, int branchCount, float baseWidth) {
        return true;
    }

    @Override
    protected boolean isPositionExcluded(double scaledRelativeX, double scaledRelativeY, double scaledRelativeZ, int y) {
        return false;
    }
}
