package me.gege.timer;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.InGameTimerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import static me.gege.util.WorldUtil.playClientSound;

public class ModTimer {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void initializeTimer() {
        InGameTimer.onComplete(inGameTimer -> {
            if (client.player == null) {
                return;
            }

            String detailText = "Match Winner: §e" + client.player.getName().getString() + "§r (" + InGameTimerUtils.timeToStringFormat(inGameTimer.getRealTimeAttack()) + ")";

            new Thread(() -> {
                try {
                    Thread.sleep(500);

                    sendTitle(new LiteralText("§e§lVictory!"), null);
                    sendTitle(null, new LiteralText(detailText));
                    playClientSound(client, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1f);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });
    }

    private static void sendTitle(Text title, Text subtitle) {
        client.inGameHud.setTitles(title, subtitle, 10, 120, 20);
    }
}
