package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class DevotionTags {
	public static final TagKey<Block> ALTAR_PALETTE = TagKey.create(Registries.BLOCK, Devotion.id("altar_palette"));
	public static final TagKey<Item> HOODS = TagKey.create(Registries.ITEM, Devotion.id("hoods"));
}
