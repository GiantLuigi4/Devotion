package dev.cammiescorner.devotion.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.api.actions.ConfiguredAltarAction;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.blocks.entities.AltarFocusBlockEntity;
import dev.cammiescorner.devotion.common.registries.DevotionRecipes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevotionAltarRecipe implements Recipe<AltarFocusBlockEntity> {
	private final String group;
	private final List<Ingredient> input;
	private final Map<AuraType, Float> auraCosts = new HashMap<>();
	private final boolean requiresPlayer;
	private final Holder<ConfiguredAltarAction> result;
	private final List<Holder<Research>> requiredResearch;

	public DevotionAltarRecipe(String group, List<Ingredient> input, Map<AuraType, Float> auraCosts, boolean requiresPlayer, Holder<ConfiguredAltarAction> result, List<Holder<Research>> requiredResearch) {
		this.group = group;
		this.input = input;
		this.auraCosts.putAll(auraCosts);
		this.requiresPlayer = requiresPlayer;
		this.result = result;
		this.requiredResearch = requiredResearch;
	}

	@Override
	public boolean matches(AltarFocusBlockEntity altar, Level level) {
		StackedContents matcher = new StackedContents();

		int i = 0;

		for(int j = 0; j < altar.size(); ++j) {
			ItemStack stack = altar.getItem(j);

			if(stack.isEmpty())
				continue;

			++i;
			matcher.accountStack(stack, 1);
		}

		return i == input.size() && matcher.canCraft(this, null);
	}

	@Override
	public ItemStack assemble(AltarFocusBlockEntity input, HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width <= 10 && width > 0 && height == 1;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return ItemStack.EMPTY;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return DevotionRecipes.ALTAR_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType() {
		return DevotionRecipes.ALTAR_TYPE.get();
	}

	public void assemble(ServerLevel world, @Nullable ServerPlayer player, AltarFocusBlockEntity altar) {
		result.value().run(world, player, altar);
		altar.clearContent();
	}

	public Map<AuraType, Float> getAuraCosts() {
		return Map.copyOf(auraCosts);
	}

	public float getAuraCost(AuraType auraType) {
		return auraCosts.get(auraType);
	}

	public boolean requiresPlayer() {
		return requiresPlayer;
	}

	public Holder<ConfiguredAltarAction> getResult() {
		return result;
	}

	public List<Holder<Research>> getRequiredResearch() {
		return requiredResearch;
	}

	public static class Serializer implements RecipeSerializer<DevotionAltarRecipe> {
		MapCodec<DevotionAltarRecipe> CODEC = RecordCodecBuilder.mapCodec(recipe ->
			recipe.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(DevotionAltarRecipe::getGroup),
				Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(Recipe::getIngredients),
				Codec.unboundedMap(AuraType.CODEC.xmap(auraType -> {
					if(auraType.ordinal() >= AuraType.SPECIALIZATION.ordinal())
						throw new IllegalArgumentException(auraType.getName() + " is not a valid aura type for recipes");

					return auraType;
				}, auraType -> auraType), Codec.FLOAT).optionalFieldOf("aura_costs", Map.of()).forGetter(DevotionAltarRecipe::getAuraCosts),
				Codec.BOOL.optionalFieldOf("requires_player", false).forGetter(DevotionAltarRecipe::requiresPlayer),
				ConfiguredAltarAction.CODEC.fieldOf("result").forGetter(DevotionAltarRecipe::getResult),
				Research.CODEC.listOf().optionalFieldOf("prerequisites", List.of()).forGetter(DevotionAltarRecipe::getRequiredResearch)
			).apply(recipe, DevotionAltarRecipe::new)
		);

		StreamCodec<RegistryFriendlyByteBuf, DevotionAltarRecipe> STREAM_CODEC = StreamCodec.of(
			(buffer, recipe) -> {
				buffer.writeUtf(recipe.getGroup());
				buffer.writeVarInt(recipe.getIngredients().size());

				for(Ingredient ingredient : recipe.getIngredients())
					Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);

				buffer.writeVarInt(recipe.getAuraCosts().size());

				for(AuraType auraType : recipe.getAuraCosts().keySet()) {
					buffer.writeEnum(auraType);
					buffer.writeFloat(recipe.getAuraCosts().get(auraType));
				}

				buffer.writeBoolean(recipe.requiresPlayer());
				ConfiguredAltarAction.HOLDER_STREAM_CODEC.encode(buffer, recipe.getResult());
				buffer.writeVarInt(recipe.getRequiredResearch().size());

				for(Holder<Research> researchHolder : recipe.getRequiredResearch())
					Research.STREAM_CODEC.encode(buffer, researchHolder);
			},
			buffer -> {
				List<Ingredient> ingredients = new ArrayList<>();
				List<Holder<Research>> requiredResearch = new ArrayList<>();
				Map<AuraType, Float> auraCosts = new HashMap<>();
				String group = buffer.readUtf();
				int ingredientsSize = buffer.readVarInt();

				for(int i = 0; i < ingredientsSize; i++)
					ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

				int mapSize = buffer.readVarInt();

				for(int i = 0; i < mapSize; i++)
					auraCosts.put(buffer.readEnum(AuraType.class), buffer.readFloat());

				boolean requiresPlayer = buffer.readBoolean();
				Holder<ConfiguredAltarAction> action = ConfiguredAltarAction.HOLDER_STREAM_CODEC.decode(buffer);
				int researchSize = buffer.readVarInt();

				for(int i = 0; i < researchSize; i++)
					requiredResearch.add(Research.STREAM_CODEC.decode(buffer));

				return new DevotionAltarRecipe(group, ingredients, auraCosts, requiresPlayer, action, requiredResearch);
			}
		);

		@Override
		public MapCodec<DevotionAltarRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, DevotionAltarRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
