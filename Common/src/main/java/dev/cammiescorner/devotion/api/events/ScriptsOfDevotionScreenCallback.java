package dev.cammiescorner.devotion.api.events;

import dev.cammiescorner.devotion.client.gui.screens.ScriptsOfDevotionScreen;
import dev.upcraft.sparkweave.api.event.Event;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;

public class ScriptsOfDevotionScreenCallback {
	public static final Event<AddResearchEvent> ADD_RESEARCH = Event.create(AddResearchEvent.class, callbacks -> (screen, x, y) -> {
		for(AddResearchEvent callback : callbacks)
			callback.addWidgets(screen, x, y);
	});

	public static final Event<AddTabEvent> ADD_TAB = Event.create(AddTabEvent.class, callbacks -> map -> {
		for(AddTabEvent callback : callbacks)
			callback.addTabs(map);
	});

	public interface AddResearchEvent {
		void addWidgets(ScriptsOfDevotionScreen screen, int x, int y);
	}

	public interface AddTabEvent {
		void addTabs(LinkedHashMap<ResourceLocation, Item> map);
	}
}
