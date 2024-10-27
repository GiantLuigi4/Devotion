package dev.cammiescorner.devotion.neoforge.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cammiescorner.devotion.Devotion;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.function.Function;

@EventBusSubscriber
public class ClientEventBus {
	public static Function<ResourceLocation, RenderType> auraRenderType;

	@SubscribeEvent
	public void registerShaders(RegisterShadersEvent event) throws IOException {
		event.registerShader(new ShaderInstance(event.getResourceProvider(), Devotion.id("rendertype_aura"), DefaultVertexFormat.POSITION_TEX_COLOR), shaderInstance -> {
			RenderStateShard.ShaderStateShard auraShaderState = new RenderStateShard.ShaderStateShard(() -> shaderInstance);
			auraRenderType = Util.memoize(texture -> RenderType.create(
				"devotion:aura",
				DefaultVertexFormat.POSITION_TEX_COLOR,
				VertexFormat.Mode.QUADS,
				1536,
				RenderType.CompositeState.builder()
					.setShaderState(auraShaderState)
					.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
					.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
					.setOutputState(RenderStateShard.OUTLINE_TARGET)
					.createCompositeState(RenderType.OutlineProperty.IS_OUTLINE)
			));
		});
	}
}
