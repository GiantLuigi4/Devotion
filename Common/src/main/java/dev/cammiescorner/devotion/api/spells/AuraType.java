package dev.cammiescorner.devotion.api.spells;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.common.Color;
import net.minecraft.util.StringRepresentable;

public enum AuraType implements StringRepresentable{
	ENHANCER("enhancement", 0x008100), TRANSMUTER("transmutation", 0xbb00c5), EMITTER("emission", 0xffd100),
	CONJURER("conjuration", 0xda0018), MANIPULATOR("manipulation", 0xe08600), SPECIALIST("specialization", 0x0070b9),
	NONE("none", 0xffffff);

	public static final Codec<AuraType> CODEC = StringRepresentable.fromEnum(AuraType::values);
	private final String name;
	private final Color color;

	AuraType(String name, int color) {
		this.color = new Color(color);
		this.name = name;
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
}
