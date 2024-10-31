package dev.cammiescorner.devotion.client.renderers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.client.AuraEffectManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class AuraVertexBufferSource implements MultiBufferSource {
	private final MultiBufferSource bufferSource;
	private final int color;

	public AuraVertexBufferSource(MultiBufferSource bufferSource, int red, int green, int blue, int alpha) {
		this.bufferSource = bufferSource;
		this.color = FastColor.ARGB32.color(alpha, red, green, blue);
	}

	@Override
	public VertexConsumer getBuffer(RenderType renderType) {
		if(renderType.outline().isPresent())
			return new AuraVertexConsumer(bufferSource.getBuffer(AuraEffectManager.getRenderType(renderType)), color);
		else
			return new DummyVertexConsumer();
	}

	public VertexConsumer getBuffer(ResourceLocation texture) {
		return new AuraVertexConsumer(bufferSource.getBuffer(AuraEffectManager.getRenderType(texture)), color);
	}

	public VertexConsumer getBuffer() {
		return new AuraVertexConsumer(bufferSource.getBuffer(AuraEffectManager.getRenderType()), color);
	}

	public static class AuraVertexConsumer implements VertexConsumer {
		private final VertexConsumer delegate;
		private final int color;

		public AuraVertexConsumer(VertexConsumer delegate, int color) {
			this.delegate = delegate;
			this.color = color;
		}

		public AuraVertexConsumer(VertexConsumer delegate, int red, int green, int blue, int alpha) {
			this(delegate, FastColor.ARGB32.color(alpha, red, green, blue));
		}

		@Override
		public VertexConsumer addVertex(float x, float y, float z) {
			delegate.addVertex(x, y, z).setColor(color);
			return this;
		}

		@Override
		public VertexConsumer setColor(int red, int green, int blue, int alpha) {
			return this;
		}

		@Override
		public VertexConsumer setUv(float u, float v) {
			delegate.setUv(u, v);
			return this;
		}

		@Override
		public VertexConsumer setUv1(int u, int v) {
			return this;
		}

		@Override
		public VertexConsumer setUv2(int u, int v) {
			return this;
		}

		@Override
		public VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
			return this;
		}
	}

	public static class DummyVertexConsumer implements VertexConsumer {
		@Override
		public VertexConsumer addVertex(float x, float y, float z) { return this; }

		@Override
		public VertexConsumer setColor(int red, int green, int blue, int alpha) { return this; }

		@Override
		public VertexConsumer setUv(float u, float v) { return this; }

		@Override
		public VertexConsumer setUv1(int u, int v) { return this; }

		@Override
		public VertexConsumer setUv2(int u, int v) { return this; }

		@Override
		public VertexConsumer setNormal(float normalX, float normalY, float normalZ) { return this; }
	}
}
