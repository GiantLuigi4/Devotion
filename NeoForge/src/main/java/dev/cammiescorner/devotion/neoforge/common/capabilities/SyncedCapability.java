package dev.cammiescorner.devotion.neoforge.common.capabilities;

import net.minecraft.nbt.CompoundTag;

public interface SyncedCapability {
	void readFromNbt(CompoundTag tag);

	void writeToNbt(CompoundTag tag);

	default void sync() {
	}
}
