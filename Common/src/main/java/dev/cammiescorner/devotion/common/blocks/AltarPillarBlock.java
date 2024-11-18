package dev.cammiescorner.devotion.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.cammiescorner.devotion.common.blocks.entities.AltarPillarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AltarPillarBlock extends BaseEntityBlock {
	public AltarPillarBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(AltarPillarBlock::new);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AltarPillarBlockEntity(pos, state);
	}
}
