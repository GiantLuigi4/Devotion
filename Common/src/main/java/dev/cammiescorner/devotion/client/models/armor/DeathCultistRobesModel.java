package dev.cammiescorner.devotion.client.models.armor;

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class DeathCultistRobesModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Devotion.id("death_cultist_robes"), "main");
	public final ModelPart closedHood;
	public final ModelPart cloak;
	public final ModelPart openHood;
	public final ModelPart garb;
	public final ModelPart rightSleeve;
	public final ModelPart leftSleeve;
	public final ModelPart rightPantLeg;
	public final ModelPart leftPantLeg;
	public final ModelPart rightShoe;
	public final ModelPart leftShoe;

	public DeathCultistRobesModel(ModelPart root) {
		super(root, RenderType::armorCutoutNoCull);
		closedHood = head.getChild("closedHood");
		cloak = body.getChild("cloak");
		openHood = cloak.getChild("openHood");
		garb = body.getChild("garb");
		rightSleeve = rightArm.getChild("rightSleeve");
		leftSleeve = leftArm.getChild("leftSleeve");
		rightPantLeg = rightLeg.getChild("rightLegSleeve");
		leftPantLeg = leftLeg.getChild("leftLegSleeve");
		rightShoe = rightLeg.getChild("rightShoe");
		leftShoe = leftLeg.getChild("leftShoe");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition head = data.getRoot().getChild(PartNames.HEAD);
		PartDefinition body = data.getRoot().getChild(PartNames.BODY);
		PartDefinition rightArm = data.getRoot().getChild(PartNames.RIGHT_ARM);
		PartDefinition leftArm = data.getRoot().getChild(PartNames.LEFT_ARM);
		PartDefinition rightLeg = data.getRoot().getChild(PartNames.RIGHT_LEG);
		PartDefinition leftLeg = data.getRoot().getChild(PartNames.LEFT_LEG);

		PartDefinition closedHood = head.addOrReplaceChild("closedHood", CubeListBuilder.create().texOffs(0, 64).addBox(-4f, -8f, -4f, 8f, 8f, 8f, new CubeDeformation(0.3f)), PartPose.offset(0f, 0f, 0f));
		PartDefinition cube_r1 = closedHood.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 80).addBox(3.6f, -6f, -3.5f, 3f, 7f, 7f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, -0.351f));
		PartDefinition cube_r2 = closedHood.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(17, 94).addBox(-6.6f, -6f, -3.5f, 3f, 7f, 7f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0.351f));
		
		PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(15, 108).addBox(-4f, -1f, -1f, 8f, 6f, 1f, new CubeDeformation(0f)), PartPose.offsetAndRotation(0f, 0f, 3f, 0.2182f, 0f, 0f));
		PartDefinition openHood = cloak.addOrReplaceChild("openHood", CubeListBuilder.create().texOffs(30, 117).addBox(-5f, -4f, -1f, 10f, 4f, 7f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, 2f, 0f, 0.1745f, 0f, 0f));
		PartDefinition garb = body.addOrReplaceChild("garb", CubeListBuilder.create().texOffs(45, 105).addBox(-3f, -3f, -2f, 6f, 3f, 1f, new CubeDeformation(0.4f)).texOffs(0, 80).addBox(-4f, -12f, -2f, 8f, 11f, 4f, new CubeDeformation(0.3f)), PartPose.offset(0f, 12f, 0f));
		PartDefinition cube_r5 = garb.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 120).addBox(-2f, 0.5f, -4f, 4f, 4f, 4f, new CubeDeformation(-0.5f)), PartPose.offsetAndRotation(0f, -12f, 0f, 0.1745f, 0f, 0f));
		PartDefinition backCover = garb.addOrReplaceChild("backCover", CubeListBuilder.create().texOffs(37, 78).addBox(-4f, 0f, 0f, 8f, 8f, 1f, new CubeDeformation(0.35f)), PartPose.offsetAndRotation(0f, -1f, 1f, 0.2618f, 0f, 0f));
		PartDefinition rightCover = garb.addOrReplaceChild("rightCover", CubeListBuilder.create().texOffs(51, 83).addBox(-1.2f, -1f, -2f, 1f, 8f, 4f, new CubeDeformation(0.35f)), PartPose.offsetAndRotation(-3f, 0f, 0f, 0f, 0f, 0.2618f));
		PartDefinition leftCover = garb.addOrReplaceChild("leftCover", CubeListBuilder.create().texOffs(16, 115).addBox(0.2f, -1f, -2f, 1f, 8f, 4f, new CubeDeformation(0.35f)), PartPose.offsetAndRotation(3f, 0f, 0f, 0f, 0f, -0.2618f));
		
		PartDefinition rightSleeve = rightArm.addOrReplaceChild("rightSleeve", CubeListBuilder.create().texOffs(32, 64).addBox(-3f, -2f, -2f, 4f, 10f, 4f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));
		PartDefinition leftSleeve = leftArm.addOrReplaceChild("leftSleeve", CubeListBuilder.create().texOffs(0, 95).addBox(-1f, -2f, -2f, 4f, 10f, 4f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));
		
		PartDefinition rightShoe = rightLeg.addOrReplaceChild("rightShoe", CubeListBuilder.create().texOffs(48, 64).addBox(-2f, 7f, -2f, 4f, 5f, 4f, new CubeDeformation(0.4f)), PartPose.offset(-0.2f, 0f, 0f));
		PartDefinition rightLegSleeve = rightLeg.addOrReplaceChild("rightLegSleeve", CubeListBuilder.create().texOffs(37, 94).addBox(-2f, 0f, -2f, 4f, 7f, 4f, new CubeDeformation(0.3f)), PartPose.offset(-0.1f, 0f, 0f));
		PartDefinition leftShoe = leftLeg.addOrReplaceChild("leftShoe", CubeListBuilder.create().texOffs(0, 111).addBox(-2f, 7f, -2f, 4f, 5f, 4f, new CubeDeformation(0.4f)), PartPose.offset(0.2f, 0f, 0f));
		PartDefinition leftLegSleeve = leftLeg.addOrReplaceChild("leftLegSleeve", CubeListBuilder.create().texOffs(33, 105).addBox(-1.9f, 0f, -2f, 4f, 7f, 4f, new CubeDeformation(0.3f)), PartPose.offset(0f, 0f, 0f));

		return LayerDefinition.create(data, 64, 128);
	}
}
