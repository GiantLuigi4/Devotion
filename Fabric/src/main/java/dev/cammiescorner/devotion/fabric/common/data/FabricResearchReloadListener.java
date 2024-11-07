package dev.cammiescorner.devotion.fabric.common.data;

import dev.cammiescorner.devotion.Devotion;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FabricResearchReloadListener extends dev.cammiescorner.devotion.common.data.ResearchReloadListener implements IdentifiableResourceReloadListener {
	@Override
	public ResourceLocation getFabricId() {
		return Devotion.id("research");
	}
}
