package me.gege.mixin.screen;

import me.gege.screen.SaveConfirmScreen;
import me.gege.util.WorldUtil;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.util.SeedUtil.isPracticing;

/**
 * Replaces buttons in Pause Menu for more useful ones
 */

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "initWidgets", cancellable = true)
    private void initWidgets(CallbackInfo ci) {
        if (!isPracticing) {
            return;
        }

        this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 24 + -16, 204, 20, new TranslatableText("menu.returnToGame"), buttonWidgetx -> {
            this.client.openScreen(null);
            this.client.mouse.lockCursor();
        }));
        this.addButton(
                new ButtonWidget(
                        this.width / 2 - 102,
                        this.height / 4 + 48 + -16,
                        98,
                        20,
                        new TranslatableText("gui.advancements"),
                        buttonWidgetx -> this.client.openScreen(new AdvancementsScreen(this.client.player.networkHandler.getAdvancementHandler()))
                )
        );
        this.addButton(
                new ButtonWidget(
                        this.width / 2 + 4,
                        this.height / 4 + 48 + -16,
                        98,
                        20,
                        new TranslatableText("gui.stats"),
                        buttonWidgetx -> this.client.openScreen(new StatsScreen(this, this.client.player.getStatHandler()))
                )
        );
        String string = SharedConstants.getGameVersion().isStable() ? "https://aka.ms/javafeedback?ref=game" : "https://aka.ms/snapshotfeedback?ref=game";
        this.addButton(
                new ButtonWidget(
                        this.width / 2 - 102,
                        this.height / 4 + 72 + -16,
                        98,
                        20,
                        new TranslatableText("menu.sendFeedback"),
                        buttonWidgetx -> this.client.openScreen(new ConfirmChatLinkScreen(bl -> {
                            if (bl) {
                                Util.getOperatingSystem().open(string);
                            }

                            this.client.openScreen(this);
                        }, string, true))
                )
        );
        this.addButton(
                new ButtonWidget(
                        this.width / 2 + 4,
                        this.height / 4 + 72 + -16,
                        98,
                        20,
                        new LiteralText("Enable Cheats"),
                        buttonWidgetx -> {
                            WorldUtil.enableCheats();
                        }
                )
        );
        this.addButton(
                new ButtonWidget(
                        this.width / 2 - 102,
                        this.height / 4 + 96 + -16,
                        98,
                        20,
                        new TranslatableText("menu.options"),
                        buttonWidgetx -> this.client.openScreen(new OptionsScreen(this, this.client.options))
                )
        );

        ButtonWidget buttonWidget = this.addButton(
                new ButtonWidget(
                        this.width / 2 + 4,
                        this.height / 4 + 96 + -16,
                        98,
                        20,
                        new LiteralText("New Seed"),
                        buttonWidgetx -> {
                            WorldUtil.createWorldInGame();
                        }
                )
        );

        buttonWidget.active = this.client.isIntegratedServerRunning() && !this.client.getServer().isRemote();
        ButtonWidget buttonWidget2 = this.addButton(
                new ButtonWidget(this.width / 2 - 102, this.height / 4 + 120 + -16, 204, 20, new TranslatableText("menu.returnToMenu"), buttonWidgetx -> {
                    this.client.openScreen(new SaveConfirmScreen());
                })
        );

        if (!this.client.isInSingleplayer()) {
            buttonWidget2.setMessage(new TranslatableText("menu.disconnect"));
        }

        ci.cancel();
    }
}
