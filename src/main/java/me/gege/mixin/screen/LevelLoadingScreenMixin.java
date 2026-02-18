package me.gege.mixin.screen;

import me.gege.util.SeedUtil;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.util.SeedUtil.isPracticing;
import static me.gege.util.SeedUtil.seedType;

/**
 * Display seed type in loading screen
 */

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {
    protected LevelLoadingScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!isPracticing) {
            return;
        }

        int x = this.width / 2;
        int y = this.height / 2;

        String text = SeedUtil.nameFromType(seedType);

        this.drawCenteredString(matrices, this.textRenderer, text, x, y + 80, 16777215);
    }
}
