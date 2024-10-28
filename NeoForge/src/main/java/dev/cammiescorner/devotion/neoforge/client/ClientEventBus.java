package dev.cammiescorner.devotion.neoforge.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.client.AuraEffectManager;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber
public class ClientEventBus {
	@SubscribeEvent
	public void registerShaders(RegisterShadersEvent event) throws IOException {
		event.registerShader(new ShaderInstance(event.getResourceProvider(), Devotion.id("rendertype_aura"), DefaultVertexFormat.POSITION_TEX_COLOR), AuraEffectManager.INSTANCE::initShaderInstance);
	}
}
