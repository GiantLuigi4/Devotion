package dev.cammiescorner.devotion.client.models.blockentity;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.cammiescorner.devotion.Devotion;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class AltarPillarModel extends Model {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Devotion.id("altar_pillar"), "main");
	private final ModelPart root;
	public final ModelPart claw;

	public AltarPillarModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.root = root;
		this.claw = root.getChild("base").getChild("claw");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-8f, -8f, -8f, 16f, 8f, 16f, new CubeDeformation(0f)), PartPose.offset(0f, 24f, 0f));

		PartDefinition claw = base.addOrReplaceChild("claw", CubeListBuilder.create().texOffs(0, 24).addBox(-5.5f, -9f, -5.5f, 11f, 12f, 11f, new CubeDeformation(0f)), PartPose.offsetAndRotation(0f, -8f, 0f, -0.0873f, 0f, 0f));
		PartDefinition cube_r1 = claw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(64, 0).addBox(-7f, -10f, -7f, 14f, 10f, 14f, new CubeDeformation(0f)), PartPose.offsetAndRotation(0f, -7f, 0f, -0.0654f, 0f, 0f));

		PartDefinition clawmid = claw.addOrReplaceChild("clawmid", CubeListBuilder.create().texOffs(0, 48).addBox(-5f, -12f, -4.5f, 10f, 12f, 10f, new CubeDeformation(0f)), PartPose.offsetAndRotation(0f, -16f, 0f, -0.1309f, 0f, 0f));
		PartDefinition cube_r2 = clawmid.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(64, 24).addBox(-6f, -9f, -5.5f, 12f, 10f, 12f, new CubeDeformation(0f)), PartPose.offsetAndRotation(0f, -12f, 0f, -0.0873f, 0f, 0f));

		PartDefinition clawtip = clawmid.addOrReplaceChild("clawtip", CubeListBuilder.create().texOffs(0, 69).addBox(-4f, -14f, -2.75f, 8f, 16f, 8f, new CubeDeformation(0f)), PartPose.offsetAndRotation(0f, -21f, 0f, -0.2182f, 0f, 0f));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, buffer, packedLight, packedOverlay, color);
	}
}