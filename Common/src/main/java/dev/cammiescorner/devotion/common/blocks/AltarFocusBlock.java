package dev.cammiescorner.devotion.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.cammiescorner.devotion.common.blocks.entities.AltarFocusBlockEntity;
import dev.cammiescorner.devotion.common.blocks.properties.RotationProperty;
import dev.cammiescorner.devotion.common.registries.DevotionBlockStateProperties;
import dev.cammiescorner.devotion.common.registries.DevotionBlocks;
import dev.upcraft.sparkweave.api.registry.block.BlockItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class AltarFocusBlock extends BaseEntityBlock implements BlockItemProvider {
	public static final RotationProperty ROTATION = DevotionBlockStateProperties.ROTATION;

	public AltarFocusBlock(Properties properties) {
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(ROTATION, Rotation.NONE));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Rotation rotation = switch(context.getHorizontalDirection()) {
			case NORTH -> Rotation.NONE;
			case EAST -> Rotation.CLOCKWISE_90;
			case WEST -> Rotation.COUNTERCLOCKWISE_90;
			case SOUTH -> Rotation.CLOCKWISE_180;
			default -> Rotation.NONE;
		};

		return this.defaultBlockState().setValue(ROTATION, rotation);
	}

	@Override
	protected BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(ROTATION, rotation.getRotated(state.getValue(ROTATION)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(state.getValue(ROTATION));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ROTATION);
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
		return level.isClientSide() ? null : createTickerHelper(altar, DevotionBlocks.ALTAR_FOCUS_ENTITY.get(), AltarFocusBlockEntity::tick);
	}
}
