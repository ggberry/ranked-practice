package me.gege.seed;

import net.minecraft.client.MinecraftClient;
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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.RuinedPortalFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import static me.gege.util.SeedUtil.sourcePos;

/**
 * Helper methods for Ruined Portal seed type generation.
 * - supplyBoostedInventory: Adds additional loot to ruined portals to make them playable
 * - getPortalStart: Returns StructureStart (helper) object of the ruined portal structure
 */

public class RuinedPortalHelper {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private static final HashMap<String, Integer> portalObsidian;
    private static final StructureFeature<RuinedPortalFeatureConfig> feature = StructureFeature.RUINED_PORTAL;
    private static final List<ConfiguredStructureFeature<?, ?>> configs;

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

        StructureStart<?> structureStart = getPortalStart(seed, sourcePos, (Biome) biomeSource.getBiomesInArea(sourcePos.x << 4, 63, sourcePos.z << 4, 1).toArray()[0]);
        List<ItemStack> bonusItems = new ArrayList<>();

        if (structureStart == null || chestPos.isWithinDistance(structureStart.getPos(), 20)) {
            return;
        }

        Random random = new Random();
        if (!bucket || random.nextInt(4) == 0) {
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

    public static StructureStart<?> getPortalStart(long seed, ChunkPos startPos, Biome biome) {
        if (client.getServer() == null) {
            return null;
        }

        Properties customProperties = new Properties();
        customProperties.put("generator-settings", "");
        customProperties.put("level-seed", Long.toString(seed));
        customProperties.put("generate-structures", Boolean.toString(true));
        customProperties.put("level-type", "default");

        GeneratorOptions generatorOptions = GeneratorOptions.fromProperties(customProperties);
        ChunkGenerator chunkGenerator = generatorOptions.getChunkGenerator();

        ConfiguredStructureFeature<?, ?> config;
        if (biome == Biomes.SWAMP) {
            config = configs.get(0);
        } else if (biome == Biomes.JUNGLE) {
            config = configs.get(1);
        } else if (biome == Biomes.DESERT) {
            config = configs.get(2);
        } else if (biome == Biomes.MOUNTAINS) {
            config = configs.get(3);
        } else {
            config = configs.get(4);
        }

        StructureStart<?> start = config.method_28622(
                chunkGenerator,
                chunkGenerator.getBiomeSource(),
                client.getServer().getStructureManager(),
                seed,
                startPos,
                biome,
                0,
                StructuresConfig.DEFAULT_STRUCTURES.get(feature)
        );

        if (start.hasChildren()) {
            return start;
        }

        return null;
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

    static {
        configs = Arrays.asList(
                DefaultBiomeFeatures.SWAMP_RUINED_PORTAL,
                DefaultBiomeFeatures.JUNGLE_RUINED_PORTAL,
                DefaultBiomeFeatures.DESERT_RUINED_PORTAL,
                DefaultBiomeFeatures.MOUNTAIN_RUINED_PORTAL,
                DefaultBiomeFeatures.STANDARD_RUINED_PORTAL
        );

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
