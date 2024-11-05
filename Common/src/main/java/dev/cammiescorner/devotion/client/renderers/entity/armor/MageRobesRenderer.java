package dev.cammiescorner.devotion.client.renderers.entity.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.client.models.armor.MageRobesModel;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.upcraft.sparkweave.api.client.render.CustomHumanoidModelArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MageRobesRenderer extends CustomHumanoidModelArmorRenderer<LivingEntity, HumanoidModel<LivingEntity>, MageRobesModel<LivingEntity>> {
	private final MageRobesModel<LivingEntity> model;
	private final ResourceLocation texture;
	boolean slim = false;

	public MageRobesRenderer(EntityRendererProvider.Context context, ResourceLocation texture) {
		this.model = new MageRobesModel<>(context.bakeLayer(MageRobesModel.MODEL_LAYER));
		this.texture = texture;
	}

	@Override
	protected void renderModelPart(PoseStack matrices, MultiBufferSource bufferSource, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, int dyeColor, HumanoidModel<LivingEntity> contextModel, MageRobesModel<LivingEntity> armorModel) {
		super.renderModelPart(matrices, bufferSource, stack, entity, slot, light, dyeColor, contextModel, armorModel);
		slim = contextModel instanceof PlayerModel<LivingEntity> playerModel && playerModel.slim;
	}

	@Override
	protected void setPartVisibility(MageRobesModel<LivingEntity> model, LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
		boolean isClosed = stack.getOrDefault(DevotionData.CLOSED_HOOD.get(), false);

		model.setAllVisible(true);
		model.closedHood.visible = isClosed && slot == EquipmentSlot.HEAD;
		model.openHood.visible = !isClosed && slot == EquipmentSlot.HEAD;
		model.cloak.visible = slot == EquipmentSlot.HEAD;
		model.garb.visible = slot == EquipmentSlot.CHEST;
		model.rightSleeve.visible = slot == EquipmentSlot.CHEST && !slim;
		model.leftSleeve.visible = slot == EquipmentSlot.CHEST && !slim;
		model.rightSleeveSlim.visible = slot == EquipmentSlot.CHEST && slim;
		model.leftSleeveSlim.visible = slot == EquipmentSlot.CHEST && slim;
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
