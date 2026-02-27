package me.gege.seed;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.gege.RankedPractice;
import me.gege.config.ConfigManager;
import net.minecraft.util.math.ChunkPos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static me.gege.util.SeedUtil.*;

/**
 * Requests random seeds from API
 */

public class SeedManager {
    private static final String API_URL = "https://ranked-practice.onrender.com/";

    public static void generateWorldInfo() {
        JsonObject seedInfo = getRandomSeed();

        JsonObject overworldInfo = seedInfo.get("overworld").getAsJsonObject();
        JsonObject netherInfo = seedInfo.get("nether").getAsJsonObject();
        int typeInfo = seedInfo.get("type").getAsInt();

        long newOverworldSeed = overworldInfo.get("seed").getAsLong();
        int overworldChunkX = overworldInfo.get("chunkX").getAsInt();
        int overworldChunkZ = overworldInfo.get("chunkZ").getAsInt();

        long newNetherSeed = netherInfo.get("seed").getAsLong();

        magmaRavines.clear();
        inNether = false;
        isPracticing = true;

        overworldSeed = newOverworldSeed;
        netherSeed = newNetherSeed;
        seedType = typeInfo;
        sourcePos = new ChunkPos(overworldChunkX, overworldChunkZ);
    }

    private static JsonObject getRandomSeed() {
        try {
            String suffix = "request-seed/" + portalCheck(ConfigManager.CONFIGS.seedType);

            URL url = new URL(API_URL + suffix);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();

            Gson gson = new Gson();

            return gson.fromJson(response.toString(), JsonObject.class);
        } catch (Exception e) {
            RankedPractice.LOGGER.error("Failed to load Ranked Practice seed.");
        }

        return new JsonObject();
    }
}
