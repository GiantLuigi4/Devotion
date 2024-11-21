package dev.cammiescorner.devotion.neoforge.common;

import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.Duck;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.AuraCapability;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.KnownResearchCapability;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class NeoDuck implements Duck {
	@Override
	public float getAura(LivingEntity entity, AuraType auraType) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability ? capability.getAura(auraType) : 0f;
	}

	@Override
	public void setAura(LivingEntity entity, AuraType auraType, float amount) {
		if(entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability)
			capability.setAura(auraType, amount);
	}

	@Override
	public AuraType getPrimaryAuraType(LivingEntity entity) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability ? capability.getPrimaryAuraType() : AuraType.NONE;
	}

	@Override
	public void setPrimaryAuraType(LivingEntity entity, AuraType primaryAuraType) {
		if(entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability)
			capability.setPrimaryAuraType(primaryAuraType);
	}

	@Override
	public float getAuraAlpha(LivingEntity entity, AuraType auraType) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability ? capability.getAuraAlpha() : 1f;
	}

	@Override
	public boolean drainAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability && capability.drainAura(auraType, amount, simulate);
	}

	@Override
	public boolean regenAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		return entity.getCapability(NeoMain.AURA) instanceof AuraCapability capability && capability.regenAura(auraType, amount, simulate);
	}

	@Override
	public Set<ResourceLocation> getResearchIds(Player player) {
		return player.getCapability(NeoMain.KNOWN_RESEARCH) instanceof KnownResearchCapability capability ? capability.getResearchIds() : Set.of();
	}

	@Override
	public boolean giveResearch(Player player, Research research, boolean simulate) {
		return player.getCapability(NeoMain.KNOWN_RESEARCH) instanceof KnownResearchCapability capability && capability.giveResearch(research, simulate);
	}

	@Override
	public boolean revokeResearch(Player player, Research research, boolean simulate) {
		return player.getCapability(NeoMain.KNOWN_RESEARCH) instanceof KnownResearchCapability capability && capability.revokeResearch(research, simulate);
	}
}
