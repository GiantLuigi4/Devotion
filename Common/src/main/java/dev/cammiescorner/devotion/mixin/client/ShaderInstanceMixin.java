package dev.cammiescorner.devotion.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin {
	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
	private ResourceLocation namespaceCanBeAnything(String location, Operation<ResourceLocation> original, ResourceProvider resourceProvider, String name, VertexFormat vertexFormat) {
		int index = name.indexOf(':');

		if(index < 0)
			return original.call(location);

		String namespace = name.substring(0, index);
		String path = name.substring(index + 1);
		return ResourceLocation.fromNamespaceAndPath(namespace, "shaders/core/" + path + ".json");
	}
}
