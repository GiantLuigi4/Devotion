package dev.cammiescorner.devotion.common.items;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.Graph;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.registries.DevotionData;
import it.unimi.dsi.fastutil.Pair;
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

	public static List<Pair<AuraType, Integer>> generateRiddle(RandomGenerator random, int maxRiddleLength) {
		List<Graph.Node<AuraType>> path = new ArrayList<>();
		HashSet<Graph.Edge<AuraType>> visitedEdges = new HashSet<>();

		// pick random starting node
		path.add(Devotion.AURA_GRAPH.nodes.get(random.nextInt(Devotion.AURA_GRAPH.nodes.size())));

		while(path.size() < maxRiddleLength) {
			Graph.Node<AuraType> current = path.getLast();
			List<Graph.Edge<AuraType>> connections = current.getConnections();

			if(visitedEdges.containsAll(connections)) {
				// path too short, go back 1 and try again
				path.removeLast();
				visitedEdges.removeIf(e -> e.nodes.contains(current) && e.nodes.contains(path.get(path.size() - 1)));
				continue;
			}

			Graph.Edge<AuraType> next = connections.get(random.nextInt(connections.size()));

			if(visitedEdges.contains(next))
				continue;

			visitedEdges.add(next);
			path.add(next.nodes.stream().filter(n -> n != current).findFirst().orElseThrow(() -> new IllegalStateException("invalid graph")));
		}

		return path.stream().map(auraTypeNode -> Pair.of(auraTypeNode.obj, random.nextInt(9))).toList();
	}
}
