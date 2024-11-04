package dev.cammiescorner.devotion.client.models.armor;// Made with Blockbench 4.11.1

import dev.cammiescorner.devotion.Devotion;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class MageRobesModel<T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation MODEL_LAYER = new ModelLayerLocation(Devotion.id("mage_robes"), "main");
	public final ModelPart closedHood;
	public final ModelPart cloak;
	public final ModelPart openHood;
	public final ModelPart garb;
	public final ModelPart rightSleeve;
	public final ModelPart leftSleeve;
	public final ModelPart belt;
	public final ModelPart rightShoe;
	public final ModelPart leftShoe;

	public MageRobesModel(ModelPart root) {
		super(root, RenderType::armorCutoutNoCull);
		closedHood = head.getChild("closedHood");
		cloak = body.getChild("cloak");
		openHood = cloak.getChild("openHood");
		garb = body.getChild("garb");
		rightSleeve = rightArm.getChild("rightSleeve");
		leftSleeve = leftArm.getChild("leftSleeve");
		belt = body.getChild("belt");
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
		PartDefinition closedHoodFront = closedHood.addOrReplaceChild("closedHoodFront", CubeListBuilder.create().texOffs(29, 78).addBox(-3f, 0.2f, 0f, 6f, 6f, 3f, new CubeDeformation(0.35f)), PartPose.offsetAndRotation(0f, -8f, -4f, -0.1745f, 0f, 0f));

		PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(24, 91).addBox(-5f, -1f, 2f, 10f, 16f, 1f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, 2f, 0f, 0.1745f, 0f, 0f));

		PartDefinition openHood = cloak.addOrReplaceChild("openHood", CubeListBuilder.create().texOffs(0, 80).addBox(-5f, -4f, 0f, 10f, 4f, 7f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, 2f, 0f, 0.1745f, 0f, 0f));

		PartDefinition garb = body.addOrReplaceChild("garb", CubeListBuilder.create().texOffs(0, 91).addBox(-4f, -12f, -2f, 8f, 11f, 4f, new CubeDeformation(0.3f)), PartPose.offset(0f, 12f, 0f));
		PartDefinition backCover = garb.addOrReplaceChild("backCover", CubeListBuilder.create().texOffs(16, 108).addBox(-4f, 0f, 0f, 8f, 8f, 1f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, -1f, 1f, 0.2618f, 0f, 0f));

		PartDefinition rightCover = garb.addOrReplaceChild("rightCover", CubeListBuilder.create().texOffs(50, 110).addBox(-1f, -1f, -2f, 1f, 8f, 4f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(-3f, 0f, 0f, 0f, 0f, 0.2618f));
		PartDefinition leftCover = garb.addOrReplaceChild("leftCover", CubeListBuilder.create().texOffs(48, 64).addBox(0f, -1f, -2f, 1f, 8f, 4f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(3f, 0f, 0f, 0f, 0f, -0.2618f));

		PartDefinition rightSleeve = rightArm.addOrReplaceChild("rightSleeve", CubeListBuilder.create().texOffs(0, 106).addBox(-3f, -2f, -2f, 4f, 10f, 4f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));
		PartDefinition leftSleeve = leftArm.addOrReplaceChild("leftSleeve", CubeListBuilder.create().texOffs(32, 64).addBox(-1f, -2f, -2f, 4f, 10f, 4f, new CubeDeformation(0.3f)), PartPose.offsetAndRotation(0f, 0f, 0f, 0f, 0f, 0f));

		PartDefinition belt = body.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(41, 80).addBox(-3f, -3f, -2f, 6f, 3f, 1f, new CubeDeformation(0.2f)), PartPose.offset(0f, 12f, 0f));

		PartDefinition rightShoe = rightLeg.addOrReplaceChild("rightShoe", CubeListBuilder.create().texOffs(34, 110).addBox(-2f, 7f, -2f, 4f, 5f, 4f, new CubeDeformation(0.2f)), PartPose.offset(0f, 0f, 0f));

		PartDefinition leftShoe = leftLeg.addOrReplaceChild("leftShoe", CubeListBuilder.create().texOffs(46, 90).addBox(-2f, 7f, -2f, 4f, 5f, 4f, new CubeDeformation(0.2f)), PartPose.offset(0.2f, 0f, 0f));

		return LayerDefinition.create(data, 64, 128);
	}
}