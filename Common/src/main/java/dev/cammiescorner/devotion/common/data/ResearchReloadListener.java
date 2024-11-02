package dev.cammiescorner.devotion.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.Research;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ResearchReloadListener extends SimpleJsonResourceReloadListener {
	private static final Gson GSON = new GsonBuilder().create();

	public ResearchReloadListener() {
		super(GSON, "devotion_research");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {
		Devotion.RESEARCH.clear();
		Map<Research, Set<ResourceLocation>> parentMap = new HashMap<>();

		prepared.forEach((identifier, jsonElement) -> {
//			JsonObject json = GsonHelper.getAsJsonObject(jsonElement, "devotion:research");
//			JsonArray parents = GsonHelper.getAsJsonArray(json, "parents", new JsonArray());
//			Set<ResourceLocation> parentIds = new HashSet<>();
//			boolean hidden = GsonHelper.getAsBoolean(json, "hidden", false);
//			Item item = GsonHelper.getAsItem(json, "item_icon").value();
//			Research.Difficulty difficulty = Research.Difficulty.valueOf(GsonHelper.getAsString(json, "difficulty").toUpperCase(Locale.ROOT));
//			Research research = new Research(identifier, difficulty, hidden, item);
//
//			for(JsonElement parent : parents)
//				parentIds.add(ResourceLocation.parse(GsonHelper.getAsString(parent, "research_id")));
//
//			parentMap.put(research, parentIds);
//
//			Devotion.RESEARCH.put(identifier, research);
		});

		parentMap.forEach((research, identifiers) -> {
			research.setParents(identifiers.stream().map(identifier -> {
				if(!Devotion.RESEARCH.containsKey(identifier)) {
					Devotion.LOGGER.warn("Research {} is missing a parent {}", research.getId(), identifier);
					return null;
				}
				else {
					return Devotion.RESEARCH.get(identifier);
				}
			}).filter(Objects::nonNull).collect(Collectors.toSet()));
		});
	}
}
