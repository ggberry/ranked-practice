package me.gege.mixin.seed;

import me.gege.RankedPractice;
import me.gege.util.SeedUtil;
import net.minecraft.world.gen.ChunkRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static me.gege.util.SeedUtil.isPracticing;
import static me.gege.util.SeedUtil.netherSeed;

/**
 * Allows nether seed to be separated from overworld seed
 */

@Mixin(ChunkRandom.class)
public abstract class ChunkRandomMixin extends Random {
    @Inject(at = @At("HEAD"), method = "setPopulationSeed", cancellable = true)
    private void setPopulationSeed(long worldSeed, int blockX, int blockZ, CallbackInfoReturnable<Long> cir) {
        if (RankedPractice.DEVELOPING) {
            return;
        }

        if (!isPracticing) {
            return;
        }

        if (!SeedUtil.inNether) {
            return;
        }

        worldSeed = netherSeed;

        this.setSeed(worldSeed);
        long l = this.nextLong() | 1L;
        long m = this.nextLong() | 1L;
        long n = blockX * l + blockZ * m ^ worldSeed;
        this.setSeed(n);
        cir.setReturnValue(n);
    }

    @Inject(at = @At("HEAD"), method = "setCarverSeed", cancellable = true)
    private void setCarverSeed(long worldSeed, int chunkX, int chunkZ, CallbackInfoReturnable<Long> cir) {
        if (RankedPractice.DEVELOPING) {
            return;
        }

        if (!isPracticing) {
            return;
        }

        if (!SeedUtil.inNether) {
            return;
        }

        worldSeed = netherSeed;

        this.setSeed(worldSeed);
        long l = this.nextLong();
        long m = this.nextLong();
        long n = chunkX * l ^ chunkZ * m ^ worldSeed;
        this.setSeed(n);
        cir.setReturnValue(n);
    }

    @Inject(at = @At("HEAD"), method = "setRegionSeed", cancellable = true)
    private void setRegionSeed(long worldSeed, int regionX, int regionZ, int salt, CallbackInfoReturnable<Long> cir) {
        if (RankedPractice.DEVELOPING) {
            return;
        }

        if (!isPracticing) {
            return;
        }

        if (!SeedUtil.inNether) {
            return;
        }

        worldSeed = netherSeed;

        long l = regionX * 341873128712L + regionZ * 132897987541L + worldSeed + salt;
        this.setSeed(l);
        cir.setReturnValue(l);
    }
}
