package dev.cammiescorner.devotion.client;

import dev.cammiescorner.devotion.DevotionClient;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ClientHelper {
	private static final ClientDuck duck = Services.getService(ClientDuck.class);
	private static final ResourceLocation WHITE_TEXTURE = ResourceLocation.withDefaultNamespace("misc/white.png");

	public StructureMapData getStructureMapData() {
		return DevotionClient.data;
	}

	public static RenderType auraRenderType(ResourceLocation texture) {
		return AuraEffectManager.INSTANCE.auraRenderType.apply(texture);
	}

	public static RenderType auraRenderType(RenderType base) {
		if(base instanceof RenderType.CompositeRenderType compositeType)
			return auraRenderType(compositeType.state().textureState.cutoutTexture().orElse(WHITE_TEXTURE));
		else
			return auraRenderType(WHITE_TEXTURE);
	}
}
