package dev.cammiescorner.devotion.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.DevotionClient;
import dev.cammiescorner.devotion.DevotionConfig;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.util.function.Function;

public class AuraEffectManager {
	public static final AuraEffectManager INSTANCE = new AuraEffectManager();
	public static final RenderStateShard.OutputStateShard AURA_OUTPUT = new RenderStateShard.OutputStateShard("devotion:aura_output", AuraEffectManager.INSTANCE::startAuraRenderTarget, AuraEffectManager.INSTANCE::stopAuraRenderTarget);
	public PostChain postEffect;
	public RenderTarget auraRenderTarget;
	public Function<ResourceLocation, RenderType> auraRenderType;
	public ShaderInstance auraShader;
	public boolean auraBufferCleared;

	public void startAuraRenderTarget() {
		if(auraRenderTarget != null) {
			auraRenderTarget.bindWrite(false);

			if(!auraBufferCleared) {
				float[] clearChannels = auraRenderTarget.clearChannels;
				RenderSystem.clearColor(clearChannels[0], clearChannels[1], clearChannels[2], clearChannels[3]);
				RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT, Minecraft.ON_OSX);

				auraBufferCleared = true;
			}
		}
	}

	public void stopAuraRenderTarget() {
		DevotionClient.client.getMainRenderTarget().bindWrite(false);
	}

	public void renderShader(float partialTicks) {
		if(auraBufferCleared) {
			postEffect.setUniform("TransStepGranularity", DevotionConfig.Client.auraGradiant);
			postEffect.setUniform("BlobsStepGranularity", DevotionConfig.Client.auraSharpness);
			postEffect.process(partialTicks);
			DevotionClient.client.getMainRenderTarget().bindWrite(true);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			auraRenderTarget.blitToScreen(DevotionClient.client.getWindow().getWidth(), DevotionClient.client.getWindow().getHeight(), false);
			RenderSystem.disableBlend();
		}
	}

	public void assignDepthTexture() {
		DevotionClient.client.getMainRenderTarget().bindWrite(false);
		int depthTexturePtr = DevotionClient.client.getMainRenderTarget().getDepthTextureId();

		if(depthTexturePtr != -1) {
			auraRenderTarget.bindWrite(false);
			GlStateManager._glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_TEXTURE_2D, depthTexturePtr, 0);
		}
	}

	public void initCoreShader(ResourceProvider provider) throws IOException {
		if(auraShader != null) {
			try {
				auraShader.close();
				auraShader = null;
			}
			catch(Exception e) {
				throw new RuntimeException("Failed to release core aura shader", e);
			}
		}

		auraShader = new ShaderInstance(provider, Devotion.id("rendertype_aura").toString(), DefaultVertexFormat.POSITION_TEX_COLOR);
	}

	public void initPostShader() throws IOException {
		if(postEffect != null) {
			try {
				postEffect.close();
				postEffect = null;
			}
			catch(Exception e) {
				throw new RuntimeException("Failed to release post aura shader", e);
			}
		}

		postEffect = new PostChain(DevotionClient.client.getTextureManager(), DevotionClient.client.getResourceManager(), DevotionClient.client.getMainRenderTarget(), Devotion.id("shaders/post/aura.json"));
		postEffect.resize(DevotionClient.client.getWindow().getWidth(), DevotionClient.client.getWindow().getHeight());
		postEffect.addTempTarget("devotion:aura_render_target", DevotionClient.client.getWindow().getWidth(), DevotionClient.client.getWindow().getHeight());
		auraRenderTarget = postEffect.getTempTarget("devotion:aura_render_target");
		assignDepthTexture();
	}

	public void initShaderInstance(ShaderInstance shaderInstance) {
		RenderStateShard.ShaderStateShard auraShaderState = new RenderStateShard.ShaderStateShard(() -> shaderInstance);
		auraShader = shaderInstance;

		auraRenderType = Util.memoize(texture -> RenderType.create(
			"devotion:aura",
			DefaultVertexFormat.POSITION_TEX_COLOR,
			VertexFormat.Mode.QUADS,
			256,
			false,
			true,
			RenderType.CompositeState.builder()
				.setShaderState(auraShaderState)
				.setWriteMaskState(RenderStateShard.COLOR_WRITE)
				.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
				.setOutputState(AURA_OUTPUT)
				.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
				.createCompositeState(false)
		));
	}
}
