package me.gege.seed.util;

import me.gege.seed.filter.RuinedPortalSeedFilter;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.gege.util.SeedUtil.sourcePos;

public class RuinedPortalHelper {
    public static void supplyBoostedInventory(Inventory inventory, LootContext context, boolean bucket, CallbackInfo ci) {
        ServerWorld world = context.getWorld();
        MinecraftServer server = world.getServer();

        LootTable lootTable = server.getLootManager().getTable(LootTables.RUINED_PORTAL_CHEST);
        long seed = world.getSeed();
        BiomeSource biomeSource = new VanillaLayeredBiomeSource(seed, false, false);
        BlockPos chestPos = context.get(LootContextParameters.POSITION);

        if (world.getDimension() != DimensionType.OVERWORLD || chestPos == null) {
            return;
        }

        StructureStart<?> structureStart = RuinedPortalSeedFilter.getPortalStart(seed, sourcePos, (Biome) biomeSource.getBiomesInArea(sourcePos.x << 4, 63, sourcePos.z << 4, 1).toArray()[0]);
        List<ItemStack> bonusItems = new ArrayList<>();

        if (structureStart == null || chestPos.isWithinDistance(structureStart.getPos(), 20)) {
            return;
        }

        Random random = new Random();
        if (!bucket || random.nextInt(4) == 0) {
            bonusItems.add(new ItemStack(Items.OBSIDIAN, RuinedPortalSeedFilter.getObsidianNeeded(structureStart)));
        } else {
            bonusItems.add(new ItemStack(Items.IRON_NUGGET, 27));
        }

        int bonusIron = getBonusIron(random);

        bonusItems.add(new ItemStack(Items.IRON_NUGGET, 18 + bonusIron));

        int lightInt = random.nextInt(2);
        int fireChargeInt = random.nextInt(3);
        int swordInt = random.nextInt(100);
        ItemStack swordStack = new ItemStack(Items.GOLDEN_SWORD);

        if (lightInt == 0) {
            bonusItems.add(new ItemStack(Items.FLINT, 1));
        } else {
            bonusItems.add(new ItemStack(Items.FLINT_AND_STEEL, 1));
        }

        if (fireChargeInt == 0) {
            bonusItems.add(new ItemStack(Items.FIRE_CHARGE, 1));
        }

        if (swordInt > 95) {
            swordStack.addEnchantment(Enchantments.LOOTING, random.nextInt(3) + 1);
        } else if (swordInt > 85) {
            swordStack.addEnchantment(Enchantments.FIRE_ASPECT, random.nextInt(2) + 1);
        } else {
            swordStack = null;
        }

        if (swordStack != null) {
            bonusItems.add(swordStack);
        }

        List<ItemStack> rawList = lootTable.generateLoot(context);
        List<ItemStack> list = new ArrayList<>();

        for (ItemStack stack: rawList) {
            if (stack.getItem() != Items.IRON_NUGGET) {
                list.add(stack);
            }
        }

        list.addAll(bonusItems);

        List<Integer> list2 = lootTable.getFreeSlots(inventory, random);
        lootTable.shuffle(list, list2.size(), random);

        for (ItemStack itemStack : list) {
            if (itemStack.isEmpty()) {
                inventory.setStack(list2.remove(list2.size() - 1), ItemStack.EMPTY);
            } else {
                inventory.setStack(list2.remove(list2.size() - 1), itemStack);
            }
        }

        ci.cancel();
    }

    private static int getBonusIron(Random random) {
        float bracket = random.nextFloat();
        int bonus;

        if (bracket < 0.5f) {
            bonus = random.nextInt(5);
        } else if (bracket < 0.65f) {
            bonus = random.nextInt(5) + 5;
        } else if (bracket < 0.85f) {
            bonus = random.nextInt(10) + 5;
        } else {
            bonus = random.nextInt(15) + 15;
        }

        return bonus;
    }
}
