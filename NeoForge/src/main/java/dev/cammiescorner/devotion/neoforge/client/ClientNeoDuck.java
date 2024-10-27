package dev.cammiescorner.devotion.neoforge.client;

import dev.cammiescorner.devotion.client.ClientDuck;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ClientNeoDuck implements ClientDuck {
	@Override
	public RenderType auraRenderType(ResourceLocation texture) {
		return ClientEventBus.auraRenderType.apply(texture);
	}
}
