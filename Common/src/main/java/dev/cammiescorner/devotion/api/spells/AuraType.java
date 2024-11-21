package dev.cammiescorner.devotion.api.spells;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.Color;
import net.minecraft.util.StringRepresentable;

public enum AuraType implements StringRepresentable {
	ENHANCEMENT("enhancement", 0x009222), TRANSMUTATION("transmutation", 0xcb00d4), EMISSION("emission", 0xffeC07),
	CONJURATION("conjuration", 0x9c001d), MANIPULATION("manipulation", 0xff9f00), SPECIALIZATION("specialization", 0x2847bb),
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
			case ENHANCEMENT -> this == EMISSION || this == TRANSMUTATION;
			case TRANSMUTATION -> this == ENHANCEMENT || this == CONJURATION;
			case EMISSION -> this == MANIPULATION || this == ENHANCEMENT;
			case CONJURATION -> this == TRANSMUTATION || this == SPECIALIZATION;
			case MANIPULATION -> this == SPECIALIZATION || this == EMISSION;
			case SPECIALIZATION -> this == CONJURATION || this == MANIPULATION;
			case NONE -> false;
		};
	}

	public boolean isTertiaryAffinity(AuraType primaryAuraType) {
		return switch(primaryAuraType) {
			case ENHANCEMENT -> this == MANIPULATION || this == CONJURATION;
			case TRANSMUTATION -> this == EMISSION || this == SPECIALIZATION;
			case EMISSION -> this == SPECIALIZATION || this == TRANSMUTATION;
			case CONJURATION -> this == MANIPULATION || this == ENHANCEMENT;
			case MANIPULATION -> this == CONJURATION || this == ENHANCEMENT;
			case SPECIALIZATION -> this == EMISSION || this == TRANSMUTATION;
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

	public static AuraType byName(String name) {
		for(AuraType auraType : AuraType.values()) {
			if(auraType.getSerializedName().equals(name))
				return auraType;
		}

		Devotion.LOGGER.warn("Aura Type with name {} doesn't exist! Defaulting to NONE.", name);
		return NONE;
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
