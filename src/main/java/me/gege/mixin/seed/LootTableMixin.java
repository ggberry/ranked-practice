package me.gege.mixin.seed;

import me.gege.seed.RuinedPortalHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.gege.util.SeedUtil.isPracticing;
import static me.gege.util.SeedUtil.seedType;

/**
 * Artificially boosts loot in Ruined Portal seed types
 */

@Mixin(LootTable.class)
public abstract class LootTableMixin {
    @Inject(at = @At("HEAD"), method = "supplyInventory", cancellable = true)
    private void supplyInventory(Inventory inventory, LootContext context, CallbackInfo ci) {
        if (!isPracticing) {
            return;
        }

        if (seedType == 4 || seedType == 5) {
            RuinedPortalHelper.supplyBoostedInventory(inventory, context, seedType == 5, ci);
        }
    }
}
