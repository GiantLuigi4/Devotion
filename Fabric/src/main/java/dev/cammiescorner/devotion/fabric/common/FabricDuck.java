package dev.cammiescorner.devotion.fabric.common;

import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.common.Duck;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;

public class FabricDuck implements Duck {
	@Override
	public void setStructureMap(MinecraftServer server, StructureMapData data) {
		server.getScoreboard().getComponent(DevotionComponents.ALTAR_STRUCTURE).setStructureMap(data);
	}

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
}
