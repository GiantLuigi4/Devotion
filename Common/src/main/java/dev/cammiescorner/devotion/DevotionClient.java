package dev.cammiescorner.devotion;

import commonnetwork.api.Network;
import dev.cammiescorner.devotion.api.events.ScriptsOfDevotionScreenCallback;
import dev.cammiescorner.devotion.client.AuraEffectManager;
import dev.cammiescorner.devotion.client.gui.widgets.ResearchWidget;
import dev.cammiescorner.devotion.client.models.armor.DeathCultLeaderArmorModel;
import dev.cammiescorner.devotion.client.models.armor.DeathCultistRobesModel;
import dev.cammiescorner.devotion.client.models.armor.MageRobesModel;
import dev.cammiescorner.devotion.client.renderers.entity.armor.DeathCultLeaderArmorRenderer;
import dev.cammiescorner.devotion.client.renderers.entity.armor.DeathCultistRobesRenderer;
import dev.cammiescorner.devotion.client.renderers.entity.armor.MageRobesRenderer;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.networking.c2s.ServerboundGiveResearchScrollPacket;
import dev.cammiescorner.devotion.common.networking.c2s.ServerboundOpenCloseHoodPacket;
import dev.cammiescorner.devotion.common.registries.DevotionBlocks;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.cammiescorner.velvet.api.event.EntitiesPreRenderCallback;
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.upcraft.sparkweave.api.client.event.RegisterCustomArmorRenderersEvent;
import dev.upcraft.sparkweave.api.client.event.RegisterLayerDefinitionsEvent;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.List;

public class DevotionClient implements ClientEntryPoint {
	private static final ResourceLocation BASIC_MAGE_ROBES = Devotion.id("textures/entity/armor/basic_mage_robes.png");
	private static final ResourceLocation ENHANCER_MAGE_ROBES = Devotion.id("textures/entity/armor/enhancer_mage_robes.png");
	private static final ResourceLocation TRANSMUTER_MAGE_ROBES = Devotion.id("textures/entity/armor/transmuter_mage_robes.png");
	private static final ResourceLocation EMITTER_MAGE_ROBES = Devotion.id("textures/entity/armor/emitter_mage_robes.png");
	private static final ResourceLocation CONJURER_MAGE_ROBES = Devotion.id("textures/entity/armor/conjurer_mage_robes.png");
	private static final ResourceLocation MANIPULATOR_MAGE_ROBES = Devotion.id("textures/entity/armor/manipulator_mage_robes.png");
	public static final Minecraft client = Minecraft.getInstance();
	public static float guideBookOffsetX, guideBookOffsetY;

	@Override
	public void onInitializeClient(ModContainer mod) {
		EntitiesPreRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);

		RegisterLayerDefinitionsEvent.EVENT.register(event -> {
			event.registerModelLayers(MageRobesModel.MODEL_LAYER, MageRobesModel::createBodyLayer);
			event.registerModelLayers(DeathCultistRobesModel.MODEL_LAYER, DeathCultistRobesModel::createBodyLayer);
			event.registerModelLayers(DeathCultLeaderArmorModel.MODEL_LAYER, DeathCultLeaderArmorModel::createBodyLayer);
		});

		RegisterCustomArmorRenderersEvent.EVENT.register(event -> {
			event.register((entity, context, renderer) -> new MageRobesRenderer(context, BASIC_MAGE_ROBES), DevotionItems.BASIC_MAGE_HOOD.get(), DevotionItems.BASIC_MAGE_ROBE.get(), DevotionItems.BASIC_MAGE_BELT.get(), DevotionItems.BASIC_MAGE_BOOTS.get());
			event.register((entity, context, renderer) -> new MageRobesRenderer(context, ENHANCER_MAGE_ROBES), DevotionItems.ENHANCER_MAGE_HOOD.get(), DevotionItems.ENHANCER_MAGE_ROBE.get(), DevotionItems.ENHANCER_MAGE_BELT.get(), DevotionItems.ENHANCER_MAGE_BOOTS.get());
			event.register((entity, context, renderer) -> new MageRobesRenderer(context, TRANSMUTER_MAGE_ROBES), DevotionItems.TRANSMUTER_MAGE_HOOD.get(), DevotionItems.TRANSMUTER_MAGE_ROBE.get(), DevotionItems.TRANSMUTER_MAGE_BELT.get(), DevotionItems.TRANSMUTER_MAGE_BOOTS.get());
			event.register((entity, context, renderer) -> new MageRobesRenderer(context, EMITTER_MAGE_ROBES), DevotionItems.EMITTER_MAGE_HOOD.get(), DevotionItems.EMITTER_MAGE_ROBE.get(), DevotionItems.EMITTER_MAGE_BELT.get(), DevotionItems.EMITTER_MAGE_BOOTS.get());
			event.register((entity, context, renderer) -> new MageRobesRenderer(context, CONJURER_MAGE_ROBES), DevotionItems.CONJURER_MAGE_HOOD.get(), DevotionItems.CONJURER_MAGE_ROBE.get(), DevotionItems.CONJURER_MAGE_BELT.get(), DevotionItems.CONJURER_MAGE_BOOTS.get());
			event.register((entity, context, renderer) -> new MageRobesRenderer(context, MANIPULATOR_MAGE_ROBES), DevotionItems.MANIPULATOR_MAGE_HOOD.get(), DevotionItems.MANIPULATOR_MAGE_ROBE.get(), DevotionItems.MANIPULATOR_MAGE_BELT.get(), DevotionItems.MANIPULATOR_MAGE_BOOTS.get());
			event.register((entity, context, renderer) -> new DeathCultistRobesRenderer(context), DevotionItems.DEATH_CULTIST_HOOD.get(), DevotionItems.DEATH_CULTIST_ROBE.get(), DevotionItems.DEATH_CULTIST_LEGGINGS.get(), DevotionItems.DEATH_CULTIST_BOOTS.get());
			event.register((entity, context, renderer) -> new DeathCultLeaderArmorRenderer(context), DevotionItems.DEATH_CULT_LEADER_HEADDRESS.get(), DevotionItems.DEATH_CULT_LEADER_CLOAK.get(), DevotionItems.DEATH_CULT_LEADER_LEGGINGS.get(), DevotionItems.DEATH_CULT_LEADER_BOOTS.get());
		});

