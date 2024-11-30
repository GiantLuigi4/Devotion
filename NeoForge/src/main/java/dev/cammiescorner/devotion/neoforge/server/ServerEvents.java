package dev.cammiescorner.devotion.neoforge.server;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.BookEntry;
import dev.cammiescorner.devotion.api.research.BookTab;
import dev.cammiescorner.devotion.api.research.Research;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Devotion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ServerEvents {
	@SubscribeEvent
	public static void registerDynamicRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(Devotion.RESEARCH_KEY, Research.DIRECT_CODEC);
		event.dataPackRegistry(Devotion.BOOK_TAB_KEY, BookTab.DIRECT_CODEC);
		event.dataPackRegistry(Devotion.BOOK_ENTRY_KEY, BookEntry.DIRECT_CODEC);
	}
}
