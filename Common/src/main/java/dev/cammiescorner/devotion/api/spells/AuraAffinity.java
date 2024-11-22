package dev.cammiescorner.devotion.api.spells;

public enum AuraAffinity {
	PRIMARY(1f), SECONDARY(0.6f), TERTIARY(0.4f), OPPOSITE(0.2f);

	private final float multiplier;

	AuraAffinity(float multiplier) {
		this.multiplier = multiplier;
	}

	public float getMultiplier() {
		return multiplier;
	}
}
