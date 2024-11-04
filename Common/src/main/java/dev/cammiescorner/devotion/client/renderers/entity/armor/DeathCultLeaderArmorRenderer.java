package dev.cammiescorner.devotion.client.renderers.entity.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.models.armor.DeathCultLeaderArmorModel;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import dev.upcraft.sparkweave.api.client.render.CustomHumanoidModelArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DeathCultLeaderArmorRenderer extends CustomHumanoidModelArmorRenderer<LivingEntity, HumanoidModel<LivingEntity>, DeathCultLeaderArmorModel<LivingEntity>> {
	private static final ResourceLocation TEXTURE = Devotion.id("textures/entity/armor/death_cult_leader_armor.png");
	private final DeathCultLeaderArmorModel<LivingEntity> model;

	public DeathCultLeaderArmorRenderer(EntityRendererProvider.Context context) {
		this.model = new DeathCultLeaderArmorModel<>(context.bakeLayer(DeathCultLeaderArmorModel.MODEL_LAYER));
	}

	@Override
	protected void renderModelPart(PoseStack matrices, MultiBufferSource bufferSource, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, int dyeColor, HumanoidModel<LivingEntity> contextModel, DeathCultLeaderArmorModel<LivingEntity> armorModel) {
		model.rightCloakSleeve.setRotation(contextModel.leftArm.xRot, 0, 0);
		model.leftCloakSleeve.setRotation(contextModel.rightArm.xRot, 0, 0);

		super.renderModelPart(matrices, bufferSource, stack, entity, slot, light, dyeColor, contextModel, armorModel);
	}

	@Override
	protected void setPartVisibility(DeathCultLeaderArmorModel<LivingEntity> model, LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		boolean wearingCloak = entity.getItemBySlot(EquipmentSlot.CHEST).is(DevotionItems.DEATH_CULT_LEADER_CLOAK.get());

		model.setAllVisible(true);
		model.skull.visible = slot == EquipmentSlot.HEAD;
		model.hood.visible = slot == EquipmentSlot.HEAD;
		model.cloak.visible = slot == EquipmentSlot.CHEST;
		model.gambeson.visible = wearingCloak && slot == EquipmentSlot.LEGS;
		model.rightCuisse.visible = slot == EquipmentSlot.LEGS;
		model.leftCuisse.visible = slot == EquipmentSlot.LEGS;
		model.rightShoe.visible = slot == EquipmentSlot.FEET;
		model.leftShoe.visible = slot == EquipmentSlot.FEET;
	}

	@Override
	protected DeathCultLeaderArmorModel<LivingEntity> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		return model;
	}

	@Override
	protected ResourceLocation getTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		return TEXTURE;
	}
}
