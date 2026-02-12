package me.gege.screen;

import me.gege.config.ConfigManager;
import me.gege.screen.widget.UpdateCheckboxWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ConfigScreen extends Screen {
    public ConfigScreen() {
        super(new LiteralText("Config Screen"));
    }

    @Override
    protected void init() {
        String autoUpdateText = "Automatically Update Mod";

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
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        this.drawCenteredText(matrices, textRenderer, new LiteralText("§e§lRanked Practice Configs"), this.width / 2, 100, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
