package me.gege.util;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeedUtil extends ChunkRandom {
    private static final ConfiguredFeature<?, ?> LAVA_POOL_FEATURE =
            Feature.LAKE.configure(
                    new SingleStateFeatureConfig(Blocks.LAVA.getDefaultState())
    );

    public static List<ChunkPos> magmaRavines = new ArrayList<>();

    public static boolean isPracticing = false;
    public static int seedType;
    public static long overworldSeed = 0;
    public static long netherSeed;
    public static ChunkPos sourcePos;

    public static int netherCooldown;
    public static boolean inNether;

    public static void generateLavaPools(ServerWorld world) {
        if (sourcePos == null) {
            return;
        }

        int enters = 0;
        int totalTries = 0;

        while (totalTries++ < 1000 && enters < 3) {
            ChunkPos chunk = randomChunkInRing(world.random, sourcePos, 2, 6);

            boolean generated = false;

            for (int i = 0; i < 4 && !generated; i++) {
                int x = chunk.getStartX() + world.random.nextInt(16);
                int z = chunk.getStartZ() + world.random.nextInt(16);

                int y = world.getTopY(
                        Heightmap.Type.WORLD_SURFACE,
                        x, z
                ) - 1;

                BlockPos pos = new BlockPos(x, y, z);

                generated = LAVA_POOL_FEATURE.generate(
                        world,
                        world.getStructureAccessor(),
                        world.getChunkManager().getChunkGenerator(),
                        world.random,
                        pos
                );
            }

            if (generated) {
                enters++;
            }
        }
    }

    public static boolean isOceanBiome(BiomeSource biomeSource, int blockX, int blockZ) {
        Biome biome = biomeSource.getBiomeForNoiseGen(blockX >> 2, 0, blockZ >> 2);
        return biome.getCategory() == Biome.Category.OCEAN;
    }

    public static boolean isOceanSeed() {
        return seedType == 1 || seedType == 2;
    }

    public static ChunkPos randomChunkInRing(Random random, ChunkPos center, int minRadius, int maxRadius) {
        double angle = random.nextDouble() * Math.PI * 2.0;

        double r = Math.sqrt(
                random.nextDouble() * (maxRadius * maxRadius - minRadius * minRadius)
                        + minRadius * minRadius
        );

        int dx = (int) Math.round(Math.cos(angle) * r);
        int dz = (int) Math.round(Math.sin(angle) * r);

        return new ChunkPos(center.x + dx, center.z + dz);
    }

    public static void setNetherTerrain(CallbackInfoReturnable<ChunkGenerator> cir) {
        if (!isPracticing) {
            return;
        }

        cir.setReturnValue(new SurfaceChunkGenerator(MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(netherSeed), netherSeed, ChunkGeneratorType.Preset.NETHER.getChunkGeneratorType()));
    }

    public static String nameFromType(int seedType) {
        switch (seedType) {
            case 0:
                return "Village";
            case 1:
                return "Shipwreck";
            case 2:
                return "Buried Treasure";
            case 3:
                return "Desert Temple";
        }

        return "Ruined Portal";
    }
}
