package dev.cammiescorner.devotion.common.screens.providers;

import commonnetwork.api.Network;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundRefreshResearchScreenPacket;
import dev.cammiescorner.devotion.common.screens.ResearchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ResearchMenuProvider implements MenuProvider {
	private final Level level;
	private final ItemStack stack;
	private final BlockPos pos;
	private final Container container;

	public ResearchMenuProvider(Level level, ItemStack stack, BlockPos pos, Container container) {
		this.level = level;
		this.stack = stack;
		this.pos = pos;
		this.container = container;
	}

	@Override
	public Component getDisplayName() {
		return Component.empty();
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
		if(player instanceof ServerPlayer serverPlayer)
			Network.getNetworkHandler().sendToClient(new ClientboundRefreshResearchScreenPacket(stack), serverPlayer);

		return new ResearchMenu(containerId, container, ContainerLevelAccess.create(level, pos));
	}
}
