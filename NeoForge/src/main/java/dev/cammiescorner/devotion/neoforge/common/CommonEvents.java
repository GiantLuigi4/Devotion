package dev.cammiescorner.devotion.neoforge.common;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.data.ResearchReloadListener;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.AuraCapability;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.KnownResearchCapability;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@EventBusSubscriber(modid = Devotion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CommonEvents {
	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerEntity(NeoMain.AURA, EntityType.PLAYER, (entity, unused) -> new AuraCapability(entity, AuraCapability.MAX_AURA, 0xffffff));
		event.registerEntity(NeoMain.KNOWN_RESEARCH, EntityType.PLAYER, (player, unused) -> new KnownResearchCapability(player));
	}

	@SubscribeEvent
	public static void registerReloadListeners(AddReloadListenerEvent event) {
		event.addListener(new ResearchReloadListener());
	}
}
