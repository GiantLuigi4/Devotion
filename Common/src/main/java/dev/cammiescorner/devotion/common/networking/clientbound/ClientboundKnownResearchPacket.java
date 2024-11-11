package dev.cammiescorner.devotion.common.networking.clientbound;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public record ClientboundKnownResearchPacket(Set<ResourceLocation> researchIds) implements CustomPacketPayload {
	public static final Type<ClientboundKnownResearchPacket> TYPE = new Type<>(Devotion.id("known_research"));
	public static final StreamCodec<? extends FriendlyByteBuf, ClientboundKnownResearchPacket> CODEC = StreamCodec.of((buffer, value) -> {
		buffer.writeVarInt(value.researchIds.size());
		value.researchIds.forEach(buffer::writeResourceLocation);
	}, buffer -> {
		Set<ResourceLocation> knownResearchIds = new HashSet<>();
		int size = buffer.readVarInt();

		for(int i = 0; i < size; i++)
			knownResearchIds.add(buffer.readResourceLocation());

		return new ClientboundKnownResearchPacket(knownResearchIds);
	});

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ClientboundKnownResearchPacket> context) {
		if(Minecraft.getInstance().level instanceof ClientLevel level && Minecraft.getInstance().player instanceof Player player)
			for(ResourceLocation id : context.message().researchIds())
				MainHelper.giveResearch(player, Research.get(id, level.registryAccess()), false);
	}
}
