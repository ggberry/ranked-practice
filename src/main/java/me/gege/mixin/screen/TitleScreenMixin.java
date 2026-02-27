package me.gege.mixin.screen;

import me.gege.RankedPractice;
import me.gege.screen.ConfigScreen;
import me.gege.util.WorldUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.util.GeneralUtil.RANKED_SETTINGS_LOCATION;
import static me.gege.util.WorldUtil.playClientSound;

/**
 * Removes Multiplayer & Realm options and replaces them with Practice.
 */

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "initWidgetsNormal", cancellable = true)
    private void initWidgetsNormal(int y, int spacingY, CallbackInfo ci) {
        this.addButton(
                new ButtonWidget(
                        this.width / 2 - 100, y, 200, 20, new TranslatableText("menu.singleplayer"), buttonWidget -> this.client.openScreen(new SelectWorldScreen(this))
                )
        );

        this.addButton(
                new ButtonWidget(this.width / 2 - 100, y + spacingY, 200, 20, new LiteralText("Practice"), buttonWidget -> {
                    assert client != null;
                    playClientSound(client, SoundEvents.BLOCK_NOTE_BLOCK_PLING, 3f);
                    WorldUtil.createWorld();
                })
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
                        buttonWidget -> this.client.openScreen(new ConfigScreen(this.client.currentScreen)),
                        new LiteralText("Ranked Configurations")
                )
        );

        ci.cancel();
    }
}
