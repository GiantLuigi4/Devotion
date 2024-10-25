package dev.cammiescorner.devotion.common;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.registries.DevotionTags;
import dev.upcraft.sparkweave.api.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.HashMap;
import java.util.List;

public class MainHelper {
	private static final Duck duck = Services.getService(Duck.class);

	public static boolean isValidAltarBlock(BlockState state) {
		return state.is(DevotionTags.ALTAR_PALETTE);
	}

	public void constructStructureMap(MinecraftServer server) {
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
}
