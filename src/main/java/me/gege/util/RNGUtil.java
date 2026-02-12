package me.gege.util;

import me.gege.data.PlayerEntityData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static me.gege.util.SeedUtil.isPracticing;
import static net.minecraft.entity.mob.MobEntity.canMobSpawn;

public class RNGUtil {
    public static void doBarterPity(PlayerEntityData playerData, Random random, CallbackInfoReturnable<List<ItemStack>> cir) {
        int goldBartered = playerData.getBarteredGold();

        if (playerData.getObsidianPity().contains(goldBartered)) {
            cir.setReturnValue(Collections.singletonList(new ItemStack(Items.OBSIDIAN)));
        } else if (playerData.getPearlPity().contains(goldBartered)) {
            int count = random.nextInt(5) + 4;
            cir.setReturnValue(new ArrayList<>(Collections.nCopies(count, new ItemStack(Items.ENDER_PEARL))));
        }
    }

    public static void golemLoot(LivingEntity entity, CallbackInfo ci) {
        if (!(entity.getType() == EntityType.IRON_GOLEM)) {
            return;
        }

        entity.dropStack(new ItemStack(Items.IRON_INGOT, 4));

        ci.cancel();
    }

    public static boolean shouldEyeBreak(World world) {
        PlayerEntityData playerData = GeneralUtil.getPlayerData(world);
        if (playerData == null) {
            return false;
        }

        playerData.addEyesThrown();

        return playerData.getEyesThrown() != 2;
    }

    public static void betterPortalCords(World world, Entity entity, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (!isPracticing) {
            return;
        }

        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntityData data = (PlayerEntityData) entity;
        data.addPortals();
        int portals = data.getPortals();

        if ((portals == 2 && entity.getBlockPos().getY() < 48) || portals > 2) {
            return;
        }

        int targetY = -67;

        double d = -1.0;
        int j = MathHelper.floor(entity.getX());
        int k = MathHelper.floor(entity.getY());
        int l = MathHelper.floor(entity.getZ());
        int m = j;
        int n = k;
        int o = l;
        int p = 0;
        int q = random.nextInt(4);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int r = j - 16; r <= j + 16; r++) {
            double e = r + 0.5 - entity.getX();

            for (int s = l - 16; s <= l + 16; s++) {
                double f = s + 0.5 - entity.getZ();

                label279:
                for (int t = world.getDimensionHeight() - 1; t >= 0; t--) {
                    if (world.isAir(mutable.set(r, t, s))) {
                        while (t > 0 && world.isAir(mutable.set(r, t - 1, s))) {
                            t--;
                        }

                        for (int u = q; u < q + 4; u++) {
                            int v = u % 2;
                            int w = 1 - v;
                            if (u % 4 >= 2) {
                                v = -v;
                                w = -w;
                            }

                            for (int x = 0; x < 3; x++) {
                                for (int y = 0; y < 4; y++) {
                                    for (int z = -1; z < 4; z++) {
                                        int aa = r + (y - 1) * v + x * w;
                                        int ab = t + z;
                                        int ac = s + (y - 1) * w - x * v;
                                        mutable.set(aa, ab, ac);
                                        if (z < 0 && !world.getBlockState(mutable).getMaterial().isSolid() || z >= 0 && !world.isAir(mutable)) {
                                            continue label279;
                                        }
                                    }
                                }
                            }

                            double g = t + 0.5 - entity.getY();
                            double h = e * e + g * g + f * f;
                            if (d < 0.0 || h < d) {
                                d = h;
                                m = r;
                                n = t;
                                o = s;
                                p = u % 4;
                            }
                        }
                    }
                }
            }
        }

