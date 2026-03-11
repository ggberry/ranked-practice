package me.gege.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ConfirmButtonWidget extends ButtonWidget {
    private final ButtonWidget.PressAction action;
    private final String message;
    private boolean pressed;
    public final int maxAge;
    public long startTime;

    public ConfirmButtonWidget(int maxAge, String message, ButtonWidget.PressAction action) {
        super(0, 0, 0, 0, new LiteralText(""), null);

        this.action = action;
        this.maxAge = maxAge;
        this.message = message;
        this.pressed = false;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onPress() {
        this.pressed = true;
        this.action.onPress(this);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        update();
    }

    public void init(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startTime = System.currentTimeMillis();
    }

    public void update() {
        if (pressed) {
            this.active = false;
            return;
        }

        String text;
        long timeDiff = System.currentTimeMillis() - startTime;

        if (maxAge != 0 && timeDiff < maxAge) {
            double time = ((double) (maxAge - timeDiff) / 1000);
            text = "Wait... (" + String.format("%.2f", time) + "s)";
            this.active = false;
        } else {
            text = message;
            this.active = true;
        }

        this.setMessage(new LiteralText(text));
    }
}
