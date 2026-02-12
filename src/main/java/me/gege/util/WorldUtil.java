package me.gege.util;

import me.gege.seed.util.SeedManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
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

public class WorldUtil extends Screen {
    protected WorldUtil(Text title) {
        super(title);
    }

    public static void createWorldInGame() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world != null) {
            client.world.disconnect();
            client.disconnect(new SaveLevelScreen(new LiteralText("Leaving world...")));
        }

        client.submit(WorldUtil::createWorld);
    }

    public static void createWorld() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }

        long seed = (long) SeedManager.getWorldInfo().keySet().toArray()[0];
        String worldName = getFileName();

        GeneratorOptions generatorOptions = new GeneratorOptions(
                seed,
                true,
                false,
                GeneratorOptions.method_28608(DimensionType.method_28517(seed), GeneratorOptions.createOverworldGenerator(seed))
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

        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_PLING, 3f));
        client.method_29607(worldName, levelInfo, RegistryTracker.create(), generatorOptions);
    }

    public static void enableCheats() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        MinecraftServer server = client.getServer();
        
        if (player == null || server == null) {
            return;
        }

        PlayerManager playerManager = server.getPlayerManager();
        ServerPlayerEntity serverPlayer = playerManager.getPlayerList().get(0);

        playerManager.setCheatsAllowed(true);
        playerManager.sendCommandTree(serverPlayer);

        player.setClientPermissionLevel(server.getPermissionLevel(player.getGameProfile()));
        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_PLING, 3f));
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
