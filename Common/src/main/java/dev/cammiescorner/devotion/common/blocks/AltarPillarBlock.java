package dev.cammiescorner.devotion.common.blocks;

import com.mojang.serialization.MapCodec;
import dev.cammiescorner.devotion.common.blocks.entities.AltarPillarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class AltarPillarBlock extends BaseEntityBlock {
	public static final IntegerProperty LAYER = IntegerProperty.create("layer", 0, 2);

	public AltarPillarBlock(Properties properties) {
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(LAYER, 0));
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if(state != newState && level.getBlockEntity(pos) instanceof AltarPillarBlockEntity pillar) {
			for(BlockState storedBlock : pillar.getStoredBlocks()) {
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), storedBlock.getBlock().getCloneItemStack(level, pos, storedBlock));
			}
		}

		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		int layer = state.getValue(LAYER);
		boolean idkWhatToCallThis = switch(layer) {
			case 0 -> facing == Direction.UP;
			case 1 -> true;
			case 2 -> facing == Direction.DOWN;
			default -> false;
		};

		if(facing.getAxis() != Direction.Axis.Y || !idkWhatToCallThis)
			return super.updateShape(state, facing, neighborState, level, pos, neighborPos);
		else
			return neighborState.is(this) && (layer + facing.getStepY() == neighborState.getValue(LAYER)) ? state : Blocks.AIR.defaultBlockState();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAYER);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return simpleCodec(AltarPillarBlock::new);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return state.getValue(LAYER) == 0 ? new AltarPillarBlockEntity(pos, state) : null;
	}
}
