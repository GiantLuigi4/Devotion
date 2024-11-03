package dev.cammiescorner.devotion.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.cammiescorner.devotion.common.blocks.entities.AltarFocusBlockEntity;
import dev.cammiescorner.devotion.common.registries.DevotionBlocks;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AltarFocusBlock extends BaseEntityBlock implements BlockItemProvider {
	public AltarFocusBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(AltarFocusBlock::new);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AltarFocusBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> altar) {
		// TODO register AltarBlockEntity type
		return level.isClientSide() ? null : createTickerHelper(altar, DevotionBlocks.ALTAR_ENTITY.get(), AltarFocusBlockEntity::tick);
	}
}
