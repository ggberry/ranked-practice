package me.gege;

import me.gege.config.ConfigManager;
import me.gege.event.ModEvents;
import me.gege.seed.SeedManager;
import me.gege.worldgen.ModCarvers;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RankedPractice implements ModInitializer {
	private static final String LOGGER_NAME = "Ranked Practice";
	public static final String MOD_ID = "ranked-practice";
	public static final Logger LOGGER = LogManager.getLogger(LOGGER_NAME);
	public static final boolean DEVELOPING = false;

	@Override
	public void onInitialize() {
		SeedManager.warmUp();
		ConfigManager.load();

		ModEvents.registerEvents();
		ModCarvers.registerCarvers();

		LOGGER.info("Ranked Practice running.");
	}
}