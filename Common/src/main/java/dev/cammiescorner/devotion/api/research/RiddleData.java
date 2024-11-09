package dev.cammiescorner.devotion.api.research;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.api.spells.AuraType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record RiddleData(List<Pair<AuraType, Integer>> riddles) {
	public static final Codec<RiddleData> CODEC = RecordCodecBuilder.create(riddleDataInstance -> riddleDataInstance.group(
		Codec.compoundList(AuraType.CODEC, Codec.INT).fieldOf("riddles").forGetter(RiddleData::riddles)
	).apply(riddleDataInstance, RiddleData::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, RiddleData> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC);

	public Pair<AuraType, Integer> getRiddle(int index) {
		return index < riddles.size() ? riddles.get(index) : null;
	}

	public String getRiddleTranslationKey(int index) {
		Pair<AuraType, Integer> riddle = getRiddle(index);
		return riddle != null ? String.format("devotion_riddle.%s_%s", riddle.getFirst().getSerializedName(), riddle.getSecond()) : "";
	}
}
