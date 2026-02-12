package me.gege.seed.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;

import java.util.List;
import java.util.concurrent.Executor;

public class SeededServerWorld extends ServerWorld {
    private final long filterSeed;

    public SeededServerWorld(long seed, MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> registryKey, RegistryKey<DimensionType> registryKey2, DimensionType dimensionType, WorldGenerationProgressListener generationProgressListener, ChunkGenerator chunkGenerator, boolean bl, List<Spawner> list, boolean bl2) {
        super(server, workerExecutor, session, properties, registryKey, registryKey2, dimensionType, generationProgressListener, chunkGenerator, bl, seed, list, bl2);

        this.filterSeed = seed;
    }

    @Override
    public long getSeed() {
        return filterSeed;
    }
}
