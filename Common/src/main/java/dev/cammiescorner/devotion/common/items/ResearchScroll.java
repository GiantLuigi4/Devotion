package dev.cammiescorner.devotion.common.items;

import com.mojang.datafixers.util.Pair;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.Graph;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.research.RiddleData;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import dev.cammiescorner.devotion.common.registries.DevotionItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.random.RandomGenerator;

public class ResearchScroll extends Item {
	public ResearchScroll() {
		super(new Properties().stacksTo(1).component(DevotionData.RESEARCH.get(), Research.getById(Devotion.id("empty"))));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		if(!level.isClientSide()) {
			ItemStack stack = player.getItemInHand(usedHand);

			if(stack.getOrDefault(DevotionData.SCROLL_COMPLETED.get(), false) && stack.has(DevotionData.RESEARCH.get())) {
				Research research = stack.get(DevotionData.RESEARCH.get());

				if(MainHelper.getResearchIds(player).containsAll(research.getParentIds())) {
					if(MainHelper.giveResearch(player, research, false)) {
						stack.consume(1, player);
						return InteractionResultHolder.success(stack);
					}
					else {
						player.displayClientMessage(Component.translatable("research_error." + Devotion.MOD_ID + ".already_known").withStyle(ChatFormatting.RED), true);
						return InteractionResultHolder.fail(stack);
					}
				}
				else {
					player.displayClientMessage(Component.translatable("research_error." + Devotion.MOD_ID + ".dont_know_parents").withStyle(ChatFormatting.RED), true);
					return InteractionResultHolder.fail(stack);
				}
			}
			else {
				return InteractionResultHolder.fail(stack);
			}
		}

		return super.use(level, player, usedHand);
	}

	@Override
	public Component getName(ItemStack stack) {
		DataComponentType<Research> data = DevotionData.RESEARCH.get();

		if(stack.get(data) instanceof Research research)
			return super.getName(stack).copy().append(" (").append(Component.translatable(research.getTranslationKey())).append(")");

		return super.getName(stack);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return stack.getOrDefault(DevotionData.SCROLL_COMPLETED.get(), false);
	}

	public static ItemStack createScroll(Research research, RandomGenerator random) {
		List<Graph.Node<AuraType>> path = new ArrayList<>();
		HashSet<Graph.Edge<AuraType>> visitedEdges = new HashSet<>();
		Research.Difficulty difficulty = research != null ? research.getDifficulty() : Research.Difficulty.EASY;
		int maxRiddles = difficulty == Research.Difficulty.EASY ? 4 : difficulty == Research.Difficulty.NORMAL ? 6 : 8;

		// pick random starting node
		path.add(Devotion.AURA_GRAPH.nodes.get(random.nextInt(Devotion.AURA_GRAPH.nodes.size())));

		while(path.size() < maxRiddles) {
			Graph.Node<AuraType> current = path.getLast();
			List<Graph.Edge<AuraType>> connections = current.getConnections();

			if(visitedEdges.containsAll(connections)) {
				// path too short, go back 1 and try again
				path.removeLast();
				visitedEdges.removeIf(e -> e.nodes.contains(current) && e.nodes.contains(path.getLast()));
				continue;
			}

			Graph.Edge<AuraType> next = connections.get(random.nextInt(connections.size()));

			if(visitedEdges.contains(next))
				continue;

			visitedEdges.add(next);
			path.add(next.nodes.stream().filter(n -> n != current).findFirst().orElseThrow(() -> new IllegalStateException("invalid graph")));
		}

		ItemStack stack = new ItemStack(DevotionItems.RESEARCH_SCROLL.get());
		RiddleData riddleData = new RiddleData(path.stream().map(auraTypeNode -> Pair.of(auraTypeNode.obj, random.nextInt(9))).toList());

		stack.set(DevotionData.RIDDLE_DATA.get(), riddleData);

		return stack;
	}
}
