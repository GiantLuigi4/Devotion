package dev.cammiescorner.devotion.common.blocks.properties;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RotationProperty extends EnumProperty<Rotation> {
	public RotationProperty(String name, Collection<Rotation> values) {
		super(name, Rotation.class, values);
	}

	public static RotationProperty create(String name) {
		return create(name, rotation -> true);
	}

	public static RotationProperty create(String name, Predicate<Rotation> filter) {
		return create(name, Arrays.stream(Rotation.values()).filter(filter).collect(Collectors.toList()));
	}

	public static RotationProperty create(String name, Rotation... values) {
		return create(name, Lists.newArrayList(values));
	}

	public static RotationProperty create(String name, Collection<Rotation> values) {
		return new RotationProperty(name, values);
	}
}
