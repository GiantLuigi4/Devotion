package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class DevotionAttributes {
	public static final RegistryHandler<Attribute> ATTRIBUTES = RegistryHandler.create(Registries.ATTRIBUTE, Devotion.MOD_ID);
}
