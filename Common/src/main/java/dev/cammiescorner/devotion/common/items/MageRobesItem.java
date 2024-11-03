package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.registries.DevotionMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

import java.util.List;

public class MageRobesItem extends ArmorItem {
	private final AuraType primaryAuraType;
	private final List<AuraType> secondaryAuraTypes;

	public MageRobesItem(Holder<ArmorMaterial> material, Type type, Properties properties, AuraType primaryAuraType, AuraType... secondaryAuraTypes) {
		super(material, type, properties.stacksTo(1));
		this.primaryAuraType = primaryAuraType;
		this.secondaryAuraTypes = List.of(secondaryAuraTypes);
	}

	public MageRobesItem(Type type, Properties properties, AuraType primaryAuraType, AuraType... secondaryAuraTypes) {
		this(DevotionMaterials.MAGE_ROBE_MATERIAL.holder(), type, properties, primaryAuraType, secondaryAuraTypes);
	}

	public MageRobesItem(Type type, Properties properties) {
		this(type, properties, AuraType.NONE);
	}

	public AuraType getPrimaryAuraType() {
		return primaryAuraType;
	}

	public List<AuraType> getSecondaryAuraTypes() {
		return secondaryAuraTypes;
	}
}
