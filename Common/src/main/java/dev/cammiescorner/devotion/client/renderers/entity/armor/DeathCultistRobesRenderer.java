package dev.cammiescorner.devotion.client.renderers.entity.armor;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.DevotionClient;
import dev.cammiescorner.devotion.client.models.armor.DeathCultistRobesModel;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DeathCultistRobesRenderer {
	private static final ResourceLocation TEXTURE = Devotion.id("textures/entity/armor/death_cultist_robes.png");
	private final DeathCultistRobesModel<LivingEntity> model;

	public DeathCultistRobesRenderer() {
		this.model = new DeathCultistRobesModel<>(DevotionClient.client.getEntityModels().bakeLayer(DeathCultistRobesModel.MODEL_LAYER));
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
				model.rightPantLeg.visible = true;
				model.leftPantLeg.visible = true;
			}
			case FEET -> {
				model.rightShoe.visible = true;
				model.leftShoe.visible = true;
			}
		}
	}
}