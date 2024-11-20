package dev.cammiescorner.devotion.mixin.client;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.client.renderers.entity.layers.AuraRenderLayer;
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
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
	@Unique private AuraRenderLayer<T, M> auraLayer;
	@Shadow @Final protected List<RenderLayer<T, M>> layers;

	protected LivingEntityRendererMixin(EntityRendererProvider.Context context) { super(context); }

	@ModifyArgs(
		method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Entity;FFFFFF)V")
	)
	private void stealArgs(Args args, @Share("limbSwing") LocalFloatRef limbSwing, @Share("limbSwingAmount") LocalFloatRef limbSwingAmount, @Share("ageInTicks") LocalFloatRef ageInTicks, @Share("netHeadYaw") LocalFloatRef netHeadYaw, @Share("headPitch") LocalFloatRef headPitch) {
		limbSwing.set(args.get(4));
		limbSwingAmount.set(args.get(5));
		ageInTicks.set(args.get(7));
		netHeadYaw.set(args.get(8));
		headPitch.set(args.get(9));
	}

	@Inject(
		method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;")
	)
	private void renderAura(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo info, @Share("limbSwing") LocalFloatRef limbSwing, @Share("limbSwingAmount") LocalFloatRef limbSwingAmount, @Share("ageInTicks") LocalFloatRef ageInTicks, @Share("netHeadYaw") LocalFloatRef netHeadYaw, @Share("headPitch") LocalFloatRef headPitch) {
		if(!entity.isSpectator()) {
			if(auraLayer == null)
				auraLayer = new AuraRenderLayer<>(this, layers);

			auraLayer.render(poseStack, buffer, packedLight, entity, limbSwing.get(), limbSwingAmount.get(), partialTicks, ageInTicks.get(), netHeadYaw.get(), headPitch.get());
		}
	}
}
