package me.gege.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.gege.RankedPractice;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads and saves configurations for the mod
 */

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(RankedPractice.MOD_ID + ".json");

    public static ModConfigs CONFIGS = new ModConfigs();

    public static void load() {
        /* Load configurations */

        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                CONFIGS = GSON.fromJson(reader, ModConfigs.class);
            } catch (IOException e) {
                RankedPractice.LOGGER.error("Unable to load Ranked Configs");
            }
        } else {
            save();
        }
    }

    public static void save() {
        /* Save configurations */

        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(CONFIGS, writer);
        } catch (IOException e) {
            RankedPractice.LOGGER.error("Unable to save Ranked Configs");
        }
    }
}