		Network.registerPacket(ServerboundOpenCloseHoodPacket.TYPE, ServerboundOpenCloseHoodPacket.class, ServerboundOpenCloseHoodPacket.CODEC, ServerboundOpenCloseHoodPacket::handle);
		Network.registerPacket(ServerboundGiveResearchScrollPacket.TYPE, ServerboundGiveResearchScrollPacket.class, ServerboundGiveResearchScrollPacket.CODEC, ServerboundGiveResearchScrollPacket::handle);

		createItemPropertyForList(Devotion.HOOD_ITEMS, Devotion.id("closed_hood"), (stack, level, entity, seed) -> stack.getOrDefault(DevotionData.CLOSED_HOOD.get(), false) ? 1f : 0f);

		ScriptsOfDevotionScreenCallback.ADD_TAB.register(tabMap -> {
			tabMap.put(Devotion.id("artifice"), DevotionItems.BASIC_MAGE_HOOD.get());
			tabMap.put(Devotion.id("spells"), DevotionBlocks.ALTAR_BLOCK.get().asItem());
			tabMap.put(Devotion.id("cults"), DevotionItems.DEATH_CULTIST_HOOD.get());
		});

		ScriptsOfDevotionScreenCallback.ADD_RESEARCH.register((screen, x, y) -> {
			screen.addArtificeChild(new ResearchWidget(x + 144, y + 170, Devotion.id("root"), DevotionClient::researchWidgetClick));
			screen.addArtificeChild(new ResearchWidget(x + 204, y + 170, Devotion.id("research"), DevotionClient::researchWidgetClick));
			screen.addArtificeChild(new ResearchWidget(x + 174, y + 111, Devotion.id("altar_focus"), DevotionClient::researchWidgetClick));
			screen.addArtificeChild(new ResearchWidget(x + 54, y + 130, Devotion.id("basic_mage_armor"), DevotionClient::researchWidgetClick));
			screen.addArtificeChild(new ResearchWidget(x + 54, y + 66, Devotion.id("enhancement_mage_armor"), DevotionClient::researchWidgetClick));
			screen.addArtificeChild(new ResearchWidget(x + 114, y + 111, Devotion.id("transmutation_mage_armor"), DevotionClient::researchWidgetClick));
			screen.addArtificeChild(new ResearchWidget(x - 6, y + 111, Devotion.id("emission_mage_armor"), DevotionClient::researchWidgetClick));
			screen.addArtificeChild(new ResearchWidget(x + 91, y + 181, Devotion.id("conjuration_mage_armor"), DevotionClient::researchWidgetClick));
			screen.addArtificeChild(new ResearchWidget(x + 17, y + 181, Devotion.id("manipulation_mage_armor"), DevotionClient::researchWidgetClick));
		});
	}

	private static void researchWidgetClick(ResearchWidget widget) {
		Player player = client.player;

		if(player != null) {
			ResourceLocation researchId = widget.getResearch().getId();

			if(MainHelper.getResearchIds(player).contains(researchId))
				return;

			Network.getNetworkHandler().sendToServer(new ServerboundGiveResearchScrollPacket(researchId));
		}
	}

	private static void createItemPropertyForList(List<Item> items, ResourceLocation name, ClampedItemPropertyFunction property) {
		for(Item item : items)
			ItemProperties.register(item, name, property);
	}
}
