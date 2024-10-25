package dev.cammiescorner.devotion.neoforge.common;

import dev.cammiescorner.devotion.common.Duck;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.devotion.neoforge.common.capabilities.AltarStructureCapability;
import net.minecraft.server.MinecraftServer;

public class NeoDuck implements Duck {
	@Override
	public void setStructureMap(MinecraftServer server, StructureMapData data) {
		AltarStructureCapability.getInstance(server).setStructureMap(data);
	}
}
