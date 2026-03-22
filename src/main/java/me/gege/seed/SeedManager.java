package me.gege.seed;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.gege.RankedPractice;
import me.gege.config.ConfigManager;
import me.gege.config.ModConfigs;
import me.gege.util.DragonUtil;
import net.minecraft.util.math.ChunkPos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.gege.util.SeedUtil.*;

/**
 * Requests random seeds from API
 */

public class SeedManager {
    private static final String API_URL = "https://ranked-practice.onrender.com/";
    public static volatile WorldInfo futureWorldInfo;

    public static void preloadWorldInfo() {
        futureWorldInfo = null;
        new Thread(() -> futureWorldInfo = generateWorldInfo()).start();
    }

    public static void setWorldInfo() {
        if (futureWorldInfo == null) {
            preloadWorldInfo();
        }

        WorldInfo info = futureWorldInfo;

        DragonUtil.init();

        magmaRavines.clear();
        inNether = false;
        isPracticing = true;

        overworldSeed = info.overworldSeed;
        netherSeed = info.netherSeed;
        seedType = info.seedType;
        sourcePos = new ChunkPos(info.overworldChunkX, info.overworldChunkZ);

        preloadWorldInfo();
    }

    private static WorldInfo generateWorldInfo() {
        JsonObject seedInfo = randomSeedInfo();

        JsonObject overworldInfo = seedInfo.get("overworld").getAsJsonObject();
        JsonObject netherInfo = seedInfo.get("nether").getAsJsonObject();
        int typeInfo = seedInfo.get("type").getAsInt();

        long newOverworldSeed = overworldInfo.get("seed").getAsLong();
        int overworldChunkX = overworldInfo.get("chunkX").getAsInt();
        int overworldChunkZ = overworldInfo.get("chunkZ").getAsInt();

        long newNetherSeed = netherInfo.get("seed").getAsLong();

        return new WorldInfo(
                newOverworldSeed,
                overworldChunkX,
                overworldChunkZ,
                newNetherSeed,
                typeInfo
        );
    }

    private static JsonObject randomSeedInfo() {
        try {
            String type = getSeedType();
            String suffix = "request-seed/" + type;

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

    private static String getSeedType() {
        List<String> enabledTypes = getEnabledTypes();
        Collections.shuffle(enabledTypes);

        return portalCheck(enabledTypes.get(0));
    }

    public static List<String> getEnabledTypes() {
        List<String> enabledTypes = new ArrayList<>();
        ModConfigs configs = ConfigManager.CONFIGS;

        if (configs.doVillages) {
            enabledTypes.add("village");
        } if (configs.doShipwrecks) {
            enabledTypes.add("shipwreck");
        } if (configs.doTreasures) {
            enabledTypes.add("treasure");
        } if (configs.doTemples) {
            enabledTypes.add("temple");
        } if (configs.doPortals) {
            enabledTypes.add("portal");
        }

        return enabledTypes;
    }
}
