package dev.cammiescorner.devotion.neoforge.common.capabilities.entity;

import commonnetwork.api.Network;
import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.common.networking.s2c.ClientboundDataPacket;
import dev.cammiescorner.devotion.neoforge.common.capabilities.SyncedCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class AuraCapability implements SyncedCapability {
	public static final float MAX_AURA = 100;
	private final LivingEntity entity;
	private float aura;
	private int auraColor;

	public AuraCapability(LivingEntity entity, float aura, int auraColor) {
		this.entity = entity;
		this.aura = aura;
		this.auraColor = auraColor;
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		aura = tag.getFloat("Aura");
		auraColor = tag.getInt("AuraColor");
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		tag.putFloat("Aura", aura);
		tag.putInt("AuraColor", auraColor);
	}

	public float getAura() {
		return aura;
	}

	public void setAura(float amount) {
		setAura(amount, true);
	}

	public void setAura(float amount, boolean sync) {
		aura = Mth.clamp(amount, 0, MAX_AURA);

		if(sync && entity.level() instanceof ServerLevel level)
			Network.getNetworkHandler().sendToClientsInRange(new ClientboundDataPacket(entity.getId(), aura, auraColor), level, entity.blockPosition(), (double) entity.getType().clientTrackingRange() * 16);
	}

	public Color getAuraColor() {
		return new Color(auraColor);
	}

	public void setAuraColor(Color color) {
		setAuraColor(color, true);
	}

	public void setAuraColor(Color color, boolean sync) {
		auraColor = color.getDecimal();

		if(sync && entity.level() instanceof ServerLevel level)
			Network.getNetworkHandler().sendToClientsInRange(new ClientboundDataPacket(entity.getId(), aura, auraColor), level, entity.blockPosition(), (double) entity.getType().clientTrackingRange() * 16);
	}

	public float getAuraAlpha() {
		return aura / MAX_AURA;
	}

	public boolean drainAura(float amount, boolean simulate) {
		if(aura - amount >= 0) {
			if(!simulate)
				setAura(getAura() - amount);

			return true;
		}

		return false;
	}

	public boolean regenAura(float amount, boolean simulate) {
		if(amount < MAX_AURA) {
			if(!simulate)
				setAura(getAura() + amount);

			return true;
		}

		return false;
	}
}
