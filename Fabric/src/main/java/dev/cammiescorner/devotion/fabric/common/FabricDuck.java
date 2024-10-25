package dev.cammiescorner.devotion.fabric.common;

import dev.cammiescorner.devotion.common.Duck;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.minecraft.server.MinecraftServer;

public class FabricDuck implements Duck {
	@Override
	public void setStructureMap(MinecraftServer server, StructureMapData data) {
		server.getScoreboard().getComponent(DevotionComponents.ALTAR_STRUCTURE_COMPONENT).setStructureMap(data);
	}
}
