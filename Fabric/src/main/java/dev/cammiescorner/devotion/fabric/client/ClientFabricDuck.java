package dev.cammiescorner.devotion.fabric.client;

import dev.cammiescorner.devotion.client.ClientDuck;
import dev.cammiescorner.devotion.fabric.entrypoints.FabricClient;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ClientFabricDuck implements ClientDuck {
	@Override
	public RenderType auraRenderType(ResourceLocation texture) {
		return FabricClient.auraRenderType.apply(texture);
	}
}
