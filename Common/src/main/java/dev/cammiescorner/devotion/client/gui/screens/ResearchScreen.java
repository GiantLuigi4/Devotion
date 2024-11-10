package dev.cammiescorner.devotion.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Pair;
import commonnetwork.api.Network;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.RiddleData;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.networking.serverbound.ServerboundSaveScrollDataPacket;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.screens.ResearchMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static dev.cammiescorner.devotion.DevotionClient.client;

public class ResearchScreen extends AbstractContainerScreen<ResearchMenu> {
	public static final ResourceLocation TEXTURE = Devotion.id("textures/gui/research/research_scroll.png");
	private final List<Pair<Vec2, Vec2>> lines = new ArrayList<>();
	private final List<AuraType> auraTypes = new ArrayList<>();
	private final Vec2 enhancerPos = new Vec2(268, 52);
	private final Vec2 transmuterPos = new Vec2(328, 97);
	private final Vec2 conjurerPos = new Vec2(305, 167);
	private final Vec2 manipulatorPos = new Vec2(231, 167);
	private final Vec2 emitterPos = new Vec2(208, 97);
	private final Vec2 specialistPos = new Vec2(268, 116);
	private final double angle = Math.toRadians(72);
	private final double offset = Math.PI * 0.5;
	private final int distance = 64;
	private ItemStack stack = ItemStack.EMPTY;
	private Vec2 mousePos = new Vec2(0, 0);
	private Vec2 lastPos = new Vec2(0, 0);
	private Vec2 lineStart;

