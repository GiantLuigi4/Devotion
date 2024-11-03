package dev.cammiescorner.devotion.client.renderers.entity.armor;

import dev.cammiescorner.devotion.DevotionClient;
import dev.cammiescorner.devotion.client.models.armor.MageRobesModel;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class MageRobesRenderer {
	private final MageRobesModel<LivingEntity> model;
	private final ResourceLocation texture;

	public MageRobesRenderer(ResourceLocation texture) {
		this.model = new MageRobesModel<>(DevotionClient.client.getEntityModels().bakeLayer(MageRobesModel.MODEL_LAYER));
		this.texture = texture;
	}

	private void setPartVisibility(ItemStack stack, EquipmentSlot slot) {
		model.setAllVisible(true);

		switch(slot) {
			case HEAD -> {
				boolean isClosed = stack.getOrDefault(DevotionData.CLOSED_HOOD.get(), false);
				model.closedHood.visible = isClosed;
				model.openHood.visible = !isClosed;
				model.cloak.visible = true;
			}
			case BODY -> {
				model.garb.visible = true;
				model.rightSleeve.visible = true;
				model.leftSleeve.visible = true;
			}
			case LEGS -> {
				model.belt.visible = true;
			}
			case FEET -> {
				model.rightShoe.visible = true;
				model.leftShoe.visible = true;
			}
		}
	}
}
