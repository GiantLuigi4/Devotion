package dev.cammiescorner.devotion.common.screens;

import dev.cammiescorner.devotion.common.registries.DevotionMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class ResearchMenu extends AbstractContainerMenu {
	private final ContainerLevelAccess access;
	private final Inventory inventory;
	private ItemStack stack;

	public ResearchMenu(int containerId, Inventory inventory, ItemStack stack) {
		this(containerId, inventory, ContainerLevelAccess.NULL, stack);
	}

	public ResearchMenu(int containerId, Inventory inventory, ContainerLevelAccess access, ItemStack stack) {
		super(DevotionMenus.RESEARCH.get(), containerId);
		this.access = access;
		this.inventory = inventory;
		this.stack = stack;
	}

	@Override
	public boolean clickMenuButton(Player player, int id) {
		if(!player.mayBuild())
			return false;

		ItemStack stack = inventory.removeItemNoUpdate(0);
		inventory.setChanged();

		if(!player.getInventory().add(stack))
			player.drop(stack, false);

		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	public ItemStack getScroll() {
		return stack;
	}

	public void setScroll(ItemStack stack) {
		this.stack = stack;
	}

	public ContainerLevelAccess getLevelAccess() {
		return access;
	}
}
