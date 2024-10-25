package dev.cammiescorner.devotion.fabric.common.registries;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.fabric.common.components.world.AltarStructureComponent;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

public class DevotionComponents implements EntityComponentInitializer, ScoreboardComponentInitializer {
	public static final ComponentKey<AltarStructureComponent> ALTAR_STRUCTURE_COMPONENT = createComponent("altar_structure", AltarStructureComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {

	}

	@Override
	public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry registry) {
		registry.registerScoreboardComponent(ALTAR_STRUCTURE_COMPONENT, AltarStructureComponent::new);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(Devotion.id(name), component);
	}
}
