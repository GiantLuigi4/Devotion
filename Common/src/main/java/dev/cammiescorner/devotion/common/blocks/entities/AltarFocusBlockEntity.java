package dev.cammiescorner.devotion.common.blocks.entities;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.blocks.AltarFocusBlock;
import dev.cammiescorner.devotion.common.blocks.AltarPillarBlock;
import dev.cammiescorner.devotion.common.recipes.DevotionAltarRecipe;
import dev.cammiescorner.devotion.common.registries.DevotionBlocks;
import dev.upcraft.sparkweave.api.registry.RegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class AltarFocusBlockEntity extends BlockEntity implements RecipeInput {
	private static final int MAX_CRAFTING_TIME = 120;
	private static final List<BlockPos> PILLAR_OFFSETS = java.util.List.of(
		new BlockPos(0, 0, -4),  // enhancer pillar
		new BlockPos(4, 0, -1),  // transmuter pillar
		new BlockPos(-4, 0, -1), // emitter pillar
		new BlockPos(2, 0, 3),   // conjurer pillar
		new BlockPos(-2, 0, 3)   // manipulator pillar
	);
	private final Set<BlockPos> inWorldPillarPositions = new HashSet<>();
	private final NonNullList<ItemStack> inventory = NonNullList.withSize(10, ItemStack.EMPTY);
	private final Map<AuraType, Float> auraCosts = new HashMap<>();
	public final int tickOffset = new Random().nextInt(20);
	private DevotionAltarRecipe recipe;
	private ResourceLocation recipeId;
	private boolean crafting;
	private int craftingTime;

	public AltarFocusBlockEntity(BlockPos pos, BlockState blockState) {
		super(DevotionBlocks.ALTAR_FOCUS_ENTITY.get(), pos, blockState);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, AltarFocusBlockEntity altar) {
		if(!level.isClientSide()) {
			if(!altar.completed()) {
				if((level.getGameTime() + altar.tickOffset) % 10 == 0) {
					List<BlockPos> posList = getPillarOffsets(pos, state);

					if(posList.stream().allMatch(altar::isPillar)) {
						BlockState defaultPillarState = DevotionBlocks.ALTAR_PILLAR_BLOCK.get().defaultBlockState();
						BlockPos.MutableBlockPos pillarPos = new BlockPos.MutableBlockPos();

						for(BlockPos basePos : posList) {
							List<BlockState> storedStates = new ArrayList<>();
							pillarPos.set(basePos);

							for(int i = 0; i < 3; i++) {
								pillarPos.setY(basePos.getY() + i);
								storedStates.add(level.getBlockState(pillarPos));
								level.destroyBlock(pillarPos, false);
							}

							for(int i = 0; i < 3; i++) {
								pillarPos.setY(basePos.getY() + i);
								level.setBlockAndUpdate(pillarPos, defaultPillarState.setValue(AltarPillarBlock.LAYER, i));
							}

							level.getBlockEntity(basePos, DevotionBlocks.ALTAR_PILLAR_ENTITY.get()).ifPresent(pillarEntity -> {
								pillarEntity.setStoredBlocks(storedStates);
								pillarEntity.setAltarFocusPos(pos);
							});

							altar.inWorldPillarPositions.add(basePos);
							altar.notifyListeners();
						}
					}
				}
			}
			else {
				if(altar.recipeId != null) {
					Optional<RecipeHolder<?>> optional = level.getRecipeManager().byKey(altar.recipeId);

					if(optional.isPresent() && optional.get().value() instanceof DevotionAltarRecipe altarRecipe)
						altar.recipe = altarRecipe;
					else
						Devotion.LOGGER.error("Invalid Recipe ID: {}", altar.recipeId);

					altar.recipeId = null;
				}

				int filledSlots = altar.filledSlots();
				AABB box = state.getCollisionShape(level, pos).bounds().expandTowards(2, 0.4, 2).expandTowards(-3, 0, -3).move(altar.getBlockPos());
				List<ItemEntity> list = level.getEntities(EntityTypeTest.forClass(ItemEntity.class), box, itemEntity -> filledSlots < altar.size());

				if(!list.isEmpty()) {
					ItemEntity itemEntity = list.getFirst();
					ItemStack stack = itemEntity.getItem();

					altar.setItem(filledSlots, stack.split(1));

					if(stack.getCount() <= 0)
						itemEntity.discard();
				}

				if(altar.isCrafting()) {
					if(!altar.hasEnoughAura()) {
						AuraType nextAuraType = AuraType.ENHANCEMENT;

						while(nextAuraType.ordinal() < AuraType.SPECIALIZATION.ordinal()) {
							if(altar.getAura(nextAuraType) < altar.getAuraCost(nextAuraType)) {
								AltarPillarBlockEntity pillar = null;

								for(BlockPos blockPos : altar.inWorldPillarPositions) {
									if(level.getBlockEntity(blockPos) instanceof AltarPillarBlockEntity blockEntity && blockEntity.getContainedAuraType() == nextAuraType) {
										pillar = blockEntity;
										break;
									}
								}

								if(pillar != null && pillar.drainAura(1, false))
									altar.addPower(nextAuraType);
								else {
									// TODO do something to indicate it needs more aura
								}
							}
							else {
								nextAuraType = AuraType.values()[nextAuraType.ordinal() + 1];
							}
						}
					}
					else {
						if(altar.getCraftingProgress() >= 1f) {
							if(level instanceof ServerLevel serverLevel) {
								ServerPlayer player = serverLevel.getNearestEntity(ServerPlayer.class, TargetingConditions.forNonCombat(), null, altar.getBlockPos().getX() + 0.5, altar.getBlockPos().getY() + 0.5, altar.getBlockPos().getZ() + 0.5, box);

								if(player != null || !altar.recipe.requiresPlayer()) {
									altar.recipe.assemble(serverLevel, player, altar);
									altar.setCrafting(false);
								}
							}
						}

						altar.incrementCraftingTime();
					}
				}
			}
		}
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		inventory.clear();
		inWorldPillarPositions.clear();

		ListTag listTag = tag.getList("PillarPositions", Tag.TAG_INT_ARRAY);
		ContainerHelper.loadAllItems(tag, inventory, registries);
		crafting = tag.getBoolean("Active");
		craftingTime = tag.getInt("CraftingTime");

		for(int i = 0; i < listTag.size(); i++) {
			int[] xyz = listTag.getIntArray(i);

			if(xyz.length == 3)
				inWorldPillarPositions.add(new BlockPos(xyz[0], xyz[1], xyz[2]));
		}

		if(tag.contains("RecipeId", Tag.TAG_STRING)) {
			recipeId = ResourceLocation.parse(tag.getString("RecipeId"));
		}
		else {
			recipe = null;
			recipeId = null;
		}

		super.loadAdditional(tag, registries);
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		ListTag listTag = new ListTag();
		ContainerHelper.saveAllItems(tag, inventory, registries);
		tag.putBoolean("Active", crafting);
		tag.putInt("CraftingTime", craftingTime);

		for(BlockPos pos : inWorldPillarPositions)
			listTag.add(NbtUtils.writeBlockPos(pos));

		tag.put("PillarPositions", listTag);

		if(recipe != null) {
			ResourceLocation id = RegistryHelper.getBuiltinRegistry(Registries.RECIPE).getKey(recipe);

			if(id != null)
				tag.putString("RecipeId", id.toString());
			else
				tag.remove("RecipeId");
		}
		else
			tag.remove("RecipeId");

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

	@Override
	public ItemStack getItem(int index) {
		return inventory.get(index);
	}

	@Override
	public int size() {
		return inventory.size();
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}

	public ItemStack removeItem(int slot) {
		ItemStack stack = ContainerHelper.takeItem(inventory, slot);
		notifyListeners();

		return stack;
	}

	public void setItem(int slot, ItemStack stack) {
		inventory.set(slot, stack);
		notifyListeners();
	}

	public void clearContent() {
		inventory.clear();
		notifyListeners();
	}

	public void notifyListeners() {
		if(level != null && !level.isClientSide()) {
			if(isCrafting() && !recipe.matches(this, level)) {
				craftingTime = 0;
				auraCosts.clear();
				crafting = false;
				recipe = null;
			}

			setChanged();
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
		}
	}

	public int filledSlots() {
		int i = 0;

		while(i < size() && !getItem(i).isEmpty())
			++i;

		return i;
	}

	public boolean isCrafting() {
		return crafting;
	}

	public void setCrafting(boolean crafting) {
		this.crafting = crafting;

		if(!crafting) {
			craftingTime = 0;
			auraCosts.clear();
			recipe = null;
		}

		notifyListeners();
	}

	public boolean isPillar(BlockPos pillarPos) {
		BlockState baseState = level.getBlockState(pillarPos);
		BlockState middleState = level.getBlockState(pillarPos.above());
		BlockState capState = level.getBlockState(pillarPos.above(2));

		return baseState.is(DevotionBlocks.ALTAR_PILLAR_BLOCK.get()) || (baseState.is(Blocks.STONE) && middleState.is(Blocks.STONE) && capState.is(Blocks.COPPER_BLOCK));
	}

	public void incrementCraftingTime() {
		craftingTime++;
		notifyListeners();
	}

	public static List<BlockPos> getPillarOffsets(BlockPos pos, BlockState state) {
		return PILLAR_OFFSETS.stream().map(blockPos -> StructureTemplate.transform(blockPos.offset(pos), Mirror.NONE, state.getValue(AltarFocusBlock.ROTATION), pos)).toList();
	}

	public boolean completed() {
		return !inWorldPillarPositions.isEmpty() && inWorldPillarPositions.stream().allMatch(blockPos -> level.getBlockState(blockPos).is(DevotionBlocks.ALTAR_PILLAR_BLOCK.get()));
	}

	public float getAuraCost(AuraType auraType) {
		return recipe != null ? recipe.getAuraCosts().get(auraType) : 0f;
	}

	public float getAura(AuraType auraType) {
		return auraCosts.get(auraType);
	}

	public boolean hasEnoughAura() {
		return isCrafting() && Stream.of(AuraType.ENHANCEMENT, AuraType.TRANSMUTATION, AuraType.EMISSION, AuraType.CONJURATION, AuraType.MANIPULATION).allMatch(auraType -> getAura(auraType) >= getAuraCost(auraType));
	}

	public void addPower(AuraType auraType) {
		auraCosts.compute(auraType, (key, value) -> Math.min(value + 1, getAuraCost(auraType)));
		notifyListeners();
	}

	public float getCraftingProgress() {
		return Math.clamp((float) craftingTime / MAX_CRAFTING_TIME, 0, 1);
	}
}
