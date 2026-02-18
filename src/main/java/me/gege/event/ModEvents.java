package me.gege.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

/**
 * Registers events for the mod:
 * - Lava pool generation during ServerLifeCycleEvents
 */

public class ModEvents {
    public static void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(new ServerLifecycleHandler());
    }
}
