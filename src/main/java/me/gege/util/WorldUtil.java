package me.gege.util;

import me.gege.screen.DummyLevelLoadingScreen;
import me.gege.screen.widget.ConfirmButtonWidget;
import me.gege.seed.SeedManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.gege.seed.SeedManager.*;
import static me.gege.util.SeedUtil.overworldSeed;

public class WorldUtil extends Screen {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    protected WorldUtil(Text title) {
        super(title);
    }

    public static void createWorldInGame(boolean newSeed) {
        if (client.player != null) {
            client.player.sendMessage(new LiteralText("§eJoining New Seed..."), false);
        }

        WorldUtil.createWorld(newSeed);
    }

    public static void createWorld(boolean newSeed) {
        if (client == null) {
            return;
        }

        // Client-side cues
        playClientSound(client, SoundEvents.BLOCK_NOTE_BLOCK_PLING, 3f);
        sendRunToast();

        // World creation
        scheduler.schedule(() -> tryStartWorld(newSeed), 2000, TimeUnit.MILLISECONDS);
    }

    private static void tryStartWorld(boolean newSeed) {
        if (futureWorldInfo == null) {
            scheduler.schedule(() -> tryStartWorld(newSeed), 50, TimeUnit.MILLISECONDS);
            return;
        }

        client.execute(() -> {
            if (newSeed) {
                setWorldInfo();
            }

            if (client.world != null) {
                client.world.disconnect();
                client.disconnect(new DummyLevelLoadingScreen());
            }

            String worldName = getFileName();

            GeneratorOptions generatorOptions = new GeneratorOptions(
                    overworldSeed,
                    true,
                    false,
                    GeneratorOptions.method_28608(DimensionType.method_28517(overworldSeed), GeneratorOptions.createOverworldGenerator(overworldSeed))
            );

            LevelInfo levelInfo = new LevelInfo(
                    worldName,
                    GameMode.SURVIVAL,
                    false,
                    Difficulty.EASY,
                    false,
                    new GameRules(),
                    DataPackSettings.SAFE_MODE
            );

            client.method_29607(worldName, levelInfo, RegistryTracker.create(), generatorOptions);
        });
    }

    public static void playClientSound(MinecraftClient client, SoundEvent soundEvent, float pitch) {
        client.getSoundManager().play(PositionedSoundInstance.master(soundEvent, pitch));
    }

    public static void enableCheats() {
        ClientPlayerEntity player = client.player;
        MinecraftServer server = client.getServer();
        
        if (player == null || server == null) {
            return;
        }

        playClientSound(client, SoundEvents.BLOCK_NOTE_BLOCK_PLING, 3f);

        PlayerManager playerManager = server.getPlayerManager();
        ServerPlayerEntity serverPlayer = playerManager.getPlayerList().get(0);

        playerManager.setCheatsAllowed(true);
        playerManager.sendCommandTree(serverPlayer);
        player.setClientPermissionLevel(server.getPermissionLevel(player.getGameProfile()));

        player.sendMessage(new LiteralText("§aCheats have been enabled"), false);
    }

    public static void checkCheats(ConfirmButtonWidget widget) {

        MinecraftServer server = client.getServer();

        if (server == null) {
            return;
        }

        PlayerManager playerManager = server.getPlayerManager();

        if (playerManager.areCheatsAllowed()) {
            widget.setMessage(new LiteralText("Cheats Enabled"));
            widget.setPressed();
        }
    }

    public static void checkTypeEnabled(ConfirmButtonWidget buttonWidget) {
        if (SeedManager.getEnabledTypes().isEmpty()) {
            buttonWidget.setPressed();
        }
    }

    public static void sendRunToast() {
        client.getToastManager().add(
                SystemToast.method_29047(
                        client,
                        SystemToast.Type.TUTORIAL_HINT,
                        new LiteralText("Ranked Practice"),
                        new LiteralText("Starting New Speedrun...")
                )
        );
    }

    public static String getFileName() {
        File directory = new File("saves");
        File[] files = directory.listFiles();
        if (files == null) return "ranked-practice Run #";

        int highestNumber = 0;

        Pattern pattern = Pattern.compile("^ranked-practice - Practice Run #(\\d+)(?:\\.[^.]+)?$");

        for (File f : files) {
            Matcher m = pattern.matcher(f.getName());
            if (m.matches()) {
                int num = Integer.parseInt(m.group(1));
                if (num > highestNumber) {
                    highestNumber = num;
                }
            }
        }

        return "ranked-practice - Practice Run #" + (highestNumber + 1);
    }
}
