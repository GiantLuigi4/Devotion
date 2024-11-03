package dev.cammiescorner.devotion.common.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import dev.cammiescorner.devotion.api.actions.AltarAction;
import dev.cammiescorner.devotion.api.actions.ConfiguredAltarAction;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class EmptyAltarAction extends AltarAction {
	@Override
	public ConfiguredAltarAction create(JsonObject json) throws JsonParseException {
		return ConfiguredAltarAction.of((world, player, altar) -> {}, this);
	}

	@Override
	public ConfiguredAltarAction create(RegistryFriendlyByteBuf buf) {
		return ConfiguredAltarAction.of((world, player, altar) -> {}, this);
	}

	@Override
	public MapCodec<ConfiguredAltarAction> codec() {
		return MapCodec.of(Encoder.empty(), Decoder.unit(ConfiguredAltarAction.of((level, serverPlayer, altarBlockEntity) -> {}, this)));
	}
}
