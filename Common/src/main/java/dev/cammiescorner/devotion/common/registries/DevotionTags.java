package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DevotionTags {
	public static final TagKey<Block> ALTAR_PILLAR = TagKey.create(Registries.BLOCK, Devotion.id("altar_pillar"));
	public static final TagKey<Block> ALTAR_CAPS = TagKey.create(Registries.BLOCK, Devotion.id("altar_caps"));
}
