package me.gege.event;

import me.gege.util.SeedUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import static me.gege.util.SeedUtil.isOceanSeed;
import static me.gege.util.SeedUtil.isPracticing;

public class ServerLifecycleHandler implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        if (!isPracticing) {
            return;
        }

        ServerWorld world = server.getOverworld();

        if (!isOceanSeed()) {
            SeedUtil.generateLavaPools(world);
        }
    }
}
