package dev.cammiescorner.devotion.common;

import dev.cammiescorner.devotion.api.research.Research;
import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class MainHelper {
	private static final Duck duck = Services.getService(Duck.class);

	public static float getAura(LivingEntity entity) {
		return duck.getAura(entity);
	}

	public static void setAura(LivingEntity entity, float amount) {
		duck.setAura(entity, amount);
	}

	public static Color getAuraColor(LivingEntity entity) {
		return duck.getAuraColor(entity);
	}

	public static void setAuraColor(LivingEntity entity, Color color) {
		duck.setAuraColor(entity, color);
	}

	public static float getAuraAlpha(LivingEntity entity) {
		return duck.getAuraAlpha(entity);
	}

	public static boolean drainAura(LivingEntity entity, float amount, boolean simulate) {
		return duck.drainAura(entity, amount, simulate);
	}

	public static boolean regenAura(LivingEntity entity, float amount, boolean simulate) {
		return duck.regenAura(entity, amount, simulate);
	}

	public static Set<ResourceLocation> getResearchIds(Player player) {
		return duck.getResearchIds(player);
	}

	public static boolean giveResearch(Player player, Research research, boolean simulate) {
		return duck.giveResearch(player, research, simulate);
	}

	public static boolean revokeResearch(Player player, Research research, boolean simulate) {
		return duck.revokeResearch(player, research, simulate);
	}
}
