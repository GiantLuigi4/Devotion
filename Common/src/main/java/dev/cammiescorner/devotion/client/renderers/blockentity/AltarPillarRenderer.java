package dev.cammiescorner.devotion.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.models.blockentity.AltarPillarModel;
import dev.cammiescorner.devotion.common.blocks.entities.AltarPillarBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class AltarPillarRenderer implements BlockEntityRenderer<AltarPillarBlockEntity> {
	private static final ResourceLocation TEXTURE = Devotion.id("textures/block/altar_pillar.png");
	private final AltarPillarModel model;

	public AltarPillarRenderer(BlockEntityRendererProvider.Context context) {
		this.model = new AltarPillarModel(context.bakeLayer(AltarPillarModel.MODEL_LAYER));
	}

	@Override
	public void render(AltarPillarBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		Vec3 altarFocusPos = blockEntity.getAltarFocusPos().getCenter();
		Vec3 pillarPos = blockEntity.getBlockPos().getCenter();
		Vec3 direction = pillarPos.subtract(altarFocusPos).normalize();

		poseStack.pushPose();
		poseStack.mulPose(Axis.ZP.rotationDegrees(180f));
		poseStack.translate(-0.5, -1.5, 0.5);
		poseStack.mulPose(Axis.YP.rotationDegrees(90f));
		model.claw.yRot = (float) Math.atan2(direction.z, direction.x);
		model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutout(TEXTURE)), packedLight, packedOverlay, 0xffffff);
		poseStack.popPose();
	}
}
