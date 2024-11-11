package dev.cammiescorner.devotion.neoforge.common.capabilities.entity;

import com.google.common.collect.ImmutableSet;
import commonnetwork.api.Network;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundKnownResearchPacket;
import dev.cammiescorner.devotion.neoforge.common.capabilities.SyncedCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class KnownResearchCapability implements SyncedCapability {
	private final Set<ResourceLocation> researchIds = new HashSet<>();
	private final Player player;

	public KnownResearchCapability(Player player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		ListTag nbtList = tag.getList("KnownResearch", ListTag.TAG_STRING);
		researchIds.clear();

		for(int i = 0; i < nbtList.size(); i++)
			researchIds.add(ResourceLocation.parse(nbtList.getString(i)));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag nbtList = new ListTag();

		for(ResourceLocation researchId : researchIds)
			nbtList.add(StringTag.valueOf(researchId.toString()));

		tag.put("KnownResearch", nbtList);
	}

	public Set<ResourceLocation> getResearchIds() {
		return ImmutableSet.copyOf(researchIds);
	}

	public boolean giveResearch(Research research, boolean simulate) {
		ResourceLocation researchId = research.getId(player.level().registryAccess());

		if(!researchIds.contains(researchId)) {
			if(!simulate) {
				researchIds.add(researchId);

				if(player instanceof ServerPlayer serverPlayer)
					Network.getNetworkHandler().sendToClient(new ClientboundKnownResearchPacket(researchIds), serverPlayer);
			}

			return true;
		}

		return false;
	}

	public boolean revokeResearch(Research research, boolean simulate) {
		ResourceLocation researchId = research.getId(player.level().registryAccess());

		if(researchIds.contains(researchId)) {
			if(!simulate) {
				researchIds.remove(researchId);

				if(player instanceof ServerPlayer serverPlayer)
					Network.getNetworkHandler().sendToClient(new ClientboundKnownResearchPacket(researchIds), serverPlayer);
			}

			return true;
		}

		return false;
	}
}
