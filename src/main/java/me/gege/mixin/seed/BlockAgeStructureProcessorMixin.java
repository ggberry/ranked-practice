package me.gege.mixin.seed;

import me.gege.util.SeedUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockAgeStructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static me.gege.util.SeedUtil.isPracticing;
import static me.gege.util.SeedUtil.overworldSeed;

/**
 * Prevents Crying Obsidian obstruction on Normal Ruined Portal seed types
 */

@Mixin(BlockAgeStructureProcessor.class)
public abstract class BlockAgeStructureProcessorMixin {
    @Inject(at = @At("HEAD"), method = "process(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/Structure$StructureBlockInfo;Lnet/minecraft/structure/Structure$StructureBlockInfo;Lnet/minecraft/structure/StructurePlacementData;)Lnet/minecraft/structure/Structure$StructureBlockInfo;", cancellable = true)
    private void process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData structurePlacementData, CallbackInfoReturnable<Structure.StructureBlockInfo> cir) {
        if (!isPracticing) {
            return;
        }

        Random random = new Random(overworldSeed);
        boolean isOverworld = worldView.getDimension() == DimensionType.OVERWORLD;
        boolean canBucket = SeedUtil.seedType == 5 && random.nextInt(2) != 0; // If random.nextInt(2) == 0 -> not bucket completable (deterministic).

        // Returns if portal generates in nether, seed type isn't ruined portal or the portal is bucket completable
        if (!isOverworld || canBucket) {
            return;
        }

        BlockState blockState = structureBlockInfo2.state;

        if (!blockState.isOf(Blocks.OBSIDIAN)) {
            return;
        }

        cir.setReturnValue(structureBlockInfo2);
    }
}
