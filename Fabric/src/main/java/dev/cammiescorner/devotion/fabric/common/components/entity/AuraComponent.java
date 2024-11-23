package dev.cammiescorner.devotion.fabric.common.components.entity;

import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.HashMap;
import java.util.Map;

public class AuraComponent implements AutoSyncedComponent {
	public static final float MAX_AURA = 100;
	private final LivingEntity entity;
	private final Map<AuraType, Float> aura = new HashMap<>();
	private AuraType primaryAuraType;

	public AuraComponent(LivingEntity entity) {
		this.entity = entity;
		this.primaryAuraType = AuraType.NONE; // TODO make primaryAuraType random on first spawn

		for(AuraType auraType : AuraType.values())
			aura.put(auraType, MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType));
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		aura.clear();

		ListTag listTag = tag.getList("AuraMap", Tag.TAG_COMPOUND);

		for(int i = 0; i < listTag.size(); i++) {
			CompoundTag compoundTag = listTag.getCompound(i);

			aura.put(AuraType.byName(compoundTag.getString("AuraType")), compoundTag.getFloat("AuraAmount"));
		}

		primaryAuraType = AuraType.byName(tag.getString("PrimaryAuraType"));
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		ListTag listTag = new ListTag();

		for(Map.Entry<AuraType, Float> entry : aura.entrySet()) {
			CompoundTag compoundTag = new CompoundTag();

			compoundTag.putString("AuraType", entry.getKey().getSerializedName());
			compoundTag.putFloat("AuraAmount", entry.getValue());
			listTag.add(compoundTag);
		}

		tag.put("AuraMap", listTag);
		tag.putString("PrimaryAuraType", primaryAuraType.getSerializedName());
	}

	public Map<AuraType, Float> getAllAura() {
		return Map.copyOf(aura);
	}

	public float getAura(AuraType auraType) {
		return aura.get(auraType);
	}

	public void setAura(AuraType auraType, float amount) {
		aura.put(auraType, Mth.clamp(amount, 0, MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType)));
		entity.syncComponent(DevotionComponents.AURA);
	}

	public AuraType getPrimaryAuraType() {
		return primaryAuraType;
	}

	public void setPrimaryAuraType(AuraType primaryAuraType) {
		this.primaryAuraType = primaryAuraType;
		entity.syncComponent(DevotionComponents.AURA);
	}

	public float getAuraAlpha() {
		return aura.get(primaryAuraType) / MAX_AURA;
	}

	public boolean regenAura(AuraType auraType, float amount, boolean simulate) {
		float currentAura = getAura(auraType);

		if(currentAura < MAX_AURA) {
			if(!simulate)
				setAura(auraType, currentAura + amount);

			return true;
		}

		return false;
	}

	public boolean drainAura(AuraType auraType, float amount, boolean simulate) {
		float currentAura = getAura(auraType);

		if(currentAura - amount >= 0) {
			if(!simulate)
				setAura(auraType, currentAura - amount);

			return true;
		}

		return false;
	}
}
