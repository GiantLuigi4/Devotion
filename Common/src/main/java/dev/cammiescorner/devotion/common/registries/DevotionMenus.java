package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.screens.ResearchMenu;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class DevotionMenus {
	public static final RegistryHandler<MenuType<?>> MENUS = RegistryHandler.create(Registries.MENU, Devotion.MOD_ID);

	public static final RegistrySupplier<MenuType<ResearchMenu>> RESEARCH = MENUS.register("research", () -> new MenuType<>((containerId, inventory) -> new ResearchMenu(containerId, inventory, ItemStack.EMPTY), FeatureFlags.VANILLA_SET));
}
