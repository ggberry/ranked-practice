package me.gege.mixin.seed;

import me.gege.util.SeedUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockAgeStructureProcessor;
import net.minecraft.tag.BlockTags;
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

/**
 * Prevents Crying Obsidian obstruction on Normal Ruined Portal seed types
 */

@Mixin(BlockAgeStructureProcessor.class)
public abstract class BlockAgeStructureProcessorMixin {
    @Shadow @Nullable protected abstract BlockState processBlocks(Random random);

    @Shadow @Nullable protected abstract BlockState processStairs(Random random, BlockState state);

    @Shadow @Nullable protected abstract BlockState processSlabs(Random random);

    @Shadow @Nullable protected abstract BlockState processWalls(Random random);

    @Shadow @Nullable protected abstract BlockState processObsidian(Random random);

    @Inject(at = @At("HEAD"), method = "process(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/structure/Structure$StructureBlockInfo;Lnet/minecraft/structure/Structure$StructureBlockInfo;Lnet/minecraft/structure/StructurePlacementData;)Lnet/minecraft/structure/Structure$StructureBlockInfo;", cancellable = true)
    private void process(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData structurePlacementData, CallbackInfoReturnable<Structure.StructureBlockInfo> cir) {
        if (!isPracticing) {
            return;
        }

        Random random = structurePlacementData.getRandom(structureBlockInfo2.pos);
        BlockState blockState = structureBlockInfo2.state;
        BlockPos blockPos2 = structureBlockInfo2.pos;
        BlockState blockState2 = null;

        if (blockState.isOf(Blocks.STONE_BRICKS) || blockState.isOf(Blocks.STONE) || blockState.isOf(Blocks.CHISELED_STONE_BRICKS)) {
            blockState2 = this.processBlocks(random);
        } else if (blockState.isIn(BlockTags.STAIRS)) {
            blockState2 = this.processStairs(random, structureBlockInfo2.state);
        } else if (blockState.isIn(BlockTags.SLABS)) {
            blockState2 = this.processSlabs(random);
        } else if (blockState.isIn(BlockTags.WALLS)) {
            blockState2 = this.processWalls(random);
        } else if (blockState.isOf(Blocks.OBSIDIAN) && !(worldView.getDimension() == DimensionType.OVERWORLD && (SeedUtil.seedType == 4))) {
            blockState2 = this.processObsidian(random);
        }

        cir.setReturnValue(blockState2 != null ? new Structure.StructureBlockInfo(blockPos2, blockState2, structureBlockInfo2.tag) : structureBlockInfo2);
    }
}
