package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.upcraft.sparkweave.api.item.CreativeTabHelper;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class DevotionCreativeTabs {
	public static final RegistryHandler<CreativeModeTab> CREATIVE_TABS = RegistryHandler.create(Registries.CREATIVE_MODE_TAB, Devotion.MOD_ID);

	public static final RegistrySupplier<CreativeModeTab> DEVOTION_TAB = CREATIVE_TABS.register("main_tab", () -> CreativeTabHelper.newBuilder(Devotion.id("main_tab")).icon(() -> new ItemStack(DevotionItems.ALTAR_FOCUS.get())).displayItems((parameters, output) -> CreativeTabHelper.addRegistryEntries(parameters, output, DevotionItems.ITEMS)).build());
}
