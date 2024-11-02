package dev.cammiescorner.devotion.api.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.Devotion;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class Research {
	public static final ResourceKey<Registry<Research>> REGISTRY_KEY = ResourceKey.createRegistryKey(Devotion.id("research"));
	public static final Codec<Holder<Research>> CODEC = RegistryFixedCodec.create(REGISTRY_KEY);
	public static final Codec<Research> DIRECT_CODEC = RecordCodecBuilder.create(researchInstance -> researchInstance.group(
		Difficulty.CODEC.fieldOf("difficulty").forGetter(Research::getDifficulty),
		Codec.BOOL.fieldOf("hidden").forGetter(Research::isHidden),
		ItemStack.CODEC.fieldOf("icon").forGetter(Research::getIcon)
	).apply(researchInstance, Research::new));
	private final Set<Research> parents = new HashSet<>();
	private final Difficulty difficulty;
	private final ItemStack icon;
	private final boolean isHidden;

	public Research(Difficulty difficulty, boolean isHidden, ItemStack icon) {
		this.difficulty = difficulty;
		this.isHidden = isHidden;
		this.icon = icon;
	}

	public static Research getById(ResourceLocation id) {
		return Devotion.RESEARCH.get(id);
	}

	public Set<Research> getParents() {
		return parents;
	}

	public Set<ResourceLocation> getParentIds() {
		Set<ResourceLocation> parentIds = new HashSet<>();

		for(Research parent : parents)
			parentIds.add(parent.getId());

		return parentIds;
	}

	public ResourceLocation getId() {
		return Devotion.RESEARCH.inverse().get(this);
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public String getTranslationKey() {
		ResourceLocation id = getId();
		return "research." + id.getNamespace() + "." + id.getPath();
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setParents(Set<Research> parents) {
		this.parents.clear();
		this.parents.addAll(parents);
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
