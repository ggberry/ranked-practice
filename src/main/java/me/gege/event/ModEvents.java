package me.gege.event;

import me.gege.command.ModCommands;
import me.gege.hud.FilterHud;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class ModEvents {
    public static void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(new ServerLifecycleHandler());

        HudRenderCallback.EVENT.register(new FilterHud());
        ModCommands.register();
    }
}
