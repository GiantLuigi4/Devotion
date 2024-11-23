package dev.cammiescorner.devotion.neoforge.common;

import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.Duck;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.AuraAttachment;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.KnownResearchAttachment;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

public class NeoDuck implements Duck {
	@Override
	public float getAura(LivingEntity entity, AuraType auraType) {
		return AuraAttachment.isAuraProvider(entity) ? entity.getData(NeoMain.AURA).getAura(auraType) : 0f;
	}

	@Override
	public void setAura(LivingEntity entity, AuraType auraType, float amount) {
		if(!AuraAttachment.isAuraProvider(entity))
			return;

		entity.getData(NeoMain.AURA).setAura(auraType, amount);
		AuraAttachment.sync(entity);
	}

	@Override
	public AuraType getPrimaryAuraType(LivingEntity entity) {
		return AuraAttachment.isAuraProvider(entity) ? entity.getData(NeoMain.AURA).getPrimaryAuraType() : AuraType.NONE;
	}

	@Override
	public void setPrimaryAuraType(LivingEntity entity, AuraType primaryAuraType) {
		if(!AuraAttachment.isAuraProvider(entity))
			return;

		entity.getData(NeoMain.AURA).setPrimaryAuraType(primaryAuraType);
		AuraAttachment.sync(entity);
	}

	@Override
	public float getAuraAlpha(LivingEntity entity, AuraType auraType) {
		return AuraAttachment.isAuraProvider(entity) ? entity.getData(NeoMain.AURA).getAuraAlpha() : 1f;
	}

	@Override
	public boolean drainAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		float currentAura = getAura(entity, auraType);

		if(currentAura - amount >= 0) {
			if(!simulate)
				setAura(entity, auraType, currentAura - amount);

			return true;
		}

		return false;
	}

	@Override
	public boolean regenAura(LivingEntity entity, AuraType auraType, float amount, boolean simulate) {
		float currentAura = getAura(entity, auraType);

		if(currentAura < AuraAttachment.MAX_AURA) {
			if(!simulate)
				setAura(entity, auraType, currentAura + amount);

			return true;
		}

		return false;
	}

	@Override
	public Set<ResourceLocation> getResearchIds(Player player) {
		return player.getData(NeoMain.KNOWN_RESEARCH).getResearchIds();
	}

	@Override
	public boolean giveResearch(Player player, Research research, boolean simulate) {
		ResourceLocation researchId = research.getId(player.level().registryAccess());

		if(!getResearchIds(player).contains(researchId)) {
			if(!simulate) {
				player.getData(NeoMain.KNOWN_RESEARCH).addResearch(researchId);
				KnownResearchAttachment.sync(player);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean revokeResearch(Player player, Research research, boolean simulate) {
		ResourceLocation researchId = research.getId(player.level().registryAccess());

		if(getResearchIds(player).contains(researchId)) {
			if(!simulate) {
				player.getData(NeoMain.KNOWN_RESEARCH).revokeResearch(researchId);
				KnownResearchAttachment.sync(player);
			}

			return true;
		}

		return false;
	}
}
