package dev.cammiescorner.devotion.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.cammiescorner.devotion.api.spells.AuraType;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Arrays;

public class GetAuraCommand {
	public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
		root.then(Commands.literal("get_aura")
			.requires(src -> src.hasPermission(Commands.LEVEL_ADMINS))
			.then(Commands.argument("auratype", StringArgumentType.word())
				.suggests((ctx, builder) -> SharedSuggestionProvider.suggest(
					Arrays.stream(AuraType.values()).map(AuraType::getSerializedName), builder)
				)
				.executes(ctx -> getAura(ctx, ctx.getSource().getEntityOrException()))
			)
			.then(Commands.argument("entity", EntityArgument.entity())
				.then(Commands.argument("auratype", StringArgumentType.word())
					.suggests((ctx, builder) -> SharedSuggestionProvider.suggest(
						Arrays.stream(AuraType.values()).map(AuraType::getSerializedName), builder)
					)
					.executes(ctx -> getAura(ctx, ctx.getSource().getEntityOrException()))
				)
			)
		);
	}

	private static int getAura(CommandContext<CommandSourceStack> context, Entity entity) {
		if(!(entity instanceof LivingEntity living)) {
			context.getSource().sendFailure(Component.translatable(DevotionCommands.translationKey("not_living_entity")));
			return 0;
		}

		AuraType auraType = AuraType.byName(context.getArgument("auratype", String.class));
		context.getSource().sendSuccess(() -> Component.translatable(DevotionCommands.translationKey("set_aura"), living.getName(), auraType.getSerializedName(), MainHelper.getAura(living, auraType)), true);

		return Command.SINGLE_SUCCESS;
	}
}
