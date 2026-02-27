package me.gege.seed;

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
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static me.gege.util.SeedUtil.overworldSeed;
import static me.gege.util.SeedUtil.sourcePos;

/**
 * Helper methods for Ruined Portal seed type generation.
 * - supplyBoostedInventory: Adds additional loot to ruined portals to make them playable
 * - getPortalStart: Returns StructureStart (helper) object of the ruined portal structure
 */

public class RuinedPortalHelper {
    private static final HashMap<String, Integer> portalObsidian;

    public static void supplyBoostedInventory(Inventory inventory, LootContext context, boolean bucket, CallbackInfo ci) {
        ServerWorld world = context.getWorld();
        MinecraftServer server = world.getServer();

        LootTable lootTable = server.getLootManager().getTable(LootTables.RUINED_PORTAL_CHEST);
        BlockPos chestPos = context.get(LootContextParameters.POSITION);

        if (world.getDimension() != DimensionType.OVERWORLD || chestPos == null) {
            return;
        }

        StructureStart<?> structureStart = getPortalStart(server.getOverworld());
        List<ItemStack> bonusItems = new ArrayList<>();

        if (structureStart == null || chestPos.isWithinDistance(structureStart.getPos(), 20)) {
            return;
        }

        Random random = new Random(overworldSeed);
        if (!bucket || random.nextInt(2) == 0) {
            bonusItems.add(new ItemStack(Items.OBSIDIAN, getObsidianNeeded(structureStart)));
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

        if (swordInt > 98) {
            swordStack.addEnchantment(Enchantments.LOOTING, random.nextInt(3) + 1);
        } else if (swordInt > 90) {
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

    public static StructureStart<?> getPortalStart(ServerWorld serverWorld) {
        return serverWorld.getStructureAccessor().getStructureStart(ChunkSectionPos.from(sourcePos, 0), StructureFeature.RUINED_PORTAL, serverWorld.getChunk(sourcePos.getCenterBlockPos()));
    }

    public static int getObsidianNeeded(StructureStart<?> structureStart) {
        String name = structureStart.getChildren().get(0).getTag().toString();

        for (String portalName: portalObsidian.keySet()) {
            if (name.contains(portalName)) {
                return portalObsidian.get(portalName);
            }
        }

        return 0;
    }

    private static int getBonusIron(Random random) {
        float bracket = random.nextFloat();
        int bonus;

        if (bracket < 0.7f) {
            bonus = random.nextInt(5);
        } else if (bracket < 0.80f) {
            bonus = random.nextInt(5) + 5;
        } else if (bracket < 0.95f) {
            bonus = random.nextInt(10) + 5;
        } else {
            bonus = random.nextInt(15) + 15;
        }

        return bonus;
    }

    static {
        portalObsidian = new HashMap<String, Integer>() {{
            put("portal_1", 2);
            put("portal_2", 4);
            put("portal_3", 4);
            put("portal_4", 3);
            put("portal_5", 5);
            put("portal_6", 1);
            put("portal_7", 1);
            put("portal_8", 3);
            put("portal_9", 2);
            put("portal_10", 7);
        }};
    }
}
