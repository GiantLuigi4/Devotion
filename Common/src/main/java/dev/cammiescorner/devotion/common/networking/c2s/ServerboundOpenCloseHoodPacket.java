package dev.cammiescorner.devotion.common.networking.c2s;

import commonnetwork.networking.data.PacketContext;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionTags;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public record ServerboundOpenCloseHoodPacket(int slot) implements CustomPacketPayload {
	public static final Type<ServerboundOpenCloseHoodPacket> TYPE = new Type<>(Devotion.id("open_close_hood"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenCloseHoodPacket> CODEC = StreamCodec.of((buffer, value) -> buffer.writeVarInt(value.slot), buffer -> new ServerboundOpenCloseHoodPacket(buffer.readVarInt()));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PacketContext<ServerboundOpenCloseHoodPacket> context) {
		int slot = context.message().slot();
		Inventory inventory = context.sender().getInventory();
		ItemStack stack = inventory.getItem(slot).copy();

		if(stack.is(DevotionTags.HOODS)) {
			DataComponentType<Boolean> hoodData = DevotionData.CLOSED_HOOD.get();
			System.out.println(stack.set(hoodData, !stack.getOrDefault(hoodData, false)));
			inventory.setItem(slot, stack);
		}
	}
}
