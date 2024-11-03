package dev.cammiescorner.devotion.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.cammiescorner.devotion.common.blocks.entities.AltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends BaseEntityBlock {
	public AltarBlock(Properties properties) {
		super(Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).requiresCorrectToolForDrops().noOcclusion());
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return null;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AltarBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> altar) {
		// TODO register AltarBlockEntity type
		return level.isClientSide() ? null : createTickerHelper(altar, null, AltarBlockEntity::tick);
	}
}
