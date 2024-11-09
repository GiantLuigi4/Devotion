package dev.cammiescorner.devotion.common.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.StructureMapData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundAltarStructurePacket(StructureMapData data) implements CustomPacketPayload {
	public static final Type<ClientboundAltarStructurePacket> TYPE = new Type<>(Devotion.id("altar_structure"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAltarStructurePacket> CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(StructureMapData.CODEC.xmap(ClientboundAltarStructurePacket::new, ClientboundAltarStructurePacket::data));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ClientboundAltarStructurePacket> context) {
		Devotion.data = context.message().data;
	}
}
