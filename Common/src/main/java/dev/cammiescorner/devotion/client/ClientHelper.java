package dev.cammiescorner.devotion.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cammiescorner.devotion.DevotionClient;
import dev.cammiescorner.devotion.common.StructureMapData;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;

import java.io.IOException;

public class ClientHelper {
//	private static final ClientDuck duck = Services.getService(ClientDuck.class);

	public StructureMapData getStructureMapData() {
		return DevotionClient.data;
	}

	public static ShaderInstance createShaderInstance(ResourceProvider resourceProvider, ResourceLocation id, VertexFormat vertexFormat) {
		try {
			return new ShaderInstance(resourceProvider, id.toString(), vertexFormat);
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
