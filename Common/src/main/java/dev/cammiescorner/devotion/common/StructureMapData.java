package dev.cammiescorner.devotion.common;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public record StructureMapData(HashMap<BlockPos, BlockState> structureMap, int offsetX, int offsetZ) {
	public static final Codec<StructureMapData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.compoundList(BlockPos.CODEC, BlockState.CODEC).xmap(pairs -> {
			HashMap<BlockPos, BlockState> map = new HashMap<>();

			for(Pair<BlockPos, BlockState> pair : pairs)
				map.put(pair.getFirst(), pair.getSecond());

			return map;
		}, map -> map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).toList()).fieldOf("structureMap").forGetter(StructureMapData::structureMap),
		Codec.INT.fieldOf("offsetX").forGetter(StructureMapData::offsetX),
		Codec.INT.fieldOf("offsetZ").forGetter(StructureMapData::offsetZ)
	).apply(instance, StructureMapData::new));

	public static StructureMapData empty() {
		return new StructureMapData(new HashMap<>(), 0, 0);
	}
}
