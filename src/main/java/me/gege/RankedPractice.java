package me.gege;

import me.gege.config.ConfigManager;
import me.gege.event.ModEvents;
import me.gege.seed.SeedManager;
import me.gege.timer.ModTimer;
import me.gege.updater.AutoUpdater;
import me.gege.worldgen.ModCarvers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RankedPractice implements ModInitializer {
	private static final String LOGGER_NAME = "Ranked Practice";
	public static final Logger LOGGER = LogManager.getLogger(LOGGER_NAME);
	public static final String MOD_ID = "ranked-practice";
	public static final String MOD_VERSION;

	@Override
	public void onInitialize() {
		AutoUpdater.getLatestInfo();
		SeedManager.preloadWorldInfo();
		ConfigManager.load();

		ModTimer.initializeTimer();
		ModEvents.registerEvents();
		ModCarvers.registerCarvers();

		LOGGER.info("Ranked Practice running.");
	}

	static {
		MOD_VERSION = FabricLoader.getInstance()
				.getModContainer(MOD_ID)
				.map(mod -> mod.getMetadata().getVersion().getFriendlyString())
				.orElse("unknown");
	}
}