package dev.cammiescorner.devotion;

import dev.cammiescorner.devotion.common.StructureMapData;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.client.Minecraft;

public class DevotionClient implements ClientEntryPoint {
	public static final Minecraft client = Minecraft.getInstance();
	public static StructureMapData data = StructureMapData.empty();

	@Override
	public void onInitializeClient(ModContainer mod) {
	}
}
