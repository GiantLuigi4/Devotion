package dev.cammiescorner.devotion.common.networking.s2c;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;

public record ClientboundDataPacket(int entityId, float aura, int auraColor) implements CustomPacketPayload {
	public static final Type<ClientboundAltarStructurePacket> TYPE = new Type<>(Devotion.id("aura"));
	public static final StreamCodec<? extends FriendlyByteBuf, ClientboundDataPacket> CODEC = StreamCodec.of((buffer, value) -> {
		buffer.writeVarInt(value.entityId);
		buffer.writeFloat(value.aura);
		buffer.writeVarInt(value.auraColor);
	}, buffer -> {
		int entityId = buffer.readVarInt();
		float aura = buffer.readFloat();
		int auraColor = buffer.readVarInt();

		return new ClientboundDataPacket(entityId, aura, auraColor);
	});

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ClientboundDataPacket> context) {
		ClientLevel level = Minecraft.getInstance().level;
		int entityId = context.message().entityId;
		float aura = context.message().aura;
		int auraColor = context.message().auraColor;

		if(level != null && level.getEntity(entityId) instanceof LivingEntity entity) {
			MainHelper.setAura(entity, aura);
			MainHelper.setAuraColor(entity, new Color(auraColor));
		}
	}
}
