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

public class DeathCultLeaderArmorModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Devotion.id("death_cult_leader_robes"), "main");
	public final ModelPart skull;
	public final ModelPart hood;
	public final ModelPart gambeson;
	public final ModelPart cloak;
	public final ModelPart rightCloakSleeve;
	public final ModelPart leftCloakSleeve;
	public final ModelPart rightCuisse;
	public final ModelPart leftCuisse;
	public final ModelPart rightShoe;
	public final ModelPart leftShoe;

	public DeathCultLeaderArmorModel(ModelPart root) {
		super(root, RenderType::armorCutoutNoCull);
		skull = head.getChild("skull");
		hood = head.getChild("hood");
		gambeson = body.getChild("gambeson");
		cloak = body.getChild("cloak");
		rightCloakSleeve = cloak.getChild("rightCloakSleeve");
		leftCloakSleeve = cloak.getChild("leftCloakSleeve");
		rightCuisse = rightLeg.getChild("rightCuisse");
		leftCuisse = leftLeg.getChild("leftCuisse");
		rightShoe = rightLeg.getChild("rightShoe");
		leftShoe = leftLeg.getChild("leftShoe");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition data = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition head = data.getRoot().getChild(PartNames.HEAD);
		PartDefinition body = data.getRoot().getChild(PartNames.BODY);
		PartDefinition rightLeg = data.getRoot().getChild(PartNames.RIGHT_LEG);
		PartDefinition leftLeg = data.getRoot().getChild(PartNames.LEFT_LEG);

		PartDefinition hood = head.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(79, 55).addBox(-5f, -9f, -4.25f, 10f, 9f, 9f, new CubeDeformation(0.55f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
		PartDefinition skull = head.addOrReplaceChild("skull", CubeListBuilder.create().texOffs(72, 93).addBox(-4f, -8f, -4f, 8f, 7f, 8f, new CubeDeformation(0.4f)).texOffs(96, 100).addBox(-4f, -7.1f, -4f, 8f, 4f, 8f, new CubeDeformation(0.35f)), PartPose.offset(0f, 0f, 0f));
		PartDefinition cube_r1 = skull.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(106, 73).addBox(-9f, -12f, -2f, 7f, 3f, 3f, new CubeDeformation(0f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, -0.6545f, 0.7854f));
		PartDefinition cube_r2 = skull.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(78, 47).addBox(2f, -12f, -2f, 7f, 3f, 3f, new CubeDeformation(0f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0.6545f, -0.7854f));

		PartDefinition gambeson = body.addOrReplaceChild("gambeson", CubeListBuilder.create().texOffs(46, 101).addBox(-4f, 1f, -2f, 8f, 13f, 4f, new CubeDeformation(0.9f)), PartPose.offset(0f, 0f, 0f));
		PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create(), PartPose.offset(0f, 2f, 2f));
		PartDefinition cube_r4 = cloak.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(70, 108).addBox(-4f, -2f, 0f, 8f, 18f, 2f, new CubeDeformation(0.2f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0.1309f, 0f, 0f));
		PartDefinition cube_r5 = cloak.addOrReplaceChild("rightCloakSleeve", CubeListBuilder.create().texOffs(46, 81).addBox(2f, 3f, -6f, 9f, 12f, 8f, new CubeDeformation(0.25f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, -0.0436f, 0f));
		PartDefinition cube_r6 = cube_r5.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 64).addBox(1f, -2f, -7f, 11f, 7f, 10f, new CubeDeformation(0.4f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, -0.0436f, -0.1618f));
		PartDefinition cube_r7 = cloak.addOrReplaceChild("leftCloakSleeve", CubeListBuilder.create().texOffs(80, 73).addBox(-11f, 3f, -6f, 9f, 12f, 8f, new CubeDeformation(0.25f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0.0436f, 0f));
		PartDefinition cube_r8 = cube_r7.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(46, 64).addBox(-12f, -2f, -7f, 11f, 7f, 10f, new CubeDeformation(0.4f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0.0436f, 0.1618f));

		PartDefinition rightCuisse = rightLeg.addOrReplaceChild("rightCuisse", CubeListBuilder.create().texOffs(110, 89).addBox(-2f, 0f, -2f, 4f, 7f, 4f, new CubeDeformation(0.25f)), PartPose.offset(0f, 0f, 0f));
		PartDefinition rightShoe = rightLeg.addOrReplaceChild("rightShoe", CubeListBuilder.create().texOffs(106, 112).addBox(-2f, 7f, -2f, 4f, 5f, 4f, new CubeDeformation(0.4F)).texOffs(46, 66).addBox(-2f, 11f, -3f, 4f, 1f, 1f, new CubeDeformation(0.4f)), PartPose.offset(0f, 0f, 0f));

		PartDefinition leftCuisse = leftLeg.addOrReplaceChild("leftCuisse", CubeListBuilder.create().texOffs(108, 47).addBox(-1.8f, 0f, -2f, 4f, 7f, 4f, new CubeDeformation(0.25f)), PartPose.offset(0f, 0f, 0f));
		PartDefinition leftShoe = leftLeg.addOrReplaceChild("leftShoe", CubeListBuilder.create().texOffs(90, 112).addBox(-1.8f, 7f, -2f, 4f, 5f, 4f, new CubeDeformation(0.4f)).texOffs(46, 64).addBox(-1.8f, 11f, -3f, 4f, 1f, 1f, new CubeDeformation(0.4f)), PartPose.offset(0f, 0f, 0f));

		return LayerDefinition.create(data, 128, 128);
	}

	@Override
	public void setupAnim(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		hood.setRotation(headPitch / 4, netHeadYaw / 8, hood.zRot);
	}
}
