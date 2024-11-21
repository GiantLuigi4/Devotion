package dev.cammiescorner.devotion.api.spells;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.common.Color;
import net.minecraft.util.StringRepresentable;

public enum AuraType implements StringRepresentable {
	ENHANCER("enhancement", 0x009222), TRANSMUTER("transmutation", 0xcb00d4), EMITTER("emission", 0xffeC07),
	CONJURER("conjuration", 0x9c001d), MANIPULATOR("manipulation", 0xff9f00), SPECIALIST("specialization", 0x2847bb),
	NONE("none", 0xffffff);

	public static final Codec<AuraType> CODEC = StringRepresentable.fromEnum(AuraType::values);
	private final String name;
	private final Color color;

	AuraType(String name, int color) {
		this.color = new Color(color);
		this.name = name;
	}

	public boolean isSecondaryAffinity(AuraType primaryAuraType) {
		return switch(primaryAuraType) {
			case ENHANCER -> this == EMITTER || this == TRANSMUTER;
			case TRANSMUTER -> this == ENHANCER || this == CONJURER;
			case EMITTER -> this == MANIPULATOR || this == ENHANCER;
			case CONJURER -> this == TRANSMUTER || this == SPECIALIST;
			case MANIPULATOR -> this == SPECIALIST || this == EMITTER;
			case SPECIALIST -> this == CONJURER || this == MANIPULATOR;
			case NONE -> false;
		};
	}

	public boolean isTertiaryAffinity(AuraType primaryAuraType) {
		return switch(primaryAuraType) {
			case ENHANCER -> this == MANIPULATOR || this == CONJURER;
			case TRANSMUTER -> this == EMITTER || this == SPECIALIST;
			case EMITTER -> this == SPECIALIST || this == TRANSMUTER;
			case CONJURER -> this == MANIPULATOR || this == ENHANCER;
			case MANIPULATOR -> this == CONJURER || this == ENHANCER;
			case SPECIALIST -> this == EMITTER || this == TRANSMUTER;
			case NONE -> false;
		};
	}

	public Affinity getAffinity(AuraType primaryAuraType) {
		return this == primaryAuraType ? Affinity.PRIMARY : isSecondaryAffinity(primaryAuraType) ? Affinity.SECONDARY : isTertiaryAffinity(primaryAuraType) ? Affinity.TERTIARY : Affinity.OPPOSITE;
	}

	public float getAffinityMultiplier(AuraType primaryAuraType) {
		return getAffinity(primaryAuraType).getMultiplier();
	}

	public Color getColor() {
		return color;
	}

	public int getDecimal() {
		return color.getDecimal();
	}

	public float[] getRgbF() {
		return color.getRgbF();
	}

	public int[] getRgbI() {
		return color.getRgbI();
	}

	public String getName() {
		return name;
	}

	@Override
	public String getSerializedName() {
		return name;
	}

	public enum Affinity {
		PRIMARY(1f), SECONDARY(0.6f), TERTIARY(0.4f), OPPOSITE(0.2f);

		private final float multiplier;

		Affinity(float multiplier) {
			this.multiplier = multiplier;
		}

		public float getMultiplier() {
			return multiplier;
		}
	}
}
