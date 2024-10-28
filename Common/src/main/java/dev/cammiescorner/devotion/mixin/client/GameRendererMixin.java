package dev.cammiescorner.devotion.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
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
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Shadow @Final private Map<String, ShaderInstance> shaders;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
	private void renderShader(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo info) {
		AuraEffectManager.INSTANCE.renderShader(deltaTracker.getGameTimeDeltaPartialTick(renderLevel));
	}

	@Inject(method = "reloadShaders", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;loadBlurEffect(Lnet/minecraft/server/packs/resources/ResourceProvider;)V"))
	private void onReload(ResourceProvider resourceProvider, CallbackInfo info, @Local(ordinal = 1) List<Pair<ShaderInstance, Consumer<ShaderInstance>>> programs) {
		try {
			AuraEffectManager.INSTANCE.initCoreShader(resourceProvider);
			AuraEffectManager.INSTANCE.initPostShader();

			programs.add(Pair.of(AuraEffectManager.INSTANCE.auraShader, shaderInstance -> {}));
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
