package dev.cammiescorner.devotion.neoforge.common;

import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.common.Duck;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.AuraCapability;
import dev.cammiescorner.devotion.neoforge.common.capabilities.level.AltarStructureCapability;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;

public class NeoDuck implements Duck {
	@Override
	public void setStructureMap(MinecraftServer server, StructureMapData data) {
		AltarStructureCapability.getInstance(server).setStructureMap(data);
	}

	@Override
	public float getAura(LivingEntity entity) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability ? capability.getAura() : 0f;
	}

	@Override
	public void setAura(LivingEntity entity, float amount) {
		if(entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability)
			capability.setAura(amount);
	}

	@Override
	public Color getAuraColor(LivingEntity entity) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability ? capability.getAuraColor() : new Color(0xffffff);
	}

	@Override
	public void setAuraColor(LivingEntity entity, Color color) {
		if(entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability)
			capability.setAuraColor(color);
	}

	@Override
	public float getAuraAlpha(LivingEntity entity) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability ? capability.getAuraAlpha() : 1f;
	}

	@Override
	public boolean drainAura(LivingEntity entity, float amount, boolean simulate) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability && capability.drainAura(amount, simulate);
	}

	@Override
	public boolean regenAura(LivingEntity entity, float amount, boolean simulate) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability && capability.regenAura(amount, simulate);
	}
}
