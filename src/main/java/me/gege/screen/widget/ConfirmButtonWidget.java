package me.gege.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

public class ConfirmButtonWidget extends ButtonWidget {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private final ButtonWidget.PressAction action;
    private final ButtonWidget.PressAction startAction;
    private boolean startActionDone;
    private boolean pressed;
    public final int maxAge;
    public long startTime;

    public ConfirmButtonWidget(int x, int y, int width, int height, int maxAge, String message, ButtonWidget.PressAction action, @Nullable ButtonWidget.PressAction startAction) {
        super(x, y, width, height, new LiteralText(message), action);

        this.action = action;
        this.startAction = startAction;
        this.maxAge = maxAge;
        this.startActionDone = false;
        this.pressed = false;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onPress() {
        this.action.onPress(this);
        this.setPressed();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.update(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void setPressed() {
        this.pressed = true;
    }

    public void update(MatrixStack matrices) {
        if (startAction != null && !startActionDone) {
            startAction.onPress(this);
            startActionDone = true;
        }

        if (pressed) {
            this.active = false;
            this.setMessage(new LiteralText("-"));
            return;
        }

        long timeDiff = System.currentTimeMillis() - startTime;

        if (maxAge != 0 && timeDiff < maxAge) {
            double time = ((double) (maxAge - timeDiff) / 1000);
            String text = String.format("%.1f", time) + "s";
            this.drawStringWithShadow(matrices, client.textRenderer, text, this.x + (this.width - client.textRenderer.getWidth(text)) / 2, this.y + this.height / 2 - 4, 16777215);

            this.active = false;

            if (this.maxAge > 0) {
                this.setAlpha(0.25f);
            }
        } else {
            this.active = true;

            if (this.maxAge > 0) {
                this.setAlpha(1f);
            }
        }
    }
}
