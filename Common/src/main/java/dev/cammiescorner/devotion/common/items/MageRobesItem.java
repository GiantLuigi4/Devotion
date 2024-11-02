package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.common.registries.DevotionMaterials;
import net.minecraft.world.item.ArmorItem;

public class MageRobesItem extends ArmorItem {
	public MageRobesItem(Type type, Properties properties) {
		super(DevotionMaterials.MAGE_ROBE_MATERIAL.holder(), type, properties.stacksTo(1));
	}
}
