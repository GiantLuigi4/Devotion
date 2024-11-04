package dev.cammiescorner.devotion.neoforge.common.capabilities.level;

import com.mojang.datafixers.util.Pair;
import commonnetwork.api.Network;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.devotion.common.networking.s2c.ClientboundAltarStructurePacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class AltarStructureCapability extends SavedData {
	private final ServerLevel level;
	private StructureMapData data = StructureMapData.empty();

	public AltarStructureCapability(ServerLevel level) {
		this.level = level;
	}

	public static AltarStructureCapability getInstance(MinecraftServer server) {
		ServerLevel level = server.overworld();

		return level.getDataStorage().computeIfAbsent(new Factory<>(() -> new AltarStructureCapability(level), (compoundTag, provider) -> load(level, compoundTag, provider)), Devotion.id("altar_structure").toDebugFileName());
	}

	@Override
	public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
		tag.put("StructureMapData", StructureMapData.CODEC.encodeStart(NbtOps.INSTANCE, data).getOrThrow());

		return tag;
	}

	private static AltarStructureCapability load(ServerLevel level, CompoundTag tag, HolderLookup.Provider provider) {
		AltarStructureCapability capability = new AltarStructureCapability(level);

		capability.data = StructureMapData.CODEC.decode(NbtOps.INSTANCE, tag.get("StructureDataMap")).result().map(Pair::getFirst).orElseThrow();

		return capability;
	}

	public void setStructureMap(StructureMapData data) {
		this.data = data;
		Network.getNetworkHandler().sendToAllClients(new ClientboundAltarStructurePacket(data), level.getServer());
	}
}
