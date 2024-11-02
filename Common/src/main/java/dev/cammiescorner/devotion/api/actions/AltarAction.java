package dev.cammiescorner.devotion.api.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.MapCodec;
import dev.cammiescorner.devotion.Devotion;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;

public abstract class AltarAction {
	public static final ResourceKey<Registry<AltarAction>> REGISTRY_KEY = ResourceKey.createRegistryKey(Devotion.id("altar_action"));

	public abstract ConfiguredAltarAction create(JsonObject json) throws JsonParseException;

	public abstract ConfiguredAltarAction create(RegistryFriendlyByteBuf buf);

	public boolean requiresPlayer() {
		return false;
	}

	public abstract MapCodec<ConfiguredAltarAction> codec();
}
