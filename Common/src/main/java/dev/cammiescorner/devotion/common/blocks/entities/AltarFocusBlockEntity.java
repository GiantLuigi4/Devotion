package dev.cammiescorner.devotion.common.blocks.entities;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.StructureMapData;
import dev.cammiescorner.devotion.common.recipes.DevotionAltarRecipe;
import dev.cammiescorner.devotion.common.registries.DevotionBlocks;
import dev.upcraft.sparkweave.api.registry.RegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AltarFocusBlockEntity extends BlockEntity implements RecipeInput, Container {
	public static final List<BlockPos> AMETHYST_POS_LIST = List.of(
		new BlockPos(0, 1, -3), new BlockPos(3, 1, -3), new BlockPos(3, 1, 0), new BlockPos(3, 1, 3),
		new BlockPos(0, 1, 3), new BlockPos(-3, 1, 3), new BlockPos(-3, 1, 0), new BlockPos(-3, 1, -3)
	);
	private final NonNullList<ItemStack> inventory = NonNullList.withSize(10, ItemStack.EMPTY);
	private boolean crafting, completed, hideSchematic;
	private int power, craftingTime, amethystIndex;
	private DevotionAltarRecipe recipe;
	private ResourceLocation recipeId;

	public AltarFocusBlockEntity(BlockPos pos, BlockState blockState) {
		super(DevotionBlocks.ALTAR_ENTITY.get(), pos, blockState);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, AltarFocusBlockEntity altar) {
		if(!level.isClientSide() && altar.isCompleted()) {
			if(altar.recipeId != null) {
				Optional<RecipeHolder<?>> optional = level.getRecipeManager().byKey(altar.recipeId);

				if(optional.isPresent() && optional.get().value() instanceof DevotionAltarRecipe altarRecipe)
					altar.recipe = altarRecipe;
				else
					Devotion.LOGGER.error("Invalid Recipe ID: {}", altar.recipeId);

				altar.recipeId = null;
			}

			if(level.getGameTime() % 20 == 0)
				altar.checkMultiblock();

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
				if(altar.power < altar.getRequiredPower()) {
					BlockPos amethystPos = AMETHYST_POS_LIST.get(altar.getAmethystIndex()).offset(altar.getBlockPos());
					BlockState amethystState = level.getBlockState(amethystPos);

					if(!(amethystState.getBlock() instanceof AmethystClusterBlock)) {
						altar.amethystIndex++;
						return;
					}

					if(level.getGameTime() % altar.eatAmethystSpeed() == 0) {
						if(!level.isClientSide()) {
							level.destroyBlock(amethystPos, false);

							switch(BuiltInRegistries.BLOCK.getKey(amethystState.getBlock()).toString()) {
								case "minecraft:amethyst_cluster" ->
									level.setBlockAndUpdate(amethystPos, Blocks.LARGE_AMETHYST_BUD.defaultBlockState());
								case "minecraft:large_amethyst_bud" ->
									level.setBlockAndUpdate(amethystPos, Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState());
								case "minecraft:medium_amethyst_bud" ->
									level.setBlockAndUpdate(amethystPos, Blocks.SMALL_AMETHYST_BUD.defaultBlockState());
								case "minecraft:small_amethyst_bud" ->
									level.setBlockAndUpdate(amethystPos, Blocks.AIR.defaultBlockState());
								default -> {
								}
							}
						}

						altar.power++;
						altar.amethystIndex++;
					}
				}
				else {
					if(altar.getCraftingTime() >= 120) {
						if(level instanceof ServerLevel serverLevel) {
							ServerPlayer player = serverLevel.getNearestEntity(ServerPlayer.class, TargetingConditions.forNonCombat(), null, altar.getBlockPos().getX() + 0.5, altar.getBlockPos().getY() + 0.5, altar.getBlockPos().getZ() + 0.5, box);

							if(player != null || !altar.recipe.requiresPlayer()) {
								altar.recipe.assemble(serverLevel, player, altar);
								altar.setCrafting(false);
							}
						}
					}
				}
			}
		}

		if(altar.isCompleted() && altar.isCrafting() && altar.power >= altar.getRequiredPower())
			altar.craftingTime++;
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		inventory.clear();
		ContainerHelper.loadAllItems(tag, inventory, registries);
		completed = tag.getBoolean("Completed");
		crafting = tag.getBoolean("Active");
		hideSchematic = tag.getBoolean("HideSchematic");
		power = tag.getInt("Power");
		craftingTime = tag.getInt("CraftingTime");
		amethystIndex = tag.getInt("AmethystIndex");

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
		ContainerHelper.saveAllItems(tag, inventory, registries);
		tag.putBoolean("Completed", completed);
		tag.putBoolean("Active", crafting);
		tag.putBoolean("HideSchematic", hideSchematic);
		tag.putInt("Power", power);
		tag.putInt("CraftingTime", craftingTime);
		tag.putInt("AmethystIndex", amethystIndex);

		ResourceLocation id = RegistryHelper.getBuiltinRegistry(Registries.RECIPE).getKey(recipe);

		if(id != null)
			tag.putString("RecipeId", id.toString());
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
	public int getContainerSize() {
		return inventory.size();
	}

	@Override
	public ItemStack getItem(int index) {
		return inventory.get(index);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack stack = ContainerHelper.removeItem(inventory, slot, amount);
		notifyListeners();

		return stack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(inventory, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		inventory.set(slot, stack);
		notifyListeners();
	}

	@Override
	public boolean stillValid(Player player) {
		return !(player.distanceToSqr(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5) > 64);
	}

	@Override
	public int size() {
		return getContainerSize();
	}

	@Override
	public void clearContent() {
		inventory.clear();
		notifyListeners();
	}

	@Override
	public boolean isEmpty() {
		return inventory.isEmpty();
	}

	public void notifyListeners() {
		if(level != null && !level.isClientSide()) {
			if(isCrafting() && !recipe.matches(this, level)) {
				craftingTime = 0;
				power = 0;
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
			power = 0;
			recipe = null;
		}

		notifyListeners();
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
		notifyListeners();
	}

	public boolean isSchematicHidden() {
		return hideSchematic;
	}

	public void hideSchematic(boolean hide) {
		this.hideSchematic = hide;
	}

	public void checkMultiblock() {
		if(level != null && !level.isClientSide()) {
			StructureMapData structureData = MainHelper.getStructureMapData();
			HashMap<BlockPos, BlockState> structure = new HashMap<>();
			HashMap<BlockPos, BlockState> template = structureData.structureMap();

			for(Map.Entry<BlockPos, BlockState> entry : template.entrySet()) {
				BlockPos.MutableBlockPos pos = getBlockPos().mutable().move(structureData.offsetX(), 0, structureData.offsetZ()).move(entry.getKey());
				BlockState state = level.getBlockState(pos);

				if(MainHelper.isValidAltarBlock(state))
					structure.put(entry.getKey(), state);
			}

			setCompleted(structure.equals(template));
		}
	}

	public int getPower() {
		return power;
	}

	public int getAmethystIndex() {
		if(amethystIndex > 7)
			amethystIndex = 0;

		return amethystIndex;
	}

	public int getRequiredPower() {
		return recipe != null ? recipe.getPower() : 0;
	}

	public int eatAmethystSpeed() {
		return recipe != null ? Math.min(10, 160 / recipe.getPower()) : 30;
	}

	public int getCraftingTime() {
		return Math.min(craftingTime, 120);
	}
}
