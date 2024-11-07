package dev.cammiescorner.devotion.neoforge.common.capabilities.entity;

import com.google.common.collect.ImmutableSet;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.neoforge.common.capabilities.SerializableCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;

public class KnownResearchCapability implements SerializableCapability {
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
		if(!researchIds.contains(research.getId())) {
			if(!simulate) {
				researchIds.add(research.getId());

				if(player.level() instanceof ServerLevel level) {
					// TODO sync packet
				}
			}

			return true;
		}

		return false;
	}

	public boolean revokeResearch(Research research, boolean simulate) {
		if(researchIds.contains(research.getId())) {
			if(!simulate) {
				researchIds.remove(research.getId());

				if(player.level() instanceof ServerLevel level) {
					// TODO sync packet
				}
			}

			return true;
		}

		return false;
	}
}
