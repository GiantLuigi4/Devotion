package dev.cammiescorner.devotion.common.networking.c2s;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionTags;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

public record ServerboundOpenCloseHoodPacket(ItemStack stack) implements CustomPacketPayload {
	public static final Type<ServerboundOpenCloseHoodPacket> TYPE = new Type<>(Devotion.id("open_close_hood"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenCloseHoodPacket> CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(ItemStack.CODEC.xmap(ServerboundOpenCloseHoodPacket::new, ServerboundOpenCloseHoodPacket::stack));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ServerboundOpenCloseHoodPacket> context) {
		ItemStack itemStack = context.message().stack();

		if(itemStack.is(DevotionTags.HOODS)) {
			DataComponentType<Boolean> hoodData = DevotionData.CLOSED_HOOD.get();
			itemStack.set(hoodData, !itemStack.getOrDefault(hoodData, false));
		}
	}
}
