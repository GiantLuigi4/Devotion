package dev.cammiescorner.devotion.common.screens.providers;

import dev.cammiescorner.devotion.common.screens.ResearchMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ResearchMenuProvider implements MenuProvider {
	private final ItemStack stack;

	public ResearchMenuProvider(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public Component getDisplayName() {
		return Component.empty();
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new ResearchMenu(containerId, playerInventory, stack);
	}
}
