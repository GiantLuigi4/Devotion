package dev.cammiescorner.devotion.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.MainHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

import static dev.cammiescorner.devotion.DevotionClient.client;

public class ResearchWidget extends AbstractButton {
	public static final ResourceLocation TEXTURE = Devotion.id("textures/gui/scripts_of_devotion_icons.png");
	private final Research research;
	private final OnPress onPress;
	private float offsetX = 0, offsetY = 0;

	public ResearchWidget(int x, int y, ResourceLocation researchId, OnPress onPress) {
		super(x, y, 30, 30, Component.empty());
		this.research = Research.getById(researchId);
		this.onPress = onPress;

		if(research != null) {
			Set<ResourceLocation> playerResearch = MainHelper.getResearchIds(client.player);

			if(playerResearch.contains(research.getId())) {
				active = true;
				visible = true;
			}
			else if(playerResearch.containsAll(research.getParentIds())) {
				active = true;
				visible = true;
			}
			else {
				active = false;

				if(research.isHidden() || research.getParents().stream().anyMatch(Research::isHidden))
					visible = false;
			}
		}
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		return active && visible && isHovered;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return active && visible && isHovered;
	}

	@Override
	public void onPress() {
		onPress.onPress(this);
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		Set<ResourceLocation> playerResearch = MainHelper.getResearchIds(client.player);

		if(research != null) {
			if(playerResearch.contains(research.getId()))
				visible = true;
			else if(playerResearch.containsAll(research.getParentIds()))
				visible = true;
			else if(research.isHidden() || research.getParents().stream().anyMatch(Research::isHidden))
				visible = false;

			if(visible) {
				isHovered = mouseX >= getX() + offsetX && mouseY >= getY() + offsetY && mouseX < getX() + offsetX + width && mouseY < getY() + offsetY + height;
				renderButton(guiGraphics, mouseX, mouseY, playerResearch);
			}
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
		defaultButtonNarrationText(narrationElementOutput);
	}

	public void renderButton(GuiGraphics guiGraphics, int mouseX, int mouseY, Set<ResourceLocation> playerResearch) {
		ItemStack stack = research.getIcon();
		PoseStack poseStack = guiGraphics.pose();
		int u;

		if(playerResearch.contains(research.getId())) {
			u = 60;
			active = true;
		}
		else if(playerResearch.containsAll(research.getParentIds())) {
			u = 30;
			active = true;
		}
		else {
			u = 0;
			active = false;
		}

		guiGraphics.blit(TEXTURE, getX(), getY(), u, 0, width, height);

		if(!research.getParents().stream().filter(parent -> playerResearch.containsAll(parent.getParentIds())).toList().isEmpty() || research.getParents().isEmpty())
			guiGraphics.renderItem(stack, getX() + 7, getY() + 7);
		else
			guiGraphics.blit(TEXTURE, getX() + 7, getY() + 7, 0, 32, 16, 16);

		if(isHoveredOrFocused())
			guiGraphics.renderTooltip(client.font, Screen.getTooltipFromItem(client, stack), stack.getTooltipImage(), mouseX, mouseY);
	}

	public void setOffset(float x, float y) {
		offsetX = x;
		offsetY = y;
	}

	public Research getResearch() {
		return research;
	}

	public interface OnPress {
		void onPress(ResearchWidget widget);
	}
}
