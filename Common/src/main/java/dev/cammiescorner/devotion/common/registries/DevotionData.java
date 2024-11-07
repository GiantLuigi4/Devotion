package dev.cammiescorner.devotion.common.registries;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.research.Research;
import dev.cammiescorner.devotion.api.research.RiddleData;
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
	public static final RegistrySupplier<DataComponentType<Boolean>> SCROLL_COMPLETED = DATA_COMPONENTS.register("scroll_completed", () -> DataComponentType.<Boolean>builder()
		.persistent(Codec.BOOL)
		.networkSynchronized(ByteBufCodecs.BOOL)
		.cacheEncoding()
		.build()
	);
	public static final RegistrySupplier<DataComponentType<Research>> RESEARCH = DATA_COMPONENTS.register("research", () -> DataComponentType.<Research>builder()
		.persistent(Research.DIRECT_CODEC)
		.networkSynchronized(Research.OBJ_STREAM_CODEC)
		.cacheEncoding()
		.build()
	);
	public static final RegistrySupplier<DataComponentType<RiddleData>> RIDDLE_DATA = DATA_COMPONENTS.register("riddle_data", () -> DataComponentType.<RiddleData>builder()
		.persistent(RiddleData.CODEC)
		.networkSynchronized(RiddleData.STREAM_CODEC)
		.cacheEncoding()
		.build()
	);
}
