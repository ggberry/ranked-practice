package me.gege.screen;

import me.gege.screen.widget.ConfirmButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import static me.gege.util.SeedUtil.isPracticing;

public class SaveConfirmScreen extends Screen {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final ConfirmButtonWidget confirmButton = new ConfirmButtonWidget(1000, "Confirm", SaveConfirmScreen::saveAndQuit);

    public SaveConfirmScreen() {
        super(new LiteralText("Are You Sure?"));
    }

    @Override
    protected void init() {
        confirmButton.init(
                width / 2 - 102,
                height / 4 + 72 + 32,
                98,
                20
        );

        this.addButton(confirmButton);

        this.addButton(
                new ButtonWidget(
                        this.width / 2 + 4,
                        this.height / 4 + 72 + 32,
                        98,
                        20,
                        new LiteralText("Cancel"),
                        buttonWidgetx -> {
                            client.openScreen(null);
                        }
                )
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
        this.drawCenteredText(matrices, this.textRenderer, new LiteralText("§lAre you sure you want to §e§lExit§r§l?"), this.width / 2, this.height / 4 + 72, 16777215);

        super.render(matrices, mouseX, mouseY, delta);
    }

    private static void saveAndQuit(ButtonWidget widget) {
        if (SaveConfirmScreen.client.world == null || (System.currentTimeMillis() - confirmButton.startTime) < confirmButton.maxAge) {
            return;
        }

        isPracticing = false;

        SaveConfirmScreen.client.world.disconnect();

        SaveConfirmScreen.client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
        SaveConfirmScreen.client.openScreen(new TitleScreen());
    }
}
