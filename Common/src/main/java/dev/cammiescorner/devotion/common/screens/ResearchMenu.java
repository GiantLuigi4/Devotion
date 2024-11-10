package dev.cammiescorner.devotion.common.screens;

import dev.cammiescorner.devotion.common.registries.DevotionMenus;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;

public class ResearchMenu extends AbstractContainerMenu {
	private final ContainerLevelAccess access;
	private final Container inventory;

	public ResearchMenu(int containerId, Container inventory) {
		this(containerId, inventory, ContainerLevelAccess.NULL);
	}

	public ResearchMenu(int containerId, Container inventory, ContainerLevelAccess access) {
		super(DevotionMenus.RESEARCH.get(), containerId);
		this.access = access;
		this.inventory = inventory;
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

	public ContainerLevelAccess getLevelAccess() {
		return access;
	}
}
