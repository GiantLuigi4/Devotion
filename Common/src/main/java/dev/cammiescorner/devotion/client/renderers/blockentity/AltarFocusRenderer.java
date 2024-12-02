package dev.cammiescorner.devotion.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.cammiescorner.devotion.client.ClientHelper;
import dev.cammiescorner.devotion.client.renderers.AuraVertexBufferSource;
import dev.cammiescorner.devotion.common.blocks.entities.AltarFocusBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;

public class AltarFocusRenderer implements BlockEntityRenderer<AltarFocusBlockEntity> {
	private final ItemRenderer itemRenderer;
	private final BlockRenderDispatcher dispatcher;
	private final RandomSource random;

	public AltarFocusRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
		this.dispatcher = context.getBlockRenderDispatcher();
		this.random = RandomSource.create(42L);
	}

	@Override
	public void render(AltarFocusBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		Level level = blockEntity.getLevel();
		int filledSlots = blockEntity.filledSlots();
		int maxSlots = blockEntity.size();

		if(level != null) {
			float time = level.getGameTime() + partialTick;

			if(!blockEntity.completed()) {
				BlockPos altarPos = blockEntity.getBlockPos();
				float scale = (float) (0.85f + (Math.sin(time * 0.075f) * 0.06f));

				for(BlockPos pillarPos : AltarFocusBlockEntity.getPillarOffsets(altarPos, blockEntity.getBlockState())) {
					poseStack.pushPose();
					poseStack.translate(pillarPos.getX() - altarPos.getX(), pillarPos.getY() - altarPos.getY(), pillarPos.getZ() - altarPos.getZ());

					for(int i = 0; i < 3; i++) {
						poseStack.pushPose();
						poseStack.translate(0, i, 0);
						BlockPos blockPos = pillarPos.above(i);
						renderHologram(level, blockPos, i == 2 ? Blocks.COPPER_BLOCK : Blocks.STONE, poseStack, scale, bufferSource, packedLight, packedOverlay, level.getBlockState(blockPos).isAir());
						poseStack.popPose();
					}

					poseStack.popPose();
				}
			}
			else if(filledSlots > 0) {
				double radius = 1.25;
				double angleBetween = 360 / (double) filledSlots;

				poseStack.pushPose();
				poseStack.translate(0.5f, 1f, 0.5f);
				poseStack.mulPose(Axis.YP.rotationDegrees(time * 2f));

				for(int i = 0; i < filledSlots; ++i) {
					ItemStack stack = blockEntity.getItem(i);
					double angle = Math.toRadians(angleBetween * i);
					double rotX = Math.cos(angle) * radius;
					double rotZ = Math.sin(angle) * radius;

					poseStack.pushPose();
					poseStack.translate(rotX, 0, rotZ);
					poseStack.mulPose(Axis.YN.rotationDegrees(90));
					poseStack.mulPose(Axis.YN.rotation((float) angle));

					itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, new AuraVertexBufferSource(bufferSource, 255, 255, 255, 255), level, 0);
					itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, bufferSource, level, 0);
					poseStack.popPose();
				}

				poseStack.popPose();
			}
		}
	}

	private void renderHologram(Level level, BlockPos blockPos, Block blockToRender, PoseStack poseStack, float scale, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean renderBlock) {
		poseStack.pushPose();

		if(renderBlock) {
			BlockState state = blockToRender.defaultBlockState();
			BakedModel model = dispatcher.getBlockModel(state);
			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(model.getParticleIcon().atlasLocation()));

			poseStack.translate(0.5, 0.5, 0.5);
			poseStack.scale(scale, scale, scale);
			poseStack.translate(-0.5, -0.5, -0.5);

			for(Direction direction : Direction.values())
				for(BakedQuad quad : model.getQuads(state, direction, RandomSource.create(42L)))
					consumer.putBulkData(poseStack.last(), quad, 1f, 1f, 1f, 0.75f, packedLight, packedOverlay);
		}
		else if(level.getBlockState(blockPos) instanceof BlockState blockState && !blockState.is(blockToRender)) {
			poseStack.translate(0.5, 0.5, 0.5);
			poseStack.scale(1.001f, 1.001f, 1.001f);
			poseStack.translate(-0.5, -0.5, -0.5);

			VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(ClientHelper.WHITE_TEXTURE));
			BakedModel model = dispatcher.getBlockModel(blockState);

			for(Direction direction : Direction.values()) {
				BlockPos posToSide = blockPos.offset(direction.getNormal());
				BlockState stateToSide = level.getBlockState(posToSide);

				if(stateToSide.isFaceSturdy(level, posToSide, direction.getOpposite(), SupportType.FULL))
					continue;

				for(BakedQuad quad : model.getQuads(blockState, null, random))
					consumer.putBulkData(poseStack.last(), quad, 1f, 0f, 0f, 0.5f, packedLight, packedOverlay);

//				switch(direction) {
//					case SOUTH -> renderSide(consumer, pose, Direction.SOUTH, scale, 0f, 1f, 0f, 1f, 1f, 1f, 1f, 1f, packedLight, packedOverlay);
//					case NORTH -> renderSide(consumer, pose, Direction.NORTH, scale, 0f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, packedLight, packedOverlay);
//					case EAST -> renderSide(consumer, pose, Direction.EAST, scale, 1f, 1f, 1f, 0f, 0f, 1f, 1f, 0f, packedLight, packedOverlay);
//					case WEST -> renderSide(consumer, pose, Direction.WEST, scale, 0f, 0f, 0f, 1f, 0f, 1f, 1f, 0f, packedLight, packedOverlay);
//					case DOWN -> renderSide(consumer, pose, Direction.DOWN, scale, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 1f, packedLight, packedOverlay);
//					case UP -> renderSide(consumer, pose, Direction.UP, scale, 0f, 1f, 1f, 1f, 1f, 1f, 0f, 0f, packedLight, packedOverlay);
//				}
			}
		}

		poseStack.popPose();
	}

	public static void renderSide(VertexConsumer consumer, PoseStack.Pose pose, Direction direction, float scale, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, int packedLight, int packedOverlay) {
		consumer.addVertex(pose, x1, y1, z1).setColor(1f, 0f, 0f, 0.5f).setUv(0, 1).setOverlay(packedOverlay).setLight(packedLight).setNormal(pose, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ());
		consumer.addVertex(pose, x2, y1, z2).setColor(1f, 0f, 0f, 0.5f).setUv(1, 1).setOverlay(packedOverlay).setLight(packedLight).setNormal(pose, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ());
		consumer.addVertex(pose, x2, y2, z3).setColor(1f, 0f, 0f, 0.5f).setUv(1, 0).setOverlay(packedOverlay).setLight(packedLight).setNormal(pose, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ());
		consumer.addVertex(pose, x1, y2, z4).setColor(1f, 0f, 0f, 0.5f).setUv(0, 0).setOverlay(packedOverlay).setLight(packedLight).setNormal(pose, direction.getNormal().getX(), direction.getNormal().getY(), direction.getNormal().getZ());
	}
}
