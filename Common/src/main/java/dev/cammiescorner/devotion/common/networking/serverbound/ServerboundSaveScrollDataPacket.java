package dev.cammiescorner.devotion.common.networking.serverbound;

import commonnetwork.api.Network;
import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.RiddleData;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundRefreshResearchScreenPacket;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.screens.ResearchMenu;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.LecternBlockEntity;

import java.util.ArrayList;
import java.util.List;

public record ServerboundSaveScrollDataPacket(int containerId, List<AuraType> auraTypes) implements CustomPacketPayload {
	public static final Type<ServerboundSaveScrollDataPacket> TYPE = new Type<>(Devotion.id("save_scroll_data"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSaveScrollDataPacket> CODEC = StreamCodec.of((buffer, packet) -> {
		buffer.writeVarInt(packet.containerId);
		buffer.writeVarInt(packet.auraTypes.size());

		for(AuraType auraType : packet.auraTypes)
			buffer.writeEnum(auraType);

	}, buffer -> {
		int containerId = buffer.readVarInt();
		int listSize = buffer.readVarInt();
		List<AuraType> auraTypes = new ArrayList<>();

		for(int i = 0; i < listSize; i++)
			auraTypes.add(buffer.readEnum(AuraType.class));

		return new ServerboundSaveScrollDataPacket(containerId, auraTypes);
	});

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ServerboundSaveScrollDataPacket> context) {
		ServerPlayer player = context.sender();
		List<AuraType> auraTypes = context.message().auraTypes();
		int containerId = context.message().containerId();

		if(player.containerMenu.containerId == containerId && player.containerMenu instanceof ResearchMenu menu) {
			menu.getLevelAccess().execute((level, blockPos) -> {
				if(level.getBlockEntity(blockPos) instanceof LecternBlockEntity lectern) {
					DataComponentType<List<Integer>> auraTypesData = DevotionData.UNDO_BUFFER.get();
					DataComponentType<RiddleData> riddleData = DevotionData.RIDDLE_DATA.get();
					DataComponentType<Boolean> completedData = DevotionData.SCROLL_COMPLETED.get();
					DataComponentType<Long> completedTimeData = DevotionData.SCROLL_COMPLETED_TIME.get();
					ItemStack stack = lectern.getBook();
					RiddleData riddles = stack.get(riddleData);

					stack.set(auraTypesData, auraTypes.stream().map(AuraType::ordinal).toList());

					if(riddles != null) {
						for(int i = 0; i < auraTypes.size(); i++) {
							if(riddles.getRiddle(i).getFirst() != auraTypes.get(i))
								break;
							else if(riddles.riddles().size() == auraTypes.size() && !stack.getOrDefault(completedData, false)) {
								stack.set(completedData, true);
								stack.set(completedTimeData, level.getGameTime());
							}
						}

						lectern.setChanged();
						level.sendBlockUpdated(lectern.getBlockPos(), lectern.getBlockState(), lectern.getBlockState(), Block.UPDATE_ALL);

						Network.getNetworkHandler().sendToClientsLoadingPos(new ClientboundRefreshResearchScreenPacket(stack), (ServerLevel) level, lectern.getBlockPos());
					}
				}
			});
		}
	}
}
