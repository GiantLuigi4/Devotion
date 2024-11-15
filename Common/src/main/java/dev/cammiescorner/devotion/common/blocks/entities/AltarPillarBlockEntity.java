package dev.cammiescorner.devotion.common.blocks.entities;

import dev.cammiescorner.devotion.common.registries.DevotionBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AltarPillarBlockEntity extends BlockEntity {
	public AltarPillarBlockEntity(BlockPos pos, BlockState blockState) {
		super(DevotionBlocks.ALTAR_PILLAR_ENTITY.get(), pos, blockState);
	}
}
