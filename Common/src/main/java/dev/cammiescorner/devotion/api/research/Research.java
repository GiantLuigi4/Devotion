package dev.cammiescorner.devotion.api.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.Devotion;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record Research(ItemStack icon, Research.Difficulty difficulty, boolean isHidden, Set<ResourceLocation> parentIds) {
	public static final Codec<Holder<Research>> CODEC = RegistryFixedCodec.create(Devotion.RESEARCH_KEY);
	public static final Codec<Research> DIRECT_CODEC = RecordCodecBuilder.create(researchInstance -> researchInstance.group(
		ItemStack.CODEC.fieldOf("item_icon").forGetter(Research::icon),
		Difficulty.CODEC.fieldOf("difficulty").forGetter(Research::difficulty),
		Codec.BOOL.optionalFieldOf("hidden", false).forGetter(Research::isHidden),
		ResourceLocation.CODEC.listOf().xmap(Set::copyOf, List::copyOf).optionalFieldOf("parents", Set.of()).forGetter(Research::parentIds)
	).apply(researchInstance, Research::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Research>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Devotion.RESEARCH_KEY);

	public ResourceLocation getId(RegistryAccess provider) {
		return provider.registry(Devotion.RESEARCH_KEY).orElseThrow().getKey(this);
	}

	public Set<Research> getParents(HolderLookup.Provider provider) {
		HolderLookup.RegistryLookup<Research> lookup = provider.lookupOrThrow(Devotion.RESEARCH_KEY);
		return parentIds.stream().map(id -> ResourceKey.create(Devotion.RESEARCH_KEY, id)).map(lookup::getOrThrow).map(Holder.Reference::value).collect(Collectors.toSet());
	}

	public enum Difficulty implements StringRepresentable {
		EASY("easy"), NORMAL("normal"), HARD("hard");

		public static final Codec<Difficulty> CODEC = StringRepresentable.fromEnum(Difficulty::values);
		private final String name;

		Difficulty(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
