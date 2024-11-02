package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;

public class DevotionMaterials {
	public static final RegistryHandler<ArmorMaterial> ARMOR_MATERIALS = RegistryHandler.create(Registries.ARMOR_MATERIAL, Devotion.MOD_ID);

	public static final RegistrySupplier<ArmorMaterial> MAGE_ROBE_MATERIAL = ARMOR_MATERIALS.register("mage_robes", () -> new ArmorMaterial(
		Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
			enumMap.put(ArmorItem.Type.BOOTS, 1);
			enumMap.put(ArmorItem.Type.LEGGINGS, 2);
			enumMap.put(ArmorItem.Type.CHESTPLATE, 3);
			enumMap.put(ArmorItem.Type.HELMET, 1);
			enumMap.put(ArmorItem.Type.BODY, 3);
		}),
		15,
		SoundEvents.ARMOR_EQUIP_LEATHER,
		() -> Ingredient.of(Items.LEATHER),
		List.of(new ArmorMaterial.Layer(Devotion.id("mage_robes"))),
		0f,
		0f
	));
}
