package dev.cammiescorner.devotion.fabric.common.components.entity;

import com.google.common.collect.ImmutableSet;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.HashSet;
import java.util.Set;

public class KnownResearchComponent implements AutoSyncedComponent {
	private final Set<ResourceLocation> researchIds = new HashSet<>();
	private final Player player;

	public KnownResearchComponent(Player player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider provider) {
		ListTag nbtList = tag.getList("KnownResearch", ListTag.TAG_STRING);
		researchIds.clear();

		for(int i = 0; i < nbtList.size(); i++)
			researchIds.add(ResourceLocation.parse(nbtList.getString(i)));
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider provider) {
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
				DevotionComponents.KNOWN_RESEARCH.sync(player);
			}

			return true;
		}

		return false;
	}

	public boolean revokeResearch(Research research, boolean simulate) {
		if(researchIds.contains(research.getId())) {
			if(!simulate) {
				researchIds.remove(research.getId());
				DevotionComponents.KNOWN_RESEARCH.sync(player);
			}

			return true;
		}

		return false;
	}
}
