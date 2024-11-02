package dev.cammiescorner.devotion.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AltarBlockEntity extends BlockEntity implements RecipeInput {
	public AltarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public ItemStack getItem(int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public int size() {
		return 0;
	}
}
