package dev.cammiescorner.devotion.fabric.entrypoints;

import dev.cammiescorner.devotion.fabric.common.data.FabricResearchReloadListener;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

@CalledByReflection
public class FabricMain implements ModInitializer {

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FabricResearchReloadListener());
	}
}
