package dev.cammiescorner.devotion.common.networking.s2c;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.StructureMapData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientBoundAltarStructurePacket(StructureMapData data) implements CustomPacketPayload {
	public static final Type<ClientBoundAltarStructurePacket> TYPE = new Type<>(Devotion.id("altar_structure"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundAltarStructurePacket> CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(StructureMapData.CODEC.xmap(ClientBoundAltarStructurePacket::new, ClientBoundAltarStructurePacket::data));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ClientBoundAltarStructurePacket> context) {
		Devotion.data = context.message().data;
	}
}
