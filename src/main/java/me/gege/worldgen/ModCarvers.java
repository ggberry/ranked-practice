package me.gege.worldgen;

import me.gege.RankedPractice;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;

public class ModCarvers {
    public static final RankedRavineCarver RANKED_RAVINE_CARVER =
            new RankedRavineCarver(ProbabilityConfig.CODEC);

    public static ConfiguredCarver<ProbabilityConfig> CONFIGURED_RANKED_RAVINE = new ConfiguredCarver<>(RANKED_RAVINE_CARVER, RankedRavineCarver.config);

    public static void registerCarvers() {
        Registry.register(
                Registry.CARVER,
                new Identifier(RankedPractice.MOD_ID, "ranked_ravine_carver"),
                RANKED_RAVINE_CARVER
        );
    }
}
