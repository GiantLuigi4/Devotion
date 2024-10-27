package dev.cammiescorner.devotion.mixin.client;

import dev.cammiescorner.devotion.client.renderers.entity.layers.AuraRenderLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> implements RenderLayerParent<T, M> {
	@Shadow @Final protected List<RenderLayer<T, M>> layers;
	@Shadow protected abstract boolean addLayer(RenderLayer<T, M> layer);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void addAuraLayer(EntityRendererProvider.Context context, M model, float shadowRadius, CallbackInfo info) {
		addLayer(new AuraRenderLayer<>(this, layers));
	}
}
