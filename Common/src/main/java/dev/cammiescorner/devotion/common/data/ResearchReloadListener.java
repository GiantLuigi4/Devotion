package dev.cammiescorner.devotion.common.data;

import com.google.gson.*;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.recipes.DevotionAltarRecipe;
import dev.cammiescorner.devotion.common.registries.DevotionRecipes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.*;
import java.util.stream.Collectors;

public class ResearchReloadListener extends SimpleJsonResourceReloadListener {
	private static final Gson GSON = new GsonBuilder().create();

	public ResearchReloadListener() {
		super(GSON, "devotion_research");
	}

	public static void verifyResearchInRecipes(MinecraftServer server) {
		for(RecipeHolder<DevotionAltarRecipe> holder : server.getRecipeManager().getAllRecipesFor(DevotionRecipes.ALTAR_TYPE.get())) {
			DevotionAltarRecipe recipe = holder.value();

			for(Holder<Research> research : recipe.getRequiredResearch())
				if(research == null)
					Devotion.LOGGER.error("Altar recipe {} has non-existent research requirement", BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType()));
		}
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {
		Devotion.RESEARCH.clear();
		Map<Research, Set<ResourceLocation>> parentMap = new HashMap<>();

		prepared.forEach((identifier, jsonElement) -> {
			JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "devotion:research");
			JsonArray parents = GsonHelper.getAsJsonArray(json, "parents", new JsonArray());
			Set<ResourceLocation> parentIds = new HashSet<>();
			boolean hidden = GsonHelper.getAsBoolean(json, "hidden", false);
			Item item = GsonHelper.getAsItem(json, "item_icon").value();
			Research.Difficulty difficulty = Research.Difficulty.valueOf(GsonHelper.getAsString(json, "difficulty").toUpperCase(Locale.ROOT));
			Research research = new Research(difficulty, hidden, new ItemStack(item));

			for(JsonElement parent : parents)
				parentIds.add(ResourceLocation.parse(GsonHelper.convertToString(parent, "research_id")));

			parentMap.put(research, parentIds);

			Devotion.RESEARCH.put(identifier, research);
		});

		parentMap.forEach((research, identifiers) -> research.setParents(identifiers.stream().map(identifier -> {
			if(!Devotion.RESEARCH.containsKey(identifier)) {
				Devotion.LOGGER.warn("Research {} is missing a parent {}", research.getId(), identifier);
				return null;
			}
			else {
				return Devotion.RESEARCH.get(identifier);
			}
		}).filter(Objects::nonNull).collect(Collectors.toSet())));
	}
}
