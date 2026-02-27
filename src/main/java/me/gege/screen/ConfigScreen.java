package me.gege.screen;

import me.gege.config.ConfigManager;
import me.gege.util.SeedUtil;
import me.gege.screen.widget.UpdateCheckboxWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ConfigScreen extends Screen {
    private final Screen oldScreen;

    public ConfigScreen(Screen oldScreen) {
        super(new LiteralText("Config Screen"));

        this.oldScreen = oldScreen;
    }

    @Override
    protected void init() {
        String autoUpdateText = "Automatically Update";

        this.addButton(
                new UpdateCheckboxWidget(
                        this.width / 2 - 10 - textRenderer.getWidth(autoUpdateText) / 2, 100 + 20, 20, 20,
                        new LiteralText(autoUpdateText),
                        () -> ConfigManager.CONFIGS.autoUpdate,
                        value -> {
                            ConfigManager.CONFIGS.autoUpdate = value;
                            ConfigManager.save();
                        }
                )
        );

        this.addButton(new ButtonWidget(this.width / 2 - 100, 100 + 50, 200, 20,
                new LiteralText("Seed Type: " + SeedUtil.formattedFromType(ConfigManager.CONFIGS.seedType)),
                SeedUtil::updateSeedButton)
        );

        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 2 + 200, 200, 20, new LiteralText("Done"),
                buttonWidget -> this.client.openScreen(this.oldScreen))
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        this.drawCenteredText(matrices, textRenderer, new LiteralText("§e§lRanked Practice BarterConfigs"), this.width / 2, 100, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
