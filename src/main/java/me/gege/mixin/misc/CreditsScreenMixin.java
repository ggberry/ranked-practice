package me.gege.mixin.misc;

import me.gege.RankedPractice;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.util.SeedUtil.isPracticing;

@Mixin(CreditsScreen.class)
public abstract class CreditsScreenMixin {
    @Shadow protected abstract void close();

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        if (!isPracticing) {
            return;
        }

        this.close();
    }
}
