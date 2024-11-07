package dev.cammiescorner.devotion.common;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.common.registries.DevotionTags;
import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainHelper {
	private static final Duck duck = Services.getService(Duck.class);

	public static StructureMapData getStructureMapData() {
		return Devotion.data;
	}

	public static boolean isValidAltarBlock(BlockState state) {
		return state.is(DevotionTags.ALTAR_PALETTE);
	}

	public static void constructStructureMap(MinecraftServer server) {
		StructureTemplate structureTemplate = server.getStructureManager().getOrCreate(Devotion.id("altar"));
		StructurePlaceSettings placementData = new StructurePlaceSettings();
		BlockPos pos = BlockPos.ZERO;

		List<StructureTemplate.StructureBlockInfo> randInfoList = placementData.getRandomPalette(structureTemplate.palettes, pos).blocks();
		List<StructureTemplate.StructureBlockInfo> infoList = StructureTemplate.processBlockInfos(server.overworld(), pos, pos, placementData, randInfoList);
		HashMap<BlockPos, BlockState> map = new HashMap<>();
		int offsetX = (int) Math.floor(structureTemplate.getSize().getX() / 2d);
		int offsetZ = (int) Math.floor(structureTemplate.getSize().getZ() / 2d);

		for(StructureTemplate.StructureBlockInfo info : infoList)
			if(MainHelper.isValidAltarBlock(info.state()))
				map.put(info.pos().subtract(pos), info.state());

		duck.setStructureMap(server, new StructureMapData(map, offsetX, offsetZ));
	}

	public static float getAura(LivingEntity entity) {
		return duck.getAura(entity);
	}

	public static void setAura(LivingEntity entity, float amount) {
		duck.setAura(entity, amount);
	}

	public static Color getAuraColor(LivingEntity entity) {
		return duck.getAuraColor(entity);
	}

	public static void setAuraColor(LivingEntity entity, Color color) {
		duck.setAuraColor(entity, color);
	}

	public static float getAuraAlpha(LivingEntity entity) {
		return duck.getAuraAlpha(entity);
	}

	public static boolean drainAura(LivingEntity entity, float amount, boolean simulate) {
		return duck.drainAura(entity, amount, simulate);
	}

	public static boolean regenAura(LivingEntity entity, float amount, boolean simulate) {
		return duck.regenAura(entity, amount, simulate);
	}

	public static Set<ResourceLocation> getResearchIds(Player player) {
		return duck.getResearchIds(player);
	}

	public static boolean giveResearch(Player player, Research research, boolean simulate) {
		return duck.giveResearch(player, research, simulate);
	}

	public static boolean revokeResearch(Player player, Research research, boolean simulate) {
		return duck.revokeResearch(player, research, simulate);
	}
}
