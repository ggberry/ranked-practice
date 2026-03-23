package me.gege.updater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.gege.RankedPractice;
import me.gege.config.ConfigManager;
import me.gege.screen.UpdateScreen;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AutoUpdater {
    private static final String API_URL = "https://api.github.com/repos/ggberry/ranked-practice/releases/latest";
    private static String versionName = "unknown";
    private static String versionUrl = "";
    private static boolean updateDone = false;

    public static boolean isDone() {
        return updateDone;
    }

    public static String getVersionName() {
        return versionName;
    }

    public static void checkUpdate() {
        if (!ConfigManager.CONFIGS.autoUpdate || isLatest()) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        client.openScreen(new UpdateScreen());
    }

    public static void downloadLatest() {
        new Thread(() -> {
            try {
                InputStream in = new URL(versionUrl).openStream();
                Files.copy(in, Paths.get("mods/ranked-practice-" + versionName + ".jar"), StandardCopyOption.REPLACE_EXISTING);
                updateDone = true;
            } catch (Exception ignored) {}
        }).start();
    }

    public static void deleteOld() {
        if (!isLatest()) {
            return;
        }

        File modsDir = new File("mods");

        File[] files = modsDir.listFiles((dir, name) ->
                name.startsWith("ranked-practice-") &&
                        name.endsWith(".jar") &&
                        !name.equals("ranked-practice-" + RankedPractice.MOD_VERSION + ".jar")
        );

        if (files == null) return;

        for (File file : files) {
            if (file.delete()) {
                RankedPractice.LOGGER.info("Deleted old version: {}", file.getName());
            } else {
                RankedPractice.LOGGER.warn("Failed to delete: {}", file.getName());
            }
        }
    }

    private static boolean isLatest() {
        if (versionName.equals("unknown")) {
            return true;
        }

        return RankedPractice.MOD_VERSION.equals(versionName);
    }

    public static void getLatestInfo() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/vnd.github+json");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            JsonObject json = new Gson().fromJson(String.valueOf(response), JsonObject.class);

            versionName = json.get("name").getAsString().substring(1);
            versionUrl = json.get("assets").getAsJsonArray().get(0).getAsJsonObject().get("browser_download_url").getAsString();
        } catch (IOException ignored) {
            RankedPractice.LOGGER.warn("Unable to fetch latest version data.");
        }
    }
}
