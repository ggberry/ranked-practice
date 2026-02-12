package me.gege.seed.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.gege.RankedPractice;
import net.minecraft.util.math.ChunkPos;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Random;

import static me.gege.util.SeedUtil.*;

public class SeedManager {
    public static JsonArray VILLAGE_SEEDS = new JsonArray();
    public static JsonArray SHIPWRECK_SEEDS = new JsonArray();
    public static JsonArray TREASURE_SEEDS = new JsonArray();
    public static JsonArray TEMPLE_SEEDS = new JsonArray();
    public static JsonArray NORMAL_PORTAL_SEEDS = new JsonArray();
    public static JsonArray BUCKET_PORTAL_SEEDS = new JsonArray();
    public static JsonArray NETHER_SEEDS = new JsonArray();

    private static final String URL_PREFIX = getGistUrl();
    private static final String VILLAGE_SEEDS_URL = "village_seeds.json";
    private static final String SHIPWRECK_SEEDS_URL = "shipwreck_seeds.json";
    private static final String TREASURE_SEEDS_URL = "treasure_seeds.json";
    private static final String TEMPLE_SEEDS_URL = "temple_seeds.json";
    private static final String NORMAL_PORTAL_SEEDS_URL = "normal_portal_seeds.json";
    private static final String BUCKET_PORTAL_SEEDS_URL = "bucket_portal_seeds.json";
    private static final String NETHER_SEEDS_URL = "nether_seeds.json";

    public static HashMap<Long, ChunkPos> getWorldInfo() {
        Random random = new Random();
        seedType = random.nextInt(6);
        seedType = random.nextInt(3);

        JsonObject overworldInfo;
        JsonObject netherInfo = getNetherInfo();

        if (seedType == 0) {
            overworldInfo = getInfoFromArray(VILLAGE_SEEDS);
        } else if (seedType == 1) {
            overworldInfo = getInfoFromArray(SHIPWRECK_SEEDS);
        } else if (seedType == 2) {
            overworldInfo = getInfoFromArray(TREASURE_SEEDS);
        } else if (seedType == 3) {
            overworldInfo = getInfoFromArray(TEMPLE_SEEDS);
        } else if (seedType == 4) {
            overworldInfo = getInfoFromArray(NORMAL_PORTAL_SEEDS);
        } else {
            overworldInfo = getInfoFromArray(BUCKET_PORTAL_SEEDS);
        }

        long newOverworldSeed = overworldInfo.get("seed").getAsLong();
        int owChunkX = overworldInfo.get("chunkX").getAsInt();
        int owChunkZ = overworldInfo.get("chunkZ").getAsInt();

        long newNetherSeed = netherInfo.get("seed").getAsLong();

        magmaRavines.clear();
        inNether = false;
        isPracticing = true;
        overworldSeed = newOverworldSeed;
        netherSeed = newNetherSeed;
        sourcePos = new ChunkPos(owChunkX, owChunkZ);

        HashMap<Long, ChunkPos> info = new HashMap<>();
        info.put(newOverworldSeed, sourcePos);

        return info;
    }

    private static JsonObject getInfoFromArray(JsonArray array) {
        Random random = new Random();
        return array.get(random.nextInt(array.size())).getAsJsonObject();
    }

    public static JsonObject getNetherInfo() {
        Random random = new Random();

        return NETHER_SEEDS.get(random.nextInt(NETHER_SEEDS.size())).getAsJsonObject();
    }

    public static void loadSeedsFromGist() {
        VILLAGE_SEEDS = getSeedArray(VILLAGE_SEEDS_URL);
        SHIPWRECK_SEEDS = getSeedArray(SHIPWRECK_SEEDS_URL);
        TREASURE_SEEDS = getSeedArray(TREASURE_SEEDS_URL);
        TEMPLE_SEEDS = getSeedArray(TEMPLE_SEEDS_URL);
        NORMAL_PORTAL_SEEDS = getSeedArray(NORMAL_PORTAL_SEEDS_URL);
        BUCKET_PORTAL_SEEDS = getSeedArray(BUCKET_PORTAL_SEEDS_URL);

        NETHER_SEEDS = getSeedArray(NETHER_SEEDS_URL);

        String seedPoolInfo = "Ranked Practice - Loaded Seed Count:\n\t-Village: " + VILLAGE_SEEDS.size() + "\n\t-Shipwreck: " + SHIPWRECK_SEEDS.size() +
                            "\n\t-Buried Treasure: " + TREASURE_SEEDS.size() + "\n\t-Desert Temple: " + TEMPLE_SEEDS.size() + "\n\t-Regular Ruined Portal: " + NORMAL_PORTAL_SEEDS.size() +
                            "\n\t-Bucket Ruined Portal: " + BUCKET_PORTAL_SEEDS.size() + "\n\t-Nether: " + NETHER_SEEDS.size();

        RankedPractice.LOGGER.info(seedPoolInfo);
    }

    public static JsonArray getSeedArray(String URL) {
        try {
            URL url = new URL(URL_PREFIX + URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Accept", "application/vnd.github.v3.raw");
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            JsonArray array = gson.fromJson(reader, JsonArray.class);

            reader.close();
            inputStream.close();
            connection.disconnect();

            return array;
        } catch (Exception error) {
            RankedPractice.LOGGER.info("[ERROR] Unable to load Ranked-Practice seeds");
        }

        return null;
    }

    private static String getGistUrl() {
        String path = "/seed-url.txt";
        InputStream is = RankedPractice.class.getResourceAsStream(path);

        if (is == null) {
            throw new RuntimeException("Resource not found: " + path);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + path, e);
        }
    }
}
