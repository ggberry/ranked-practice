package me.gege.mixin.rng;

import me.gege.data.PlayerEntityData;
import me.gege.util.GeneralUtil;
import me.gege.util.RNGUtil;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static me.gege.util.SeedUtil.isPracticing;

/**
 * Guarantees Ender Pearl and Obsidian trades from Piglins based on the MCSR Ranked trade pity system
 */

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    @Inject(at = @At("TAIL"), method = "getBarteredItem", cancellable = true)
    private static void getBarteredItem(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (!isPracticing) {
            return;
        }

        PlayerEntityData playerData = GeneralUtil.getPlayerData(piglin.world);
        if (playerData == null) {
            return;
        }

        playerData.addBarteredGold();
        if (playerData.getBarteredGold() == 73) {
            playerData.setGoldBartered(0);
        }

        RNGUtil.doBarterPity(playerData, piglin.getRandom(), cir);
    }
}
