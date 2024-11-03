package dev.cammiescorner.devotion.api.actions;

import com.mojang.serialization.Codec;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.api.TriConsumer;
import dev.cammiescorner.devotion.common.blocks.entities.AltarFocusBlockEntity;
import dev.cammiescorner.devotion.common.registries.DevotionAltarActions;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public abstract class ConfiguredAltarAction {
	public static final ResourceKey<Registry<ConfiguredAltarAction>> REGISTRY_KEY = ResourceKey.createRegistryKey(Devotion.id("configured_altar_action"));
	public static final Codec<Holder<ConfiguredAltarAction>> CODEC = RegistryFixedCodec.create(REGISTRY_KEY);
	public static final Codec<ConfiguredAltarAction> DIRECT_CODEC = DevotionAltarActions.REGISTRY.byNameCodec().dispatch(ConfiguredAltarAction::getAltarAction, AltarAction::codec);
	public static final StreamCodec<RegistryFriendlyByteBuf, ConfiguredAltarAction> OBJ_STREAM_CODEC = ByteBufCodecs.registry(REGISTRY_KEY);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<ConfiguredAltarAction>> HOLDER_STREAM_CODEC = ByteBufCodecs.holderRegistry(REGISTRY_KEY);
	private final AltarAction altarAction;

	public ConfiguredAltarAction(AltarAction altarAction) {
		this.altarAction = altarAction;
	}

	public abstract void run(ServerLevel level, @Nullable ServerPlayer player, AltarFocusBlockEntity altar);

	public AltarAction getAltarAction() {
		return altarAction;
	}

	public static ConfiguredAltarAction of(TriConsumer<ServerLevel, @Nullable ServerPlayer, AltarFocusBlockEntity> consumer, AltarAction type) {
		return new ConfiguredAltarAction(type) {
			@Override
			public void run(ServerLevel level, @Nullable ServerPlayer player, AltarFocusBlockEntity altar) {
				consumer.accept(level, player, altar);
			}
		};
	}
}
