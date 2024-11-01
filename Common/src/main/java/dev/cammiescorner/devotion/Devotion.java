package dev.cammiescorner.devotion;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import commonnetwork.api.Network;
import dev.cammiescorner.devotion.common.networking.s2c.ClientBoundAltarStructurePacket;
import dev.cammiescorner.devotion.common.networking.s2c.ClientBoundDataPacket;
import dev.cammiescorner.devotion.common.registries.DevotionAttributes;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import net.minecraft.resources.ResourceLocation;

public class Devotion implements MainEntryPoint {
	public static final String MOD_ID = "devotion";
	public static final Configurator CONFIGURATOR = new Configurator(MOD_ID);

	@Override
	public void onInitialize(ModContainer mod) {
		CONFIGURATOR.register(DevotionConfig.class);
		RegistryService registryService = RegistryService.get();

		DevotionAttributes.ATTRIBUTES.accept(registryService);
		Network.registerPacket(ClientBoundAltarStructurePacket.TYPE, ClientBoundAltarStructurePacket.class, ClientBoundAltarStructurePacket.CODEC, ClientBoundAltarStructurePacket::handle);
		Network.registerPacket(ClientBoundDataPacket.TYPE, ClientBoundDataPacket.class, ClientBoundDataPacket.CODEC, ClientBoundDataPacket::handle);
	}

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}
