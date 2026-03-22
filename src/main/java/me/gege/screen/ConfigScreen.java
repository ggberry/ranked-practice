package me.gege.screen;

import me.gege.config.ConfigManager;
import me.gege.config.ModConfigs;
import me.gege.seed.SeedManager;
import me.gege.util.SeedUtil;
import me.gege.screen.widget.UpdateCheckboxWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import static me.gege.util.SeedUtil.updateSeedButton;

public class ConfigScreen extends Screen {
    public ConfigScreen() {
        super(new LiteralText("Config Screen"));
    }

    @Override
    protected void init() {
        ModConfigs configs = ConfigManager.CONFIGS;
        String autoUpdateText = "Automatically Update";

        this.addButton(
                new UpdateCheckboxWidget(
                        this.width / 2 - 10 - textRenderer.getWidth(autoUpdateText) / 2, 100 + 20, 20, 20,
                        new LiteralText(autoUpdateText),
                        () -> configs.autoUpdate,
                        value -> {
                            configs.autoUpdate = value;
                            ConfigManager.save();
                        }
                )
        );

        this.addButton(new ButtonWidget(this.width / 2 - 100, 100 + 50, 200, 20,
                new LiteralText("Villages: " + (configs.doVillages ? "On" : "Off")),
                SeedUtil::updateSeedButton)
        );

        this.addButton(new ButtonWidget(this.width / 2 - 100, 100 + 75, 200, 20,
                new LiteralText("Shipwrecks: " + (configs.doShipwrecks ? "On" : "Off")),
                SeedUtil::updateSeedButton)
        );

        this.addButton(new ButtonWidget(this.width / 2 - 100, 100 + 100, 200, 20,
                new LiteralText("Buried Treasures: " + (configs.doTreasures ? "On" : "Off")),
                SeedUtil::updateSeedButton)
        );

        this.addButton(new ButtonWidget(this.width / 2 - 100, 100 + 125, 200, 20,
                new LiteralText("Desert Temples: " + (configs.doTemples ? "On" : "Off")),
                SeedUtil::updateSeedButton)
        );

        this.addButton(new ButtonWidget(this.width / 2 - 100, 100 + 150, 200, 20,
                new LiteralText("Ruined Portals: " + (configs.doPortals ? "On" : "Off")),
                SeedUtil::updateSeedButton)
        );

        this.addButton(new ButtonWidget(this.width / 2 - 50, this.height / 2 + 200, 100, 20, new LiteralText("Done"),
                buttonWidget -> this.onClose())
        );
    }

    @Override
    public void onClose() {
        super.onClose();
        SeedManager.preloadWorldInfo();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        this.drawCenteredText(matrices, textRenderer, new LiteralText("§e§lRanked Practice Configs"), this.width / 2, 100, 16777215);

        if (SeedManager.getEnabledTypes().isEmpty()) {
            this.drawCenteredText(matrices, textRenderer, new LiteralText("§c§lWarning: §rNo seed types enabled."), this.width / 2, 300, 16777215);
            this.drawCenteredText(matrices, textRenderer, new LiteralText("You will be unable to use practice."), this.width / 2, 315, 16777215);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }
}
