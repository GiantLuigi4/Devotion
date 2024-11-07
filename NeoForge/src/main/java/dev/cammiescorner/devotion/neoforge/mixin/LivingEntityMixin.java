package dev.cammiescorner.devotion.neoforge.mixin;

import dev.cammiescorner.devotion.neoforge.common.capabilities.SerializableCapability;
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
		if(getCapability(NeoMain.AURA) instanceof SerializableCapability capability)
			capability.writeToNbt(compound);

		if(getCapability(NeoMain.KNOWN_RESEARCH) instanceof SerializableCapability capability)
			capability.writeToNbt(compound);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	private void readCapabilityData(CompoundTag compound, CallbackInfo info) {
		if(getCapability(NeoMain.AURA) instanceof SerializableCapability capability)
			capability.readFromNbt(compound);

		if(getCapability(NeoMain.KNOWN_RESEARCH) instanceof SerializableCapability capability)
			capability.readFromNbt(compound);
	}
}
