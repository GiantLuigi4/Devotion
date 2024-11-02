package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.actions.AltarAction;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import net.minecraft.core.Registry;

public class DevotionAltarActions {
	public static final RegistryHandler<AltarAction> ACTIONS = RegistryHandler.create(AltarAction.REGISTRY_KEY, Devotion.MOD_ID);
	public static final Registry<AltarAction> REGISTRY = ACTIONS.createNewRegistry(true, Devotion.id("empty"));


}
