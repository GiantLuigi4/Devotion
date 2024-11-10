package dev.cammiescorner.devotion.api.research;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.cammiescorner.devotion.api.spells.AuraType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public record RiddleData(List<Pair<AuraType, Integer>> riddles) {
	public static final Codec<RiddleData> CODEC = RecordCodecBuilder.create(riddleDataInstance -> riddleDataInstance.group(
		Codec.compoundList(AuraType.CODEC, Codec.INT).fieldOf("riddles").forGetter(RiddleData::riddles)
	).apply(riddleDataInstance, RiddleData::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, RiddleData> STREAM_CODEC = StreamCodec.of((buffer, value) -> {
		List<Pair<AuraType, Integer>> riddles = value.riddles();
		int riddleCount = riddles.size();

		buffer.writeVarInt(riddleCount);

		for(int i = 0; i < riddleCount; i++) {
			Pair<AuraType, Integer> pair = riddles.get(i);
			buffer.writeEnum(pair.getFirst());
			buffer.writeVarInt(pair.getSecond());
		}
	}, buffer -> {
		List<Pair<AuraType, Integer>> riddles = new ArrayList<>();
		int riddleCount = buffer.readVarInt();

		for(int i = 0; i < riddleCount; i++)
			riddles.add(Pair.of(buffer.readEnum(AuraType.class), buffer.readVarInt()));

		return new RiddleData(riddles);
	});

	public Pair<AuraType, Integer> getRiddle(int index) {
		return index < riddles.size() ? riddles.get(index) : null;
	}

	public String getRiddleTranslationKey(int index) {
		Pair<AuraType, Integer> riddle = getRiddle(index);
		return riddle != null ? String.format("devotion_riddle.%s_%s", riddle.getFirst().getSerializedName(), riddle.getSecond()) : "";
	}
}
