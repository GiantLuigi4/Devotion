package dev.cammiescorner.devotion.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.actions.ConfiguredAltarAction;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.blocks.entities.AltarBlockEntity;
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
import java.util.List;

public class DevotionAltarRecipe implements Recipe<AltarBlockEntity> {
	private final String group;
	private final List<Ingredient> input;
	private final int power;
	private final boolean requiresPlayer;
	private final Holder<ConfiguredAltarAction> result;
	private final List<Holder<Research>> requiredResearch;

	public DevotionAltarRecipe(String group, List<Ingredient> input, int power, boolean requiresPlayer, Holder<ConfiguredAltarAction> result, List<Holder<Research>> requiredResearch) {
		this.group = group;
		this.input = input;
		this.power = power;
		this.requiresPlayer = requiresPlayer;
		this.result = result;
		this.requiredResearch = requiredResearch;
	}

	@Override
	public boolean matches(AltarBlockEntity altar, Level level) {
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
	public ItemStack assemble(AltarBlockEntity input, HolderLookup.Provider registries) {
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

	public void assemble(ServerLevel world, @Nullable ServerPlayer player, AltarBlockEntity altar) {
		result.value().run(world, player, altar);
		altar.clearContent();
	}

	public int getPower() {
		return power;
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
				Codec.INT.fieldOf("power").forGetter(DevotionAltarRecipe::getPower),
				Codec.BOOL.optionalFieldOf("requires_player", false).forGetter(DevotionAltarRecipe::requiresPlayer),
				ConfiguredAltarAction.CODEC.fieldOf("result").forGetter(DevotionAltarRecipe::getResult),
				Research.CODEC.listOf().optionalFieldOf("prerequisites", List.of(Holder.direct(Research.getById(Devotion.id("empty"))))).forGetter(DevotionAltarRecipe::getRequiredResearch)
			).apply(recipe, DevotionAltarRecipe::new)
		);

		StreamCodec<RegistryFriendlyByteBuf, DevotionAltarRecipe> STREAM_CODEC = StreamCodec.of(
			(buffer, recipe) -> {
				buffer.writeUtf(recipe.getGroup());
				buffer.writeVarInt(recipe.getIngredients().size());

				for(Ingredient ingredient : recipe.getIngredients())
					Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);

				buffer.writeVarInt(recipe.getPower());
				buffer.writeBoolean(recipe.requiresPlayer());
				ConfiguredAltarAction.HOLDER_STREAM_CODEC.encode(buffer, recipe.getResult());
				buffer.writeVarInt(recipe.getRequiredResearch().size());

				for(Holder<Research> researchHolder : recipe.getRequiredResearch())
					Research.HOLDER_STREAM_CODEC.encode(buffer, researchHolder);
			},
			buffer -> {
				List<Ingredient> ingredients = new ArrayList<>();
				List<Holder<Research>> requiredResearch = new ArrayList<>();
				String group = buffer.readUtf();
				int ingredientsSize = buffer.readVarInt();

				for(int i = 0; i < ingredientsSize; i++)
					ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

				int power = buffer.readVarInt();
				boolean requiresPlayer = buffer.readBoolean();
				Holder<ConfiguredAltarAction> action = ConfiguredAltarAction.HOLDER_STREAM_CODEC.decode(buffer);
				int researchSize = buffer.readVarInt();

				for(int i = 0; i < researchSize; i++)
					requiredResearch.add(Research.HOLDER_STREAM_CODEC.decode(buffer));

				return new DevotionAltarRecipe(group, ingredients, power, requiresPlayer, action, requiredResearch);
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
