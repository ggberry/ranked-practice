package me.gege.screen;

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
    private static ButtonWidget confirmButton = new ButtonWidget(0, 0, 0, 0, new LiteralText(""), null);
    private static final int maxAge = 40;
    private static int age = 0;

    public SaveConfirmScreen() {
        super(new LiteralText("Are You Sure?"));
    }

    @Override
    protected void init() {
        age = 0;

        confirmButton = new ButtonWidget(
                width / 2 - 102,
                height / 4 + 72 + 32,
                98,
                20,
                new LiteralText(""),
                widget -> saveAndQuit(client, widget)
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
                            this.client.openScreen(null);
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

    @Override
    public void tick() {
        updateConfirmButton(client, this.width, this.height);

        super.tick();
    }

    private static void updateConfirmButton(MinecraftClient client, int width, int height) {
        if (client.world == null || !client.world.isClient) {
            return;
        }

        System.out.println(age);
        String text;

        if (age < maxAge) {
            double time = ((double) (maxAge - age) / 20);
            text = "Wait... (" + String.format("%.1f", time) + "s)";
            confirmButton.setAlpha(0.5f);
        } else {
            text = "Confirm";
            confirmButton.setAlpha(1f);
        }

        confirmButton.setMessage(new LiteralText(text));

        age++;
    }

    private static void saveAndQuit(MinecraftClient client, ButtonWidget widget) {
        if (client.world == null || age < maxAge) {
            return;
        }

        isPracticing = false;

        boolean bl = client.isInSingleplayer();
        boolean bl2 = client.isConnectedToRealms();
        widget.active = false;

        client.world.disconnect();

        if (bl) {
            client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
        } else {
            client.disconnect();
        }

        if (bl) {
            client.openScreen(new TitleScreen());
        } else if (bl2) {
            RealmsBridge realmsBridge = new RealmsBridge();
            realmsBridge.switchToRealms(new TitleScreen());
        } else {
            client.openScreen(new MultiplayerScreen(new TitleScreen()));
        }
    }
}
