package dev.cammiescorner.devotion.neoforge.common.capabilities.entity;

import commonnetwork.api.Network;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.networking.clientbound.ClientboundAuraPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class AuraCapability implements INBTSerializable<CompoundTag> {
	public static final float MAX_AURA = 100;
	private final LivingEntity entity;
	private final Map<AuraType, Float> aura = new HashMap<>();
	private AuraType primaryAuraType;

	public AuraCapability(LivingEntity entity) {
		this.entity = entity;
		this.primaryAuraType = AuraType.NONE; // TODO make primaryAuraType random on first spawn

		for(AuraType auraType : AuraType.values())
			aura.put(auraType, MAX_AURA * auraType.getAffinityMultiplier(primaryAuraType));
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
		aura.clear();

		ListTag listTag = tag.getList("AuraMap", Tag.TAG_COMPOUND);

		for(int i = 0; i < listTag.size(); i++) {
			CompoundTag compoundTag = listTag.getCompound(i);

			aura.put(AuraType.byName(compoundTag.getString("AuraType")), compoundTag.getFloat("AuraAmount"));
		}

		primaryAuraType = AuraType.byName(tag.getString("PrimaryAuraType"));
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider) {
		CompoundTag tag = new CompoundTag();
		ListTag listTag = new ListTag();

		for(Map.Entry<AuraType, Float> entry : aura.entrySet()) {
			CompoundTag compoundTag = new CompoundTag();

			compoundTag.putString("AuraType", entry.getKey().getSerializedName());
			compoundTag.putFloat("AuraAmount", entry.getValue());
			listTag.add(compoundTag);
		}

		tag.put("AuraMap", listTag);
		tag.putString("PrimaryAuraType", primaryAuraType.getSerializedName());

		return tag;
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
			Network.getNetworkHandler().sendToClientsLoadingPos(new ClientboundAuraPacket(entity.getId(), aura, primaryAuraType), level, entity.blockPosition());
	}

	public AuraType getPrimaryAuraType() {
		return primaryAuraType;
	}

	public void setPrimaryAuraType(AuraType primaryAuraType) {
		setPrimaryAuraType(primaryAuraType, true);
	}

	public void setPrimaryAuraType(AuraType primaryAuraType, boolean sync) {
		this.primaryAuraType = primaryAuraType;

		System.out.println("Side: " + (entity.level().isClientSide() ? "CLIENT" : "SERVER"));
		System.out.println("From Capability: " + getPrimaryAuraType());

		if(sync && entity.level() instanceof ServerLevel level)
			Network.getNetworkHandler().sendToClientsLoadingPos(new ClientboundAuraPacket(entity.getId(), aura, primaryAuraType), level, entity.blockPosition());
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
