package dev.cammiescorner.devotion.api.research;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.Devotion;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record BookTab(ItemStack icon, int order) {
	public static final Codec<Holder<BookTab>> CODEC = RegistryFixedCodec.create(Devotion.BOOK_TAB_KEY);
	public static final Codec<BookTab> DIRECT_CODEC = RecordCodecBuilder.create(tabInstance -> tabInstance.group(
		ItemStack.CODEC.fieldOf("item_icon").forGetter(BookTab::icon),
		Codec.INT.optionalFieldOf("order", 1000).forGetter(BookTab::order)
	).apply(tabInstance, BookTab::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BookTab>> STREAM_CODEC = ByteBufCodecs.holderRegistry(Devotion.BOOK_TAB_KEY);

	public static BookTab get(ResourceLocation id, RegistryAccess access) {
		return access.registry(Devotion.BOOK_TAB_KEY).orElseThrow().get(id);
	}

	public ResourceLocation getId(RegistryAccess access) {
		return access.registry(Devotion.BOOK_TAB_KEY).orElseThrow().getKey(this);
	}
}
