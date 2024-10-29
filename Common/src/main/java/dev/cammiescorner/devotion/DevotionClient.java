package dev.cammiescorner.devotion;

import dev.cammiescorner.devotion.client.AuraEffectManager;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.velvet.api.event.EntitiesPreRenderCallback;
import dev.cammiescorner.velvet.api.event.ShaderEffectRenderCallback;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.client.Minecraft;

public class DevotionClient implements ClientEntryPoint {
	public static final Minecraft client = Minecraft.getInstance();
	public static StructureMapData data = StructureMapData.empty();

	@Override
	public void onInitializeClient(ModContainer mod) {
		EntitiesPreRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
		ShaderEffectRenderCallback.EVENT.register(AuraEffectManager.INSTANCE);
	}
}
