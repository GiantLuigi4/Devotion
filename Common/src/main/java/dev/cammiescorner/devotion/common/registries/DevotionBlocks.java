package dev.cammiescorner.devotion.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.blocks.AltarFocusBlock;
import dev.cammiescorner.devotion.common.blocks.entities.AltarFocusBlockEntity;
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

	public static final RegistrySupplier<Block> ALTAR_BLOCK = BLOCKS.register("altar_focus", () -> new AltarFocusBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).requiresCorrectToolForDrops().noOcclusion()));
	public static final RegistrySupplier<BlockEntityType<AltarFocusBlockEntity>> ALTAR_ENTITY = BLOCK_ENTITIES.register("altar_focus", () -> BlockEntityType.Builder.of(AltarFocusBlockEntity::new, ALTAR_BLOCK.get()).build(null));
}
