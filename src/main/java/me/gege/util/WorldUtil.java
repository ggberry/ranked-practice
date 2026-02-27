package me.gege.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.gege.seed.SeedManager.generateWorldInfo;
import static me.gege.util.SeedUtil.overworldSeed;

public class WorldUtil extends Screen {
    protected WorldUtil(Text title) {
        super(title);
    }

    public static void createWorldInGame() {
        MinecraftClient client = MinecraftClient.getInstance();
        playClientSound(client, SoundEvents.BLOCK_NOTE_BLOCK_PLING, 3f);

        if (client.world != null) {
            new Thread(() -> client.world.disconnect()).start();
        }
        
        client.submit(WorldUtil::createWorld);
    }

    public static void createWorld() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }

        generateWorldInfo();
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
    }

    public static void playClientSound(MinecraftClient client, SoundEvent soundEvent, float pitch) {
        client.getSoundManager().play(PositionedSoundInstance.master(soundEvent, pitch));
    }

    public static void enableCheats() {
        MinecraftClient client = MinecraftClient.getInstance();
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
