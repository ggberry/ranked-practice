package me.gege.util;

import me.gege.data.PlayerEntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.*;
import java.util.stream.Collectors;

public class GeneralUtil {
    public static List<Integer> generatePityList() {
        Random random = new Random();
        Set<Integer> usedPity = new HashSet<>();
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < Configs.PEAL_PITY + Configs.OBSIDIAN_PITY; i++) {
            int pityNumber = random.nextInt(Configs.BARTER_PITY) + 1;

            while (usedPity.contains(pityNumber)) {
                pityNumber = random.nextInt(Configs.BARTER_PITY) + 1;
            }

            result.add(pityNumber);
            usedPity.add(pityNumber);
        }

        return result;
    }

    public static List<Integer> listFromArray(int[] arr) {
        return Arrays.stream(arr)
                .boxed()
                .collect(Collectors.toList());
    }

    public static PlayerEntityData getPlayerData(World world) {
        List<? extends PlayerEntity> players = world.getPlayers();
        if (players.isEmpty()) {
            return null;
        }

        return (PlayerEntityData) players.get(0);
    }

    public static void restorePlayerData(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        if (oldPlayer.getType() == EntityType.PLAYER) {
            PlayerEntityData playerData = (PlayerEntityData) newPlayer;
            PlayerEntityData oldPlayerData = (PlayerEntityData) oldPlayer;

            playerData.setGoldBartered(oldPlayerData.getBarteredGold());
            playerData.setEyesThrown(oldPlayerData.getEyesThrown());
            playerData.setPortals(oldPlayerData.getPortals());
            playerData.setObsidianPity(oldPlayerData.getObsidianPity());
            playerData.setPearlPity(oldPlayerData.getPearlPity());
        }
    }
}
