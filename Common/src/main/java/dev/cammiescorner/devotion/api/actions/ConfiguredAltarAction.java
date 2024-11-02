package dev.cammiescorner.devotion.api.actions;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.TriConsumer;
import dev.cammiescorner.devotion.common.blocks.entities.AltarBlockEntity;
import dev.cammiescorner.devotion.common.registries.DevotionAltarActions;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface ConfiguredAltarAction {
	public static final ResourceKey<Registry<ConfiguredAltarAction>> REGISTRY_KEY = ResourceKey.createRegistryKey(Devotion.id("configured_altar_action"));
	public static final Codec<Holder<ConfiguredAltarAction>> CODEC = RegistryFixedCodec.create(REGISTRY_KEY);
	public static final Codec<ConfiguredAltarAction> DIRECT_CODEC = DevotionAltarActions.REGISTRY.byNameCodec().dispatch(ConfiguredAltarAction::getType, AltarAction::codec);

	void run(ServerLevel level, @Nullable ServerPlayer player, AltarBlockEntity altar);

	AltarAction getType();

	static ConfiguredAltarAction of(TriConsumer<ServerLevel, @Nullable ServerPlayer, AltarBlockEntity> consumer, AltarAction type) {
		return new ConfiguredAltarAction() {
			@Override
			public void run(ServerLevel level, @Nullable ServerPlayer player, AltarBlockEntity altar) {
				consumer.accept(level, player, altar);
			}

			@Override
			public AltarAction getType() {
				return type;
			}
		};
	}
}
