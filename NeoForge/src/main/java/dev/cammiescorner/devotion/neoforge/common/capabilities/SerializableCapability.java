package dev.cammiescorner.devotion.neoforge.common.capabilities;

import net.minecraft.nbt.CompoundTag;

public interface SerializableCapability {
	void readFromNbt(CompoundTag tag);

	void writeToNbt(CompoundTag tag);
}
