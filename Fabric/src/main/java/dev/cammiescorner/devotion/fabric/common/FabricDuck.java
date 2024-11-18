package dev.cammiescorner.devotion.fabric.common;

import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.common.Duck;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class FabricDuck implements Duck {
	@Override
	public float getAura(LivingEntity entity) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getAura() : 0f;
	}

	@Override
	public void setAura(LivingEntity entity, float amount) {
		if(DevotionComponents.AURA.isProvidedBy(entity))
			entity.getComponent(DevotionComponents.AURA).setAura(amount);
	}

	@Override
	public Color getAuraColor(LivingEntity entity) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getAuraColor() : new Color(0xffffff);
	}

	@Override
	public void setAuraColor(LivingEntity entity, Color color) {
		if(DevotionComponents.AURA.isProvidedBy(entity))
			entity.getComponent(DevotionComponents.AURA).setAuraColor(color);
	}

	@Override
	public float getAuraAlpha(LivingEntity entity) {
		return DevotionComponents.AURA.isProvidedBy(entity) ? entity.getComponent(DevotionComponents.AURA).getAuraAlpha() : 1f;
	}

	@Override
	public boolean drainAura(LivingEntity entity, float amount, boolean simulate) {
		return DevotionComponents.AURA.isProvidedBy(entity) && entity.getComponent(DevotionComponents.AURA).drainAura(amount, simulate);
	}

	@Override
	public boolean regenAura(LivingEntity entity, float amount, boolean simulate) {
		return DevotionComponents.AURA.isProvidedBy(entity) && entity.getComponent(DevotionComponents.AURA).regenAura(amount, simulate);
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
