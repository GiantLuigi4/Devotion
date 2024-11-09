package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.DevotionClient;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.client.gui.screens.ScriptsOfDevotionScreen;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;

public class ScriptsOfDevotionItem extends Item {
	public ScriptsOfDevotionItem() {
		super(new Properties().stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);

		Registry<Research> registry = player.registryAccess().registryOrThrow(Research.REGISTRY_KEY);

		if(level.isClientSide()) {
			for(Map.Entry<ResourceKey<Research>, Research> entry : registry.entrySet()) {
				System.out.println(entry.getKey().location());
			}

			DevotionClient.client.setScreen(new ScriptsOfDevotionScreen());
		}

		return InteractionResultHolder.success(stack);
	}
}
