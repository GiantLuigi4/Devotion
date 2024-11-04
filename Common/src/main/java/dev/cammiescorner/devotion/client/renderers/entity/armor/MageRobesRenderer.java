package dev.cammiescorner.devotion.client.renderers.entity.armor;

import dev.cammiescorner.devotion.client.models.armor.MageRobesModel;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.upcraft.sparkweave.api.client.render.CustomHumanoidModelArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MageRobesRenderer extends CustomHumanoidModelArmorRenderer<LivingEntity, HumanoidModel<LivingEntity>, MageRobesModel<LivingEntity>> {
	private final MageRobesModel<LivingEntity> model;
	private final ResourceLocation texture;

	public MageRobesRenderer(EntityRendererProvider.Context context, ResourceLocation texture) {
		this.model = new MageRobesModel<>(context.bakeLayer(MageRobesModel.MODEL_LAYER));
		this.texture = texture;
	}

	@Override
	protected void setPartVisibility(MageRobesModel<LivingEntity> model, LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		boolean isClosed = stack.getOrDefault(DevotionData.CLOSED_HOOD.get(), false);

		model.setAllVisible(true);
		model.closedHood.visible = isClosed && slot == EquipmentSlot.HEAD;
		model.openHood.visible = !isClosed && slot == EquipmentSlot.HEAD;
		model.cloak.visible = slot == EquipmentSlot.HEAD;
		model.garb.visible = slot == EquipmentSlot.CHEST;
		model.rightSleeve.visible = slot == EquipmentSlot.CHEST;
		model.leftSleeve.visible = slot == EquipmentSlot.CHEST;
		model.belt.visible = slot == EquipmentSlot.LEGS;
		model.rightShoe.visible = slot == EquipmentSlot.FEET;
		model.leftShoe.visible = slot == EquipmentSlot.FEET;
	}

	@Override
	protected MageRobesModel<LivingEntity> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		return model;
	}

	@Override
	protected ResourceLocation getTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		return texture;
	}
}
