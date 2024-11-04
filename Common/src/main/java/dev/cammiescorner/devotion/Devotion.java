package dev.cammiescorner.devotion;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import commonnetwork.api.Network;
import dev.cammiescorner.devotion.api.Graph;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.devotion.common.networking.c2s.ServerboundOpenCloseHoodPacket;
import dev.cammiescorner.devotion.common.networking.s2c.ClientboundAltarStructurePacket;
import dev.cammiescorner.devotion.common.networking.s2c.ClientboundDataPacket;
import dev.cammiescorner.devotion.common.registries.*;
import dev.upcraft.sparkweave.api.entrypoint.MainEntryPoint;
import dev.upcraft.sparkweave.api.event.ItemMenuInteractionEvent;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Devotion implements MainEntryPoint {
	public static final String MOD_ID = "devotion";
	public static final Logger LOGGER = LoggerFactory.getLogger("Devotion");
	public static final Configurator CONFIGURATOR = new Configurator(MOD_ID);

	public static final BiMap<ResourceLocation, Research> RESEARCH = HashBiMap.create();
	public static final List<Item> HOOD_ITEMS = List.of(
		DevotionItems.BASIC_MAGE_HOOD.get(), DevotionItems.ENHANCER_MAGE_HOOD.get(), DevotionItems.TRANSMUTER_MAGE_HOOD.get(),
		DevotionItems.EMITTER_MAGE_HOOD.get(), DevotionItems.CONJURER_MAGE_HOOD.get(), DevotionItems.MANIPULATOR_MAGE_HOOD.get(),
		DevotionItems.DEATH_CULTIST_HOOD.get()
	);

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

	public static StructureMapData data = StructureMapData.empty();

	@Override
	public void onInitialize(ModContainer mod) {
		CONFIGURATOR.register(DevotionConfig.class);
		RegistryService registryService = RegistryService.get();

		// Registries that add gameplay features (e.g. items, blocks, and entities) //
		DevotionItems.ITEMS.accept(registryService);
		DevotionBlocks.BLOCKS.accept(registryService);
		DevotionBlocks.BLOCK_ENTITIES.accept(registryService);
		DevotionAltarActions.ACTIONS.accept(registryService);

		// Registries that supplement gameplay features (e.g. data components, materials, and recipes //
		DevotionCreativeTabs.CREATIVE_TABS.accept(registryService);
		DevotionMaterials.ARMOR_MATERIALS.accept(registryService);
		DevotionData.DATA_COMPONENTS.accept(registryService);
		DevotionAttributes.ATTRIBUTES.accept(registryService);
		DevotionRecipes.RECIPE_SERIALIZERS.accept(registryService);
		DevotionRecipes.RECIPE_TYPES.accept(registryService);

		Network.registerPacket(ClientboundAltarStructurePacket.TYPE, ClientboundAltarStructurePacket.class, ClientboundAltarStructurePacket.CODEC, ClientboundAltarStructurePacket::handle);
		Network.registerPacket(ClientboundDataPacket.TYPE, ClientboundDataPacket.class, ClientboundDataPacket.CODEC, ClientboundDataPacket::handle);

		ItemMenuInteractionEvent.EVENT.register((menu, player, level, clickAction, slot, slotStack, cursorStack) -> {
			if(clickAction == ClickAction.SECONDARY && cursorStack.isEmpty() && Devotion.HOOD_ITEMS.contains(slotStack.getItem())) {
				DataComponentType<Boolean> hoodData = DevotionData.CLOSED_HOOD.get();
				boolean value = !slotStack.getOrDefault(hoodData, false);

				slotStack.set(hoodData, value);
				Network.getNetworkHandler().sendToServer(new ServerboundOpenCloseHoodPacket(slot.getContainerSlot(), value));

				if(slotStack.getItem() instanceof Equipable equipable)
					level.playSeededSound(player, player.getX(), player.getY(), player.getZ(), equipable.getEquipSound().value(), SoundSource.NEUTRAL, 1f, 1f, player.getRandom().nextLong());

				return true;
			}

			return false;
		});
	}

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}