        if (d < 0.0) {
            for (int r = j - 16; r <= j + 16; r++) {
                double e = r + 0.5 - entity.getX();

                for (int s = l - 16; s <= l + 16; s++) {
                    double f = s + 0.5 - entity.getZ();

                    label216:
                    for (int tx = world.getDimensionHeight() - 1; tx >= 0; tx--) {
                        if (world.isAir(mutable.set(r, tx, s))) {
                            while (tx > 0 && world.isAir(mutable.set(r, tx - 1, s))) {
                                tx--;
                            }

                            for (int u = q; u < q + 2; u++) {
                                int vx = u % 2;
                                int wx = 1 - vx;

                                for (int x = 0; x < 4; x++) {
                                    for (int y = -1; y < 4; y++) {
                                        int zx = r + (x - 1) * vx;
                                        int aa = tx + y;
                                        int ab = s + (x - 1) * wx;
                                        mutable.set(zx, aa, ab);
                                        if (y < 0 && !world.getBlockState(mutable).getMaterial().isSolid() || y >= 0 && !world.isAir(mutable)) {
                                            continue label216;
                                        }
                                    }
                                }

                                double g = tx + 0.5 - entity.getY();
                                double h = e * e + g * g + f * f;
                                if (d < 0.0 || h < d) {
                                    d = h;
                                    m = r;
                                    n = tx;
                                    o = s;
                                    p = u % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int ad = m;
        int ae = n;
        int s = o;

        int af = p % 2;
        int ag = 1 - af;
        if (p % 4 >= 2) {
            af = -af;
            ag = -ag;
        }

        if (d < 0.0) {
            n = MathHelper.clamp(n, 70, world.getDimensionHeight() - 10);
            ae = n;

            for (int txx = -1; txx <= 1; txx++) {
                for (int u = 1; u < 3; u++) {
                    for (int vx = -1; vx < 3; vx++) {
                        int wx = ad + (u - 1) * af + txx * ag;
                        int yx = s + (u - 1) * ag - txx * af;
                        boolean bl = vx < 0;
                        int x = ae + vx;

                        mutable.set(wx, x, yx);

                        world.setBlockState(mutable, bl ? Blocks.OBSIDIAN.getDefaultState() : Blocks.AIR.getDefaultState());
                    }
                }
            }
        }

        for (int txx = -1; txx < 3; txx++) {
            for (int u = -1; u < 4; u++) {
                if (txx == -1 || txx == 2 || u == -1 || u == 3) {
                    targetY = ae;
                    int x = ad + txx * af;
                    int z = s + txx * ag;

                    if (targetY == -67 && !isSurfacePortal(world, new BlockPos(x, ae, z), ae)) {
                        targetY = findPortalY(world, new BlockPos(ad + txx * af, n, s + txx * ag));
                    }

                    mutable.set(x, targetY + u, z);
                    world.setBlockState(mutable, Blocks.OBSIDIAN.getDefaultState(), 3);
                }
            }
        }

        BlockState blockState = Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, af == 0 ? Direction.Axis.Z : Direction.Axis.X);

        for (int ux = 0; ux < 2; ux++) {
            for (int vx = 0; vx < 3; vx++) {
                mutable.set(ad + ux * af, targetY + vx, s + ux * ag);
                world.setBlockState(mutable, blockState, 18);
            }
        }

        cir.setReturnValue(true);
    }

    private static int findPortalY(World world, BlockPos blockPos) {
        int y = blockPos.getY();

        while (y < 256) {
            if (isSurfacePortal(world, blockPos, y)) {
                return y;
            }

            y++;
        }

        return y;
    }

    private static boolean isSurfacePortal(World world, BlockPos blockPos, int blockY) {
        BlockPos surface = world.getTopPosition(
                Heightmap.Type.WORLD_SURFACE,
                blockPos
        );

        int topY = surface.getY();

        for (int y = blockY; y < 256; y++) {
            Block block = world.getBlockState(new BlockPos(blockPos.getX(), y, blockPos.getZ())).getBlock();

            if (block != Blocks.AIR && block != Blocks.OBSIDIAN && block != Blocks.NETHER_PORTAL && y < topY) {
                return false;
            }
        }

        return true;
    }

    public static void noTridentDrowned(DrownedEntity drowned, Random random, CallbackInfo ci) {
        if (!isPracticing) {
            return;
        }

        if (random.nextFloat() > 0.9) {
            int i = random.nextInt(16);
            if (i >= 10) {
                drowned.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
        }

        ci.cancel();
    }

    public static void noMiningFatigue(CallbackInfo ci) {
        if (!isPracticing) {
            return;
        }

        ci.cancel();
    }

    public static void ghastSpawnControl(EntityType<GhastEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (!isPracticing) {
            return;
        }

        PlayerEntity player = world.getPlayers().get(0);
        if (player == null) {
            return;
        }

        double MIN_DISTANCE_SQUARED = Math.pow((16 * 5), 2);

        boolean defaultCondition = world.getDifficulty() != Difficulty.PEACEFUL && random.nextInt(20) == 0 && canMobSpawn(type, world, spawnReason, pos, random);
        boolean proximityCondition = pos.getSquaredDistance(player.getBlockPos()) > MIN_DISTANCE_SQUARED;

        cir.setReturnValue(proximityCondition && defaultCondition);
    }

    public static void noStructureSpawns(ServerWorld world, MobEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!isPracticing) {
            return;
        }

        if (!(entity instanceof HostileEntity)) {
            return;
        }

        BlockPos pos = entity.getBlockPos();
        StructureAccessor structureAccessor = world.getStructureAccessor();

        StructureStart<?> bastion = structureAccessor.method_28388(
                pos,
                true,
                StructureFeature.BASTION_REMNANT
        );

        StructureStart<?> dTemple = structureAccessor.method_28388(
                pos,
                true,
                StructureFeature.DESERT_PYRAMID
        );

        if ((bastion.hasChildren() && bastion.getBoundingBox().contains(pos)) || (dTemple.hasChildren() && dTemple.getBoundingBox().contains(pos))) {
            cir.setReturnValue(false);
        }
    }

//    public static void lowerFlyChance(double x, double y, double z, EnderDragonFight fight, PathNode[] pathNodes, CallbackInfoReturnable<Integer> cir) {
//        float f = 10000.0F;
//        int i = 0;
//        PathNode pathNode = new PathNode(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
//        int j = 0;
//        if (fight == null || fight.getAliveEndCrystals() == 0) {
//            j = 12;
//        }
//
//        for (int k = j; k < 20; k++) {
//            if (pathNodes[k] != null) {
//                float g = pathNodes[k].getSquaredDistance(pathNode);
//                if (g < f) {
//                    f = g;
//                    i = k;
//                }
//            }
//        }
//
//        cir.setReturnValue(i);
//    }
}
