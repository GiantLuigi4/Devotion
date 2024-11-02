package dev.cammiescorner.devotion;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import commonnetwork.api.Network;
import dev.cammiescorner.devotion.api.Graph;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.networking.s2c.ClientBoundAltarStructurePacket;
import dev.cammiescorner.devotion.common.networking.s2c.ClientBoundDataPacket;
import dev.cammiescorner.devotion.common.registries.*;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Devotion implements MainEntryPoint {
	public static final String MOD_ID = "devotion";
	public static final Logger LOGGER = LoggerFactory.getLogger("Devotion");
	public static final Configurator CONFIGURATOR = new Configurator(MOD_ID);

	public static final BiMap<ResourceLocation, Research> RESEARCH = HashBiMap.create();

	public static final Graph<AuraType> AURA_GRAPH = Util.make(new Graph<>(), (graph) -> {
		for(int i = 0; i < AuraType.values().length - 1; i++)
			graph.addNode(new Graph.Node<>(AuraType.values()[i]));

		for(int i = 0; i < graph.nodes.size(); i++) {
			Graph.Node<AuraType> node = graph.nodes.get(i);

			for(int j = 0; j < graph.nodes.size(); j++) {
				if(i == j)
					continue; // do not loop back to self

				Graph.Node<AuraType> target = graph.nodes.get(j);
				graph.addEdge(node, target);
			}
		}
	});

	@Override
	public void onInitialize(ModContainer mod) {
		CONFIGURATOR.register(DevotionConfig.class);
		RegistryService registryService = RegistryService.get();

		DevotionItems.ITEMS.accept(registryService);
		DevotionMaterials.ARMOR_MATERIALS.accept(registryService);
		DevotionAttributes.ATTRIBUTES.accept(registryService);
		DevotionData.DATA_COMPONENTS.accept(registryService);
		DevotionRecipes.RECIPE_SERIALIZERS.accept(registryService);
		DevotionRecipes.RECIPE_TYPES.accept(registryService);
		Network.registerPacket(ClientBoundAltarStructurePacket.TYPE, ClientBoundAltarStructurePacket.class, ClientBoundAltarStructurePacket.CODEC, ClientBoundAltarStructurePacket::handle);
		Network.registerPacket(ClientBoundDataPacket.TYPE, ClientBoundDataPacket.class, ClientBoundDataPacket.CODEC, ClientBoundDataPacket::handle);
	}

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}
