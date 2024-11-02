package dev.cammiescorner.devotion;

import dev.cammiescorner.devotion.client.AuraEffectManager;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.velvet.api.event.EntitiesPreRenderCallback;
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class DevotionClient implements ClientEntryPoint {
	private static final ResourceLocation BASIC_MAGE_ROBES = Devotion.id("textures/entity/armor/basic_mage_robes.png");
	private static final ResourceLocation ENHANCER_MAGE_ROBES = Devotion.id("textures/entity/armor/enhancer_mage_robes.png");
	private static final ResourceLocation TRANSMUTER_MAGE_ROBES = Devotion.id("textures/entity/armor/transmuter_mage_robes.png");
	private static final ResourceLocation EMITTER_MAGE_ROBES = Devotion.id("textures/entity/armor/emitter_mage_robes.png");
	private static final ResourceLocation CONJURER_MAGE_ROBES = Devotion.id("textures/entity/armor/conjurer_mage_robes.png");
	private static final ResourceLocation MANIPULATOR_MAGE_ROBES = Devotion.id("textures/entity/armor/manipulator_mage_robes.png");
	public static final Minecraft client = Minecraft.getInstance();
	public static StructureMapData data = StructureMapData.empty();

	@Override
	public void onInitializeClient(ModContainer mod) {
		EntitiesPreRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
	}
}
