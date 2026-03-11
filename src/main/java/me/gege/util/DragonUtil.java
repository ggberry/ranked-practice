package me.gege.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.Phase;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static me.gege.util.SeedUtil.isPracticing;

public class DragonUtil {
    public static final int maxEndTicks;
    public static final List<PhaseType<?>> resetPhases;
    public static int endTicks = 0;
    public static List<Entity> enderDragons = new ArrayList<>();

    public static void init() {
        endTicks = 0;
        enderDragons.clear();
    }

    // Returns a lower node for the zero cycle, reducing the chance dragon flies.
    public static Vec3d lowerZeroNode(Path currentPath, EnderDragonEntity dragon) {
        Vec3i vec3i = currentPath.getCurrentPosition();
        currentPath.next();
        double d = vec3i.getX();
        double e = vec3i.getZ();
        double f;

        int newHeight = 70;

        do {
            f = newHeight + dragon.getRandom().nextFloat() * 10.0F;
        } while (f < newHeight);

        return new Vec3d(d, f, e);
    }

    public static void tickForcePerch(World world) {
        if (!isPracticing) {
            return;
        }

        if (!world.isClient && world.getRegistryKey() != World.END || enderDragons.isEmpty()) {
            return;
        }

        EnderDragonEntity dragon = (EnderDragonEntity) enderDragons.get(0);

        if (dragon == null) {
            return;
        }

        PhaseManager phaseManager = dragon.getPhaseManager();
        PhaseType<? extends Phase> phaseType = phaseManager.getCurrent().getType();

        if (endTicks >= maxEndTicks) {
            phaseManager.setPhase(PhaseType.LANDING_APPROACH);
            endTicks = 0;
        } else if (resetPhases.contains(phaseType)) {
            endTicks = 0;
        }

        endTicks++;
    }

    static {
        maxEndTicks = 3400; // 2m 50s or 170s in ticks.
        resetPhases = new ArrayList<PhaseType<?>>() {{
            add(PhaseType.LANDING_APPROACH);
            add(PhaseType.SITTING_ATTACKING);
            add(PhaseType.SITTING_FLAMING);
            add(PhaseType.SITTING_SCANNING);
        }};
    }
}
