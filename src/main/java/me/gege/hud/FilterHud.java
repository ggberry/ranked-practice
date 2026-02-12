package me.gege.hud;

import me.gege.seed.util.SeedFilter;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class FilterHud implements HudRenderCallback {
    public static String text;

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (!SeedFilter.filter || client.player == null) return;

        TextRenderer textRenderer = client.textRenderer;

        int x = 10;
        int y = 10;

        textRenderer.drawWithShadow(
                matrixStack,
                text,
                x,
                y,
                0xFFFFFF
        );
    }
}
