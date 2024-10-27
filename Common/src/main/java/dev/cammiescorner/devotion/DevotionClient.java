package dev.cammiescorner.devotion;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.cammiescorner.devotion.client.ClientHelper;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public class DevotionClient implements ClientEntryPoint {
	public static StructureMapData data = StructureMapData.empty();
	public static final Supplier<ShaderInstance> AURA_SHADER = () -> ClientHelper.createShaderInstance(Minecraft.getInstance().getResourceManager(), Devotion.id("rendertype_aura"), DefaultVertexFormat.POSITION_TEX_COLOR);
	public static final RenderStateShard.ShaderStateShard AURA_SHADER_STATE = new RenderStateShard.ShaderStateShard(AURA_SHADER);
	public static final Function<ResourceLocation, RenderType> AURA_RENDER_TYPE = Util.memoize(texture -> RenderType.create(
		"devotion:aura",
		DefaultVertexFormat.POSITION_TEX_COLOR,
		VertexFormat.Mode.QUADS,
		1536,
		RenderType.CompositeState.builder()
			.setShaderState(AURA_SHADER_STATE)
			.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
			.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
			.setOutputState(RenderStateShard.OUTLINE_TARGET)
			.createCompositeState(RenderType.OutlineProperty.IS_OUTLINE)
	));

	@Override
	public void onInitializeClient(ModContainer mod) {

	}

	public static RenderType auraRenderType(ResourceLocation texture) {
		return AURA_RENDER_TYPE.apply(texture);
	}
}
