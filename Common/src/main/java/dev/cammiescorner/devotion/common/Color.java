package dev.cammiescorner.devotion.common;

public class Color {
	private final int color;

	public Color(int color) {
		this.color = color;
	}

	public Color(int r, int g, int b) throws IllegalArgumentException {
		this((r << 16) + (g << 8) + b);

		if(r < 0 || r > 255 || g < 0 || g > 255 | b < 0 || b > 255)
			throw new IllegalArgumentException("All parameters have to be between 0 and 255");
	}

	public Color(float r, float g, float b) {
		this((int) (r * 255), (int) (g * 255), (int) (b * 255));
	}

	public int getDecimal() {
		return color;
	}

	public int[] getRgbI() {
		return new int[] {
			getRedI(),
			getGreenI(),
			getBlueI()
		};
	}

	public float[] getRgbF() {
		return new float[] {
			getRedF(),
			getGreenF(),
			getBlueF()
		};
	}

	public int getRedI() {
		return (color >> 16) & 0xff;
	}

	public int getGreenI() {
		return (color >> 8) & 0xff;
	}

	public int getBlueI() {
		return color & 0xff;
	}

	public float getRedF() {
		return getRedI() / 255f;
	}

	public float getGreenF() {
		return getGreenI() / 255f;
	}

	public float getBlueF() {
		return getBlueI() / 255f;
	}
}
