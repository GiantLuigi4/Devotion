package dev.cammiescorner.devotion.fabric.common.components.entity;

import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class AuraComponent implements AutoSyncedComponent {
	public static final float MAX_AURA = 100;
	private final LivingEntity entity;
	private float aura;
	private int auraColor;

	public AuraComponent(LivingEntity entity) {
		this.entity = entity;
		this.aura = MAX_AURA;
		this.auraColor = 0xffffff;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		aura = tag.getFloat("Aura");
		auraColor = tag.getInt("AuraColor");
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		tag.putFloat("Aura", aura);
		tag.putInt("AuraColor", auraColor);
	}

	public float getAura() {
		return aura;
	}

	public void setAura(float amount) {
		aura = Mth.clamp(amount, 0, MAX_AURA);
		entity.syncComponent(DevotionComponents.AURA);
	}

	public Color getAuraColor() {
		return new Color(auraColor);
	}

	public void setAuraColor(Color color) {
		this.auraColor = color.getDecimal();
		entity.syncComponent(DevotionComponents.AURA);
	}

	public float getAuraAlpha() {
		return aura / MAX_AURA;
	}

	public boolean drainAura(float amount, boolean simulate) {
		if(aura - amount >= 0) {
			if(!simulate)
				setAura(aura - amount);

			return true;
		}

		return false;
	}

	public boolean regenAura(float amount, boolean simulate) {
		if(aura < MAX_AURA) {
			if(!simulate)
				setAura(aura + amount);

			return true;
		}

		return false;
	}
}
