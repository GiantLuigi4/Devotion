package dev.cammiescorner.devotion.mixin.client;

import dev.cammiescorner.devotion.client.AuraEffectManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Map;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow @Final private Map<String, ShaderInstance> shaders;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
	private void renderShader(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo info) {
		AuraEffectManager.INSTANCE.renderShader(deltaTracker.getGameTimeDeltaPartialTick(renderLevel));
	}

	@Inject(method = "reloadShaders", at = @At("RETURN"))
	private void onReload(ResourceProvider resourceProvider, CallbackInfo info) {
		try {
			AuraEffectManager.INSTANCE.initCoreShader(resourceProvider);
			AuraEffectManager.INSTANCE.initPostShader();

			shaders.put(AuraEffectManager.INSTANCE.auraShader.getName(), AuraEffectManager.INSTANCE.auraShader);
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
