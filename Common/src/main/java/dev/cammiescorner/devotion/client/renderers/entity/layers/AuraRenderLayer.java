package dev.cammiescorner.devotion.client.renderers.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.client.renderers.AuraVertexBufferSource;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class AuraRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final List<RenderLayer<T, M>> otherFeatureRenderers;

	public AuraRenderLayer(RenderLayerParent<T, M> renderer, List<RenderLayer<T, M>> otherFeatureRenderers) {
		super(renderer);
		this.otherFeatureRenderers = List.copyOf(otherFeatureRenderers);
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
		float aura = MainHelper.getAura(entity);
		float scale = 1f;

		if(/*aura > 0f*/ false) {
			EntityDimensions dimensions = entity.getDimensions(entity.getPose());
			AuraVertexBufferSource auraBufferSource = new AuraVertexBufferSource(bufferSource, 255, 255, 255, (int) (MainHelper.getAuraAlpha(entity) * 255));

			poseStack.pushPose();
			poseStack.scale(scale, scale, scale);

			for(RenderLayer<T, M> renderer : otherFeatureRenderers)
				renderer.render(poseStack, auraBufferSource, light, entity, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);

			poseStack.translate(0, -((dimensions.height() * scale) * 0.5 - dimensions.height() * 0.5), 0);
			getParentModel().renderToBuffer(poseStack, auraBufferSource.getBuffer(getTextureLocation(entity)), light, OverlayTexture.NO_OVERLAY, MainHelper.getAuraColor(entity).getDecimal());

			poseStack.popPose();
		}
	}
}
