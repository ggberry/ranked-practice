package me.gege.seed.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.gege.RankedPractice;
import me.gege.hud.FilterHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class SeedThreadPredicate {
    @FunctionalInterface
    public interface SeedPredicate {
        ChunkPos getSourceChunk(long seed) throws IOException;
    }

    public static void filterSeeds(long start, long end, SeedPredicate predicate) {
        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        AtomicLong checked = new AtomicLong();

        long total = end - start + 1;
        long chunkSize = (total + threads - 1) / threads;

        RankedPractice.LOGGER.info("Starting seed search ({} → {}) using {} threads.", start, end, threads);

        List<Future<List<JsonObject>>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            long chunkStart = start + i * chunkSize;
            long chunkEnd = Math.min(chunkStart + chunkSize - 1, end);

            if (chunkStart > end) break;

            Future<List<JsonObject>> future = executor.submit(() -> {
                List<JsonObject> localResults = new ArrayList<>();

                for (long seed = chunkStart; seed <= chunkEnd && SeedFilter.filter; seed++) {
                    try {
                        ChunkPos sourceChunk = predicate.getSourceChunk(seed);

                        if (sourceChunk != null) {
                            JsonObject seedInfo = new JsonObject();
                            seedInfo.addProperty("seed", seed);
                            seedInfo.addProperty("chunkX", sourceChunk.x);
                            seedInfo.addProperty("chunkZ", sourceChunk.z);

                            localResults.add(seedInfo);
                            RankedPractice.LOGGER.info("Seed {} matched", seed + " (" + sourceChunk.x + ", " + sourceChunk.z + ")");
                        }
                    } catch (Throwable t) {
                        RankedPractice.LOGGER.error("Seed {} failed", seed, t);
                    }

                    long done = checked.incrementAndGet();
                    int percent = (int) ((done * 100) / total);

                    MinecraftClient.getInstance().execute(() ->
                            FilterHud.text = String.format(
                                    "Filter: %d%% (%d/%d)", percent, done, total
                            )
                    );
                }

                return localResults;
            });

            futures.add(future);
        }

        executor.shutdown();

        new Thread(() -> {
            List<JsonObject> allResults = new ArrayList<>();

            try {
                for (Future<List<JsonObject>> future : futures) {
                    allResults.addAll(future.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                RankedPractice.LOGGER.error("Seed filtering interrupted", e);
            }

            allResults.sort(Comparator.comparingLong(o -> o.get("seed").getAsLong()));

            JsonArray sortedArray = new JsonArray();
            allResults.forEach(sortedArray::add);

            SeedFilter.filter = false;
            RankedPractice.LOGGER.info("Done! Found {} seeds.", sortedArray.size());
            RankedPractice.LOGGER.info("Found seeds: {}", sortedArray);
        }).start();
    }
}
