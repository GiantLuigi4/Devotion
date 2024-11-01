package dev.cammiescorner.devotion.neoforge.mixin;

import dev.cammiescorner.devotion.common.Color;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.AuraCapability;
import dev.cammiescorner.devotion.neoforge.entrypoints.NeoMain;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> entityType, Level level) { super(entityType, level); }

	@Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
	private void addCapabilityData(CompoundTag compound, CallbackInfo info) {
		AuraCapability capability = getCapability(NeoMain.AURA);

		if(capability != null) {
			compound.putFloat("Aura", capability.getAura());
			compound.putInt("AuraColor", capability.getAuraColor().getDecimal());
		}
	}

	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	private void readCapabilityData(CompoundTag compound, CallbackInfo info) {
		AuraCapability capability = getCapability(NeoMain.AURA);

		if(capability != null) {
			capability.setAura(compound.getFloat("Aura"), false);
			capability.setAuraColor(new Color(compound.getInt("AuraColor")), false);
		}
	}
}
