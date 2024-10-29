package dev.cammiescorner.devotion.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.client.AuraEffectManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class AuraVertexBufferSource implements MultiBufferSource {
	private final MultiBufferSource bufferSource;
	private int color = 0xffffffff;

	public AuraVertexBufferSource(MultiBufferSource bufferSource) {
		this.bufferSource = bufferSource;
	}

	@Override
	public VertexConsumer getBuffer(RenderType renderType) {
		if(renderType.outline().isPresent())
			return new AuraVertexConsumer(bufferSource.getBuffer(AuraEffectManager.getRenderType(renderType)), color);
		else
			return new VertexConsumer() {
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

				@Override
				public void putBulkData(PoseStack.Pose pose, BakedQuad quad, float[] brightness, float red, float green, float blue, float alpha, int[] lightmap, int packedOverlay, boolean readAlpha) { }

				@Override
				public void putBulkData(PoseStack.Pose pose, BakedQuad quad, float red, float green, float blue, float alpha, int packedLight, int packedOverlay) { }
			};
	}

	public VertexConsumer getBuffer(ResourceLocation texture) {
		return new AuraVertexConsumer(bufferSource.getBuffer(AuraEffectManager.getRenderType(texture)), color);
	}

	public VertexConsumer getBuffer() {
		return new AuraVertexConsumer(bufferSource.getBuffer(AuraEffectManager.getRenderType()), color);
	}

	public void setColor(int color, int alpha) {
		this.color = FastColor.ARGB32.color(alpha, color);
	}

	public void setColor(int red, int green, int blue, int alpha) {
		setColor(FastColor.ARGB32.color(red, green, blue), alpha);
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
}
