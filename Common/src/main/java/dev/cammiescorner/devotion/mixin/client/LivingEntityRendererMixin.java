package dev.cammiescorner.devotion.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.client.renderers.entity.layers.AuraRenderLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
	@Unique private AuraRenderLayer<T, M> auraLayer;
	@Shadow @Final protected List<RenderLayer<T, M>> layers;

	protected LivingEntityRendererMixin(EntityRendererProvider.Context context) { super(context); }

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(EntityRendererProvider.Context context, EntityModel<T> model, float shadowRadius, CallbackInfo info) {
		auraLayer = new AuraRenderLayer<>(this, layers);
	}

	@Inject(
		method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;")
	)
	private void renderAura(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo info, @Local(name = "f5") LocalFloatRef limbSwing, @Local(name = "f4") LocalFloatRef limbSwingAmount, @Local(name = "f9") LocalFloatRef ageInTicks, @Local(name = "f2") LocalFloatRef netHeadYaw, @Local(name = "f6") LocalFloatRef headPitch) {
		if(!entity.isSpectator()) {
			auraLayer.render(poseStack, buffer, packedLight, entity, limbSwing.get(), limbSwingAmount.get(), partialTicks, ageInTicks.get(), netHeadYaw.get(), headPitch.get());
		}
	}
}
