package dev.cammiescorner.devotion.fabric.entrypoints;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cammiescorner.devotion.Devotion;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

@CalledByReflection
public class FabricClient implements ClientModInitializer {
	public static Function<ResourceLocation, RenderType> auraRenderType;

	@Override
	public void onInitializeClient() {
		CoreShaderRegistrationCallback.EVENT.register(context -> context.register(Devotion.id("rendertype_aura"), DefaultVertexFormat.POSITION_TEX_COLOR, shaderInstance -> {
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
		}));
	}
}
