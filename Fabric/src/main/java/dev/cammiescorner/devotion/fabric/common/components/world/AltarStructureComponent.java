package dev.cammiescorner.devotion.fabric.common.components.world;

import com.mojang.datafixers.util.Pair;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.devotion.fabric.common.registries.DevotionComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.Scoreboard;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class AltarStructureComponent implements AutoSyncedComponent {
	private final Scoreboard scoreboard;
	private final MinecraftServer server;
	private StructureMapData data = StructureMapData.empty();

	public AltarStructureComponent(Scoreboard scoreboard, MinecraftServer server) {
		this.scoreboard = scoreboard;
		this.server = server;
	}

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		data = StructureMapData.CODEC.decode(NbtOps.INSTANCE, tag.get("StructureDataMap")).result().map(Pair::getFirst).orElseThrow();
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
		tag.put("StructureMapData", StructureMapData.CODEC.encodeStart(NbtOps.INSTANCE, data).getOrThrow());
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void applySyncPacket(RegistryFriendlyByteBuf buf) {
		AutoSyncedComponent.super.applySyncPacket(buf);
		Devotion.data = data;
	}

	public void setStructureMap(StructureMapData data) {
		this.data = data;
		DevotionComponents.ALTAR_STRUCTURE.sync(scoreboard);
	}
}
