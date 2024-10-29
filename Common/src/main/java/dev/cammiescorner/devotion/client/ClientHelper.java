package dev.cammiescorner.devotion.client;

import dev.cammiescorner.devotion.DevotionClient;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.upcraft.sparkweave.api.platform.Services;

public class ClientHelper {
	private static final ClientDuck duck = Services.getService(ClientDuck.class);

	public StructureMapData getStructureMapData() {
		return DevotionClient.data;
	}
}
