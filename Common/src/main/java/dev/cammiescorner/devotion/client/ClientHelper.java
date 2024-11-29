package dev.cammiescorner.devotion.client;

import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.resources.ResourceLocation;

public class ClientHelper {
	public static final ResourceLocation WHITE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/misc/white.png");
	private static final ClientDuck duck = Services.getService(ClientDuck.class);
}
