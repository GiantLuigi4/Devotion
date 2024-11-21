package dev.cammiescorner.devotion.neoforge.common.capabilities.entity;

import commonnetwork.api.Network;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundAuraPacket;
import dev.cammiescorner.devotion.neoforge.common.capabilities.SyncedCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class AuraCapability implements SyncedCapability {
	public static final float MAX_AURA = 100;
	private final LivingEntity entity;
	private final Map<AuraType, Float> aura = new HashMap<>();
	private AuraType primaryAuraType;

	public AuraCapability(LivingEntity entity, AuraType primaryAuraType) {
		this.entity = entity;
		this.primaryAuraType = primaryAuraType;

		for(AuraType auraType : AuraType.values())
			aura.put(auraType, MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType));
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		aura.clear();

		ListTag listTag = tag.getList("AuraMap", Tag.TAG_COMPOUND);

		for(int i = 0; i < listTag.size(); i++) {
			CompoundTag compoundTag = listTag.getCompound(i);

			aura.put(AuraType.valueOf(compoundTag.getString("AuraType")), compoundTag.getFloat("AuraAmount"));
		}

		primaryAuraType = AuraType.valueOf(tag.getString("PrimaryAuraType"));
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
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

	public float getAura(AuraType auraType) {
		return aura.get(auraType);
	}

	public void setAura(AuraType auraType, float amount) {
		setAura(auraType, amount, true);
	}

	public void setAura(AuraType auraType, float amount, boolean sync) {
		aura.put(auraType, Mth.clamp(amount, 0, MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType)));

		if(sync && entity.level() instanceof ServerLevel level)
			Network.getNetworkHandler().sendToClientsInRange(new ClientboundAuraPacket(entity.getId(), aura, primaryAuraType), level, entity.blockPosition(), (double) entity.getType().clientTrackingRange() * 16);
	}

	public AuraType getPrimaryAuraType() {
		return primaryAuraType;
	}

	public void setPrimaryAuraType(AuraType primaryAuraType) {
		setPrimaryAuraType(primaryAuraType, true);
	}

	public void setPrimaryAuraType(AuraType primaryAuraType, boolean sync) {
		this.primaryAuraType = primaryAuraType;

		if(sync && entity.level() instanceof ServerLevel level)
			Network.getNetworkHandler().sendToClientsInRange(new ClientboundAuraPacket(entity.getId(), aura, primaryAuraType), level, entity.blockPosition(), (double) entity.getType().clientTrackingRange() * 16);
	}

	public float getAuraAlpha() {
		return aura.get(primaryAuraType) / MAX_AURA;
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

	public boolean regenAura(AuraType auraType, float amount, boolean simulate) {
		float currentAura = getAura(auraType);

		if(currentAura < MAX_AURA) {
			if(!simulate)
				setAura(auraType, currentAura + amount);

			return true;
		}

		return false;
	}
}
