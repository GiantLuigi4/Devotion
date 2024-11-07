package dev.cammiescorner.devotion.common;

import dev.cammiescorner.devotion.api.research.Research;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public interface Duck {
	void setStructureMap(MinecraftServer server, StructureMapData data);

	float getAura(LivingEntity entity);

	void setAura(LivingEntity entity, float amount);

	Color getAuraColor(LivingEntity entity);

	void setAuraColor(LivingEntity entity, Color color);

	float getAuraAlpha(LivingEntity entity);

	boolean drainAura(LivingEntity entity, float amount, boolean simulate);

	boolean regenAura(LivingEntity entity, float amount, boolean simulate);

	Set<ResourceLocation> getResearchIds(Player player);

	boolean giveResearch(Player player, Research research, boolean simulate);

	boolean revokeResearch(Player player, Research research, boolean simulate);
}
