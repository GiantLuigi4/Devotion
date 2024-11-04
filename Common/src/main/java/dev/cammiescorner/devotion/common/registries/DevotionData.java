package dev.cammiescorner.devotion.common.registries;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.Devotion;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;

public class DevotionData {
	public static final RegistryHandler<DataComponentType<?>> DATA_COMPONENTS = RegistryHandler.create(Registries.DATA_COMPONENT_TYPE, Devotion.MOD_ID);

	public static final RegistrySupplier<DataComponentType<Boolean>> CLOSED_HOOD = DATA_COMPONENTS.register("closed_hood", () -> DataComponentType.<Boolean>builder()
		.persistent(Codec.BOOL)
		.networkSynchronized(ByteBufCodecs.BOOL)
		.cacheEncoding()
		.build()
	);
}
