package me.gege.data;

import java.util.List;

/**
 * Utility functions for player NBT data
 * <p>
 * Tracks:
 * - Bartered gold
 * - Eyes thrown
 * - Portals generated
 * - Obsidian pity
 * - Ender pearl pity
 * </p>
 */

public interface PlayerEntityData {
    void addBarteredGold();
    int getBarteredGold();
    void setGoldBartered(int amount);

    void addEyesThrown();
    int getEyesThrown();
    void setEyesThrown(int amount);

    void addPortals();
    int getPortals();
    void setPortals(int amount);

    List<Integer> getObsidianPity();
    void setObsidianPity(List<Integer> pity);

    List<Integer> getPearlPity();
    void setPearlPity(List<Integer> pity);
}
