package dev.cammiescorner.devotion.fabric.entrypoints;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.AuraEffectManager;
import dev.upcraft.sparkweave.api.annotation.CalledByReflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;

@CalledByReflection
public class FabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		CoreShaderRegistrationCallback.EVENT.register(context -> context.register(Devotion.id("rendertype_aura"), DefaultVertexFormat.POSITION_TEX_COLOR, AuraEffectManager.INSTANCE::initShaderInstance));
	}
}
