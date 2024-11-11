package dev.cammiescorner.devotion.neoforge.entrypoints;

import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.AuraCapability;
import dev.cammiescorner.devotion.neoforge.common.capabilities.entity.KnownResearchCapability;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.EntityCapability;

@Mod(Devotion.MOD_ID)
public class NeoMain {
	public static final EntityCapability<AuraCapability, Void> AURA = EntityCapability.createVoid(
		Devotion.id("aura"), AuraCapability.class
	);
	public static final EntityCapability<KnownResearchCapability, Void> KNOWN_RESEARCH = EntityCapability.createVoid(
		Devotion.id("known_research"), KnownResearchCapability.class
	);

	public NeoMain(IEventBus modBus) {

	}
}