	public ResearchScreen(ResearchMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, Component.empty());
	}

	@Override
	protected void init() {
		super.init();
		leftPos = (width - 384) / 2;
		topPos = (height - 216) / 2;
		addRenderableWidget(Button.builder(Component.translatable("lectern." + Devotion.MOD_ID + ".take_scroll"), this::takeScrollButtonShit).bounds(leftPos + 142, topPos + 200, 100, 20).build());
		redrawLines();
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, 384, 216, 384, 320);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		PoseStack poseStack = guiGraphics.pose();

		if(lineStart != null)
			drawLine(poseStack, lineStart.x, lineStart.y, mousePos.x, mousePos.y);

		if(!lines.isEmpty()) {
			for(Pair<Vec2, Vec2> line : lines) {
				Vec2 startPos = line.getFirst();
				Vec2 endPos = line.getSecond();
				drawLine(poseStack, startPos.x, startPos.y, endPos.x, endPos.y);
			}
		}

		DataComponentType<RiddleData> riddleData = DevotionData.RIDDLE_DATA.get();

		if(stack.get(riddleData) instanceof RiddleData riddles) {
			List<Pair<AuraType, Integer>> list = riddles.riddles();

			if(!list.isEmpty()) {
				RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
				guiGraphics.blit(TEXTURE, 256, 104, 120, 216, 24, 24, 384, 320);

				for(int i = 0; i < 5; i++)
					guiGraphics.blit(TEXTURE, pentagonX(256, i), pentagonY(104, i), i * 24, 216, 24, 24, 384, 320);

				List<FormattedCharSequence> agony = new ArrayList<>();
				int posY = 0;
				int boxHeight = 151;
				int boxWidth = 132;

				for(int i = 0; i < list.size(); i++) {
					agony.addAll(font.split(Component.translatable(riddles.getRiddleTranslationKey(i)), boxWidth));

					if(i < list.size() - 1)
						agony.add(Component.literal("").getVisualOrderText());
				}

				poseStack.pushPose();
				float textHeight = Math.max(1, agony.size()) * Math.max(1, font.lineHeight - 1);
				float scale = Math.min(1, boxHeight / textHeight);
				poseStack.translate(48 + (boxWidth * 0.5), 34 + (boxHeight * 0.5), 0);
				poseStack.scale(scale, scale, 1);
				poseStack.translate(0, -textHeight * 0.5, 0);

				for(FormattedCharSequence text : agony) {
					guiGraphics.drawString(font, text, (int) (-font.width(text) * 0.5f), posY, 0, false);
					posY += 8;
				}

				poseStack.popPose();
			}
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		renderMenuBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		DataComponentType<Boolean> completedData = DevotionData.SCROLL_COMPLETED.get();
		DataComponentType<RiddleData> riddleData = DevotionData.RIDDLE_DATA.get();

		if(!stack.getOrDefault(completedData, false)) {
			RiddleData riddles = stack.get(riddleData);

			if(riddles != null && !riddles.riddles().isEmpty() && lineStart == null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				if(!lines.isEmpty())
					lastPos = lines.getLast().getSecond();

				if(mousePos.distanceToSqr(enhancerPos) <= 144 && (lines.isEmpty() || lastPos.equals(enhancerPos))) {
					lineStart = enhancerPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.ENHANCER);
				}
				else if(mousePos.distanceToSqr(transmuterPos) <= 144 && (lines.isEmpty() || lastPos.equals(transmuterPos))) {
					lineStart = transmuterPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.TRANSMUTER);
				}
				else if(mousePos.distanceToSqr(emitterPos) <= 144 && (lines.isEmpty() || lastPos.equals(emitterPos))) {
					lineStart = emitterPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.EMITTER);
				}
				else if(mousePos.distanceToSqr(conjurerPos) <= 144 && (lines.isEmpty() || lastPos.equals(conjurerPos))) {
					lineStart = conjurerPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.CONJURER);
				}
				else if(mousePos.distanceToSqr(manipulatorPos) <= 144 && (lines.isEmpty() || lastPos.equals(manipulatorPos))) {
					lineStart = manipulatorPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.MANIPULATOR);
				}
				else if(mousePos.distanceToSqr(specialistPos) <= 144 && (lines.isEmpty() || lastPos.equals(specialistPos))) {
					lineStart = specialistPos;

					if(lines.isEmpty())
						auraTypes.add(AuraType.SPECIALIST);
				}
			}

			if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				if(!lines.isEmpty())
					lines.removeLast();
				if(!auraTypes.isEmpty())
					auraTypes.removeLast();

				Network.getNetworkHandler().sendToServer(new ServerboundSaveScrollDataPacket(menu.containerId, auraTypes));
				lineStart = null;
				lastPos = new Vec2(0, 0);
			}
		}

		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(lineStart != null && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if(mousePos.distanceToSqr(enhancerPos) <= 144 && !lineStart.equals(enhancerPos))
				addLine(lineStart, enhancerPos, AuraType.ENHANCER);
			else if(mousePos.distanceToSqr(transmuterPos) <= 144 && !lineStart.equals(transmuterPos))
				addLine(lineStart, transmuterPos, AuraType.TRANSMUTER);
			else if(mousePos.distanceToSqr(emitterPos) <= 144 && !lineStart.equals(emitterPos))
				addLine(lineStart, emitterPos, AuraType.EMITTER);
			else if(mousePos.distanceToSqr(conjurerPos) <= 144 && !lineStart.equals(conjurerPos))
				addLine(lineStart, conjurerPos, AuraType.CONJURER);
			else if(mousePos.distanceToSqr(manipulatorPos) <= 144 && !lineStart.equals(manipulatorPos))
				addLine(lineStart, manipulatorPos, AuraType.MANIPULATOR);
			else if(mousePos.distanceToSqr(specialistPos) <= 144 && !lineStart.equals(specialistPos))
				addLine(lineStart, specialistPos, AuraType.SPECIALIST);

			lineStart = null;
			Network.getNetworkHandler().sendToServer(new ServerboundSaveScrollDataPacket(menu.containerId, auraTypes));
		}

		return super.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		super.mouseMoved(mouseX, mouseY);
		mousePos = new Vec2((float) (mouseX - leftPos), (float) (mouseY - topPos));
	}

	@Override
	public void onClose() {
		Network.getNetworkHandler().sendToServer(new ServerboundSaveScrollDataPacket(menu.containerId, auraTypes));
		super.onClose();
	}

	public void setScroll(ItemStack stack) {
		this.stack = stack;
	}

	private void takeScrollButtonShit(Button buttonWidget) {
		if(client.gameMode != null) {
			client.gameMode.handleInventoryButtonClick(menu.containerId, 0);
			onClose();
		}
	}

	private int pentagonX(int x, int index) {
		return x + (int) (Math.cos(angle * index - offset) * distance);
	}

	private int pentagonY(int y, int index) {
		return y + (int) (Math.sin(angle * index - offset) * distance);
	}

	private void drawLine(PoseStack matrices, float x1, float y1, float x2, float y2) {
		DataComponentType<Boolean> completedData = DevotionData.SCROLL_COMPLETED.get();
		DataComponentType<Long> completedTimeData = DevotionData.SCROLL_COMPLETED_TIME.get();
		float colour = 0f;

		if(client.level != null && stack.getOrDefault(completedData, false))
			colour = 0.25f + (float) Math.sin((client.level.getGameTime() - stack.getOrDefault(completedTimeData, 0).longValue()) * 0.15f) * 0.25f;

		RenderSystem.setShader(GameRenderer::getPositionShader);
		RenderSystem.setShaderColor(0.224f + colour, 0.196f + colour, 0.175f + colour, 1f);
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		Matrix4f matrix = matrices.last().pose();
		float angle = (float) (Math.atan2(y2 - y1, x2 - x1) - (Math.PI * 0.5));
		float dx = Mth.cos(angle) * 2;
		float dy = Mth.sin(angle) * 2;

		bufferBuilder.addVertex(matrix, x2 - dx, y2 - dy, 0).setColor(0);
		bufferBuilder.addVertex(matrix, x2 + dx, y2 + dy, 0).setColor(0);
		bufferBuilder.addVertex(matrix, x1 + dx, y1 + dy, 0).setColor(0);
		bufferBuilder.addVertex(matrix, x1 - dx, y1 - dy, 0).setColor(0);
		MeshData data = bufferBuilder.build();

		if(data != null)
			BufferUploader.drawWithShader(data);
	}

	public void redrawLines() {
		DataComponentType<List<Integer>> undoBufferData = DevotionData.UNDO_BUFFER.get();
		List<Integer> undoBuffer = stack.getOrDefault(undoBufferData, List.of());

		if(client.level != null) {
			lines.clear();
			auraTypes.clear();
			lineStart = null;

			if(!undoBuffer.isEmpty()) {
				for(int i = 0; i < undoBuffer.size() - 1; i++) {
					AuraType current = AuraType.values()[undoBuffer.get(i)];
					AuraType next = AuraType.values()[undoBuffer.get(i + 1)];
					Vec2 lineX = switch(current) {
						case ENHANCER -> enhancerPos;
						case TRANSMUTER -> transmuterPos;
						case EMITTER -> emitterPos;
						case CONJURER -> conjurerPos;
						case MANIPULATOR -> manipulatorPos;
						case SPECIALIST -> specialistPos;
						case NONE -> new Vec2(0, 0);
					};
					Vec2 lineY = switch(next) {
						case ENHANCER -> enhancerPos;
						case TRANSMUTER -> transmuterPos;
						case EMITTER -> emitterPos;
						case CONJURER -> conjurerPos;
						case MANIPULATOR -> manipulatorPos;
						case SPECIALIST -> specialistPos;
						case NONE -> new Vec2(0, 0);
					};

					lines.add(new Pair<>(lineX, lineY));
					auraTypes.add(current);

					if(i == undoBuffer.size() - 2)
						auraTypes.add(next);
				}
			}
		}
	}

	public void addLine(Vec2 pos1, Vec2 pos2, AuraType type) {
		for(Pair<Vec2, Vec2> line : lines)
			if((line.getFirst().equals(pos1) && line.getSecond().equals(pos2)) || (line.getFirst().equals(pos2) && line.getSecond().equals(pos1)))
				return;

		lines.add(new Pair<>(pos1, pos2));
		auraTypes.add(type);
	}
}
