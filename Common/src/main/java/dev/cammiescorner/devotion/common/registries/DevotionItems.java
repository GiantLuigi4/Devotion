package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.items.MageRobesItem;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

public class DevotionItems {
	public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(Registries.ITEM, Devotion.MOD_ID);

	public static final RegistrySupplier<Item> BASIC_MAGE_HOOD = ITEMS.register("basic_mage_hood", () -> new MageRobesItem(ArmorItem.Type.HELMET, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false)));
	public static final RegistrySupplier<Item> BASIC_MAGE_ROBE = ITEMS.register("basic_mage_robe", () -> new MageRobesItem(ArmorItem.Type.CHESTPLATE, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false)));
	public static final RegistrySupplier<Item> BASIC_MAGE_BELT = ITEMS.register("basic_mage_belt", () -> new MageRobesItem(ArmorItem.Type.LEGGINGS, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false)));
	public static final RegistrySupplier<Item> BASIC_MAGE_SHOES = ITEMS.register("basic_mage_shoes", () -> new MageRobesItem(ArmorItem.Type.BOOTS, new Item.Properties().component(DevotionData.CLOSED_HOOD.get(), false)));
}
