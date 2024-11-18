package dev.cammiescorner.devotion.common.blocks.entities;

import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.registries.DevotionBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AltarPillarBlockEntity extends BlockEntity {
	private static final float MAX_AURA = 1000f;
	private BlockPos altarFocusPos = BlockPos.ZERO;
	private AuraType containedAuraType = AuraType.NONE;
	private float storedAura = 0f;

	public AltarPillarBlockEntity(BlockPos pos, BlockState blockState) {
		super(DevotionBlocks.ALTAR_PILLAR_ENTITY.get(), pos, blockState);
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		altarFocusPos = new BlockPos(tag.getInt("AltarPosX"), tag.getInt("AltarPosY"), tag.getInt("AltarPosZ"));
		containedAuraType = AuraType.valueOf(tag.getString("ContainedAuraType"));
		storedAura = tag.getFloat("StoredAura");
		super.loadAdditional(tag, registries);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		tag.putInt("AltarPosX", altarFocusPos.getX());
		tag.putInt("AltarPosY", altarFocusPos.getY());
		tag.putInt("AltarPosZ", altarFocusPos.getZ());
		tag.putString("ContainedAuraType", containedAuraType.name());
		tag.putFloat("StoredAura", storedAura);
		super.saveAdditional(tag, registries);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		CompoundTag tag = super.getUpdateTag(registries);
		saveAdditional(tag, registries);

		return tag;
	}

	@Override
	public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public void notifyListeners() {
		if(level != null && !level.isClientSide()) {
			setChanged();
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}

	public BlockPos getAltarFocusPos() {
		return altarFocusPos;
	}

	public void setAltarFocusPos(BlockPos pos) {
		altarFocusPos = pos;
		notifyListeners();
	}

	public AuraType getContainedAuraType() {
		return containedAuraType;
	}

	public boolean addAura(float amount, boolean simulate) {
		if(storedAura < MAX_AURA) {
			if(!simulate) {
				storedAura += amount;
				notifyListeners();
			}

			return true;
		}

		return false;
	}

	public boolean drainAura(float amount, boolean simulate) {
		if(storedAura - amount >= 0) {
			if(!simulate) {
				storedAura -= amount;
				notifyListeners();
			}

			return true;
		}

		return false;
	}
}
