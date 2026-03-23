package me.gege.screen;

import me.gege.RankedPractice;
import me.gege.updater.AutoUpdater;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class UpdateScreen extends Screen {
    private String message = "";
    private boolean complete = false;

    public UpdateScreen() {
        super(new LiteralText("Update"));
    }

    @Override
    protected void init() {
        ButtonWidget cancelWidget = new ButtonWidget(this.width / 2 + 10, this.height / 2, 100, 20, new LiteralText("Cancel"), w -> this.onClose());
        this.addButton(cancelWidget);

        this.addButton(
                new ButtonWidget(this.width / 2 - 110, this.height / 2, 100, 20, new LiteralText("Download"), w -> {
                    AutoUpdater.downloadLatest();

                    w.setMessage(new LiteralText("Downloading..."));
                    w.active = false;
                    cancelWidget.active = false;
                })
        );
    }

    @Override
    public void tick() {
        if (this.complete) {
            return;
        }

        if (AutoUpdater.isDone()) {
            this.buttons.clear();
            this.message = "\n\n§e§lUpdate Download Complete!";
            this.addButton(
                    new ButtonWidget(this.width / 2 - 100, this.height / 2, 200, 20, new LiteralText("Close This Instance"), w -> {
                        assert client != null;

                        client.scheduleStop();
                    })
            );

            this.complete = true;
        } else {
            message = "§lRanked Practice §7- §e§lNew Version Available!\nOld Version: v" + RankedPractice.MOD_VERSION + "\nNew Version: v" + AutoUpdater.getVersionName();
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(0);
        super.render(matrices, mouseX, mouseY, delta);

        int startY = -55;

        for (String line: this.message.split("\n")) {
            this.drawCenteredText(matrices, textRenderer, new LiteralText(line), this.width / 2, this.height / 2 + startY, 16777215);
            startY += 15;
        }
    }
}
