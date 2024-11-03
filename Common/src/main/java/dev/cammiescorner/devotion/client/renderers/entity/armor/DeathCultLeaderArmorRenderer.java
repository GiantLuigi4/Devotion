package dev.cammiescorner.devotion.client.renderers.entity.armor;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.DevotionClient;
import dev.cammiescorner.devotion.client.models.armor.DeathCultLeaderArmorModel;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DeathCultLeaderArmorRenderer {
	private static final ResourceLocation TEXTURE = Devotion.id("textures/entity/armor/death_cult_leader_armor.png");
	private final DeathCultLeaderArmorModel<LivingEntity> model;

	public DeathCultLeaderArmorRenderer() {
		this.model = new DeathCultLeaderArmorModel<>(DevotionClient.client.getEntityModels().bakeLayer(DeathCultLeaderArmorModel.MODEL_LAYER));
	}

	private void setPartVisibility(ItemStack stack, LivingEntity entity, EquipmentSlot slot) {
		boolean wearingCloak = entity.getItemBySlot(EquipmentSlot.BODY).is(DevotionItems.DEATH_CULT_LEADER_CLOAK.get());
		model.setAllVisible(true);

		switch(slot) {
			case HEAD -> {
				model.skull.visible = true;
				model.hood.visible = wearingCloak;
			}
			case BODY -> {
				model.cloak.visible = true;
			}
			case LEGS -> {
				model.gambeson.visible = wearingCloak;
				model.rightCuisse.visible = true;
				model.leftCuisse.visible = true;
			}
			case FEET -> {
				model.rightShoe.visible = true;
				model.leftShoe.visible = true;
			}
		}
	}
}
