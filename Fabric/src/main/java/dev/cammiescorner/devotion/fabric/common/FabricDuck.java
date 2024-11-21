package dev.cammiescorner.devotion.fabric.common;

import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.Duck;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class FabricDuck implements Duck {
	@Override
	public float getAura(LivingEntity entity, AuraType auraType) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getAura(auraType) : 0f;
	}

	@Override
	public void setAura(LivingEntity entity, AuraType auraType, float amount) {
		if(DevotionComponents.AURA.isProvidedBy(entity))
			entity.getComponent(DevotionComponents.AURA).setAura(auraType, amount);
	}

	@Override
	public AuraType getPrimaryAuraType(LivingEntity entity) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getPrimaryAuraType() : AuraType.NONE;
	}

	@Override
	public void setPrimaryAuraType(LivingEntity entity, AuraType primaryAuraType) {
		if(DevotionComponents.AURA.isProvidedBy(entity))
			entity.getComponent(DevotionComponents.AURA).setPrimaryAuraType(primaryAuraType);
	}

	@Override
	public float getAuraAlpha(LivingEntity entity, AuraType auraType) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getAuraAlpha() : 1f;
	}

	@Override
	public boolean drainAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		return DevotionComponents.AURA.isProvidedBy(entity) && entity.getComponent(DevotionComponents.AURA).drainAura(auraType, amount, simulate);
	}

	@Override
	public boolean regenAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		return DevotionComponents.AURA.isProvidedBy(entity) && entity.getComponent(DevotionComponents.AURA).regenAura(auraType, amount, simulate);
	}

	@Override
	public Set<ResourceLocation> getResearchIds(Player player) {
		return player.getComponent(DevotionComponents.KNOWN_RESEARCH).getResearchIds();
	}

	@Override
	public boolean giveResearch(Player player, Research research, boolean simulate) {
		return player.getComponent(DevotionComponents.KNOWN_RESEARCH).giveResearch(research, simulate);
	}

	@Override
	public boolean revokeResearch(Player player, Research research, boolean simulate) {
		return player.getComponent(DevotionComponents.KNOWN_RESEARCH).revokeResearch(research, simulate);
	}
}
