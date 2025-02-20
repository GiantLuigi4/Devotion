package dev.cammiescorner.devotion.fabric.entrypoints;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.BookEntry;
import dev.cammiescorner.devotion.api.research.BookTab;
import dev.cammiescorner.devotion.api.research.Research;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

@CalledByReflection
public class FabricMain implements ModInitializer {
	@Override
	public void onInitialize() {
		DynamicRegistries.registerSynced(Devotion.RESEARCH_KEY, Research.DIRECT_CODEC);
		DynamicRegistries.registerSynced(Devotion.BOOK_TAB_KEY, BookTab.DIRECT_CODEC);
		DynamicRegistries.registerSynced(Devotion.BOOK_ENTRY_KEY, BookEntry.DIRECT_CODEC);
	}
}
