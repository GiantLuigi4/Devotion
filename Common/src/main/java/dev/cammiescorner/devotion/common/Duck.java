package dev.cammiescorner.devotion.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;

public interface Duck {
	void setStructureMap(MinecraftServer server, StructureMapData data);

	float getAura(LivingEntity entity);

	void setAura(LivingEntity entity, float amount);

	Color getAuraColor(LivingEntity entity);

	void setAuraColor(LivingEntity entity, Color color);

	float getAuraAlpha(LivingEntity entity);

	boolean drainAura(LivingEntity entity, float amount, boolean simulate);

	boolean regenAura(LivingEntity entity, float amount, boolean simulate);
}
