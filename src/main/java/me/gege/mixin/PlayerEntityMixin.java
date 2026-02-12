package me.gege.mixin;

import me.gege.RankedPractice;
import me.gege.data.PlayerEntityData;
import me.gege.util.Configs;
import me.gege.util.GeneralUtil;
import me.gege.util.SeedUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity implements PlayerEntityData {
    private int goldBartered;
    private int eyesThrown;
    private int portals;
    private List<Integer> pityList = GeneralUtil.generatePityList();
    private List<Integer> obsidianPity = new ArrayList<>(pityList.subList(0, Configs.OBSIDIAN_PITY));
    private List<Integer> pearlPity = new ArrayList<>(pityList.subList(Configs.OBSIDIAN_PITY, pityList.size()));

    private static final String goldBarteredKey;
    private static final String eyesThrownKey;
    private static final String portalsKey;
    private static final String obsidianPityKey;
    private static final String pearlPityKey;

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    public void addBarteredGold() {
        goldBartered += 1;
    }

    public void setGoldBartered(int amount) {
        goldBartered = amount;
    }

    public int getBarteredGold() {
        return goldBartered;
    }

    public int getEyesThrown() {
        return eyesThrown;
    }

    public void addEyesThrown() {
        eyesThrown++;
    }

    public void setEyesThrown(int amount) {
        eyesThrown = amount;
    }

    public int getPortals() {
        return portals;
    }

    public void addPortals() {
        portals++;
    }

    public void setPortals(int amount) {
        portals = amount;
    }

    public List<Integer> getObsidianPity() {
        return obsidianPity;
    }

    public void setObsidianPity(List<Integer> pity) {
        obsidianPity = pity;
    }

    public List<Integer> getPearlPity() {
        return pearlPity;
    }

    public void setPearlPity(List<Integer> pity) {
        pearlPity = pity;
    }
    @Inject(at = @At("RETURN"), method = "writeCustomDataToTag")
    private void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        tag.putInt(goldBarteredKey, this.goldBartered);
        tag.putInt(eyesThrownKey, this.eyesThrown);
        tag.putInt(portalsKey, this.portals);
        tag.putIntArray(obsidianPityKey, this.obsidianPity);
        tag.putIntArray(pearlPityKey, this.pearlPity);
    }

    @Inject(at = @At("RETURN"), method = "readCustomDataFromTag")
    private void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
        goldBartered = tag.getInt(goldBarteredKey);
        eyesThrown = tag.getInt(eyesThrownKey);
        portals = tag.getInt(portalsKey);
        obsidianPity = GeneralUtil.listFromArray(tag.getIntArray(obsidianPityKey));
        pearlPity = GeneralUtil.listFromArray(tag.getIntArray(pearlPityKey));
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci) {
        if (this.world.isClient || this.getServer() == null) {
            return;
        }

        if (SeedUtil.netherCooldown > 0) {
            SeedUtil.netherCooldown--;
        }
    }

    static {
        goldBarteredKey = RankedPractice.MOD_ID + ".goldBartered";
        eyesThrownKey = RankedPractice.MOD_ID + ".eyesThrown";
        portalsKey = RankedPractice.MOD_ID + ".portals";
        obsidianPityKey = RankedPractice.MOD_ID + ".obsidianPity";
        pearlPityKey = RankedPractice.MOD_ID + ".pearlPity";
    }
}
