package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.blocks.AltarFocusBlock;
import dev.cammiescorner.devotion.common.blocks.AltarPillarBlock;
import dev.cammiescorner.devotion.common.blocks.entities.AltarFocusBlockEntity;
import dev.cammiescorner.devotion.common.blocks.entities.AltarPillarBlockEntity;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class DevotionBlocks {
	public static final RegistryHandler<Block> BLOCKS = RegistryHandler.create(Registries.BLOCK, Devotion.MOD_ID);
	public static final RegistryHandler<BlockEntityType<?>> BLOCK_ENTITIES = RegistryHandler.create(Registries.BLOCK_ENTITY_TYPE, Devotion.MOD_ID);

	public static final RegistrySupplier<Block> ALTAR_FOCUS_BLOCK = BLOCKS.register("altar_focus", () -> new AltarFocusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops().noOcclusion()));
	public static final RegistrySupplier<Block> ALTAR_PILLAR_BLOCK = BLOCKS.register("altar_pillar", () -> new AltarPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops().noOcclusion()));

	public static final RegistrySupplier<BlockEntityType<AltarFocusBlockEntity>> ALTAR_FOCUS_ENTITY = BLOCK_ENTITIES.register("altar_focus", () -> BlockEntityType.Builder.of(AltarFocusBlockEntity::new, ALTAR_FOCUS_BLOCK.get()).build(null));
	public static final RegistrySupplier<BlockEntityType<AltarPillarBlockEntity>> ALTAR_PILLAR_ENTITY = BLOCK_ENTITIES.register("altar_pillar", () -> BlockEntityType.Builder.of(AltarPillarBlockEntity::new, ALTAR_PILLAR_BLOCK.get()).build(null));
}
