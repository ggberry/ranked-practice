package me.gege.mixin.screen;

import me.gege.screen.ConfigScreen;
import me.gege.screen.widget.ConfirmButtonWidget;
import me.gege.updater.AutoUpdater;
import me.gege.util.WorldUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.util.GeneralUtil.RANKED_SETTINGS_LOCATION;

/**
 * Removes Multiplayer & Realm options and replaces them with Practice.
 */

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Shadow private long backgroundFadeStart;

    @Shadow @Final private boolean doBackgroundFade;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "render")
    private void render(CallbackInfo ci) {
        if (!this.doBackgroundFade) {
            return;
        }

        float timeDelta = (float)(Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F;
        float alpha = MathHelper.clamp(timeDelta - 1.0F, 0.0F, 1.0F);

        if (alpha == 1) {
            AutoUpdater.checkUpdate();
        }
    }

    @Inject(at = @At("HEAD"), method = "initWidgetsNormal", cancellable = true)
    private void initWidgetsNormal(int y, int spacingY, CallbackInfo ci) {
        assert client != null;

        this.addButton(
                new ButtonWidget(
                        this.width / 2 - 100, y, 200, 20, new TranslatableText("menu.singleplayer"), buttonWidget -> this.client.openScreen(new SelectWorldScreen(this))
                )
        );

        this.addButton(
                new ConfirmButtonWidget(this.width / 2 - 100, y + spacingY, 200, 20, 0, "Practice",
                        w -> WorldUtil.createWorld(true),
                        w -> WorldUtil.checkTypeEnabled((ConfirmButtonWidget) w)
                )
        );

        this.addButton(
                new TexturedButtonWidget(
                        this.width / 2 - 124,
                        y + spacingY,
                        20,
                        20,
                        0,
                        0,
                        20,
                        RANKED_SETTINGS_LOCATION,
                        20,
                        40,
                        buttonWidget -> this.client.openScreen(new ConfigScreen()),
                        new LiteralText("Ranked Configurations")
                )
        );

        ci.cancel();
    }
}
