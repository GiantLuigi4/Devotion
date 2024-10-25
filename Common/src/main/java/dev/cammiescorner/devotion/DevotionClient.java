package dev.cammiescorner.devotion;

import dev.cammiescorner.devotion.common.StructureMapData;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;

public class DevotionClient implements ClientEntryPoint {
	public static StructureMapData data = StructureMapData.empty();

	@Override
	public void onInitializeClient(ModContainer mod) {

	}
}
