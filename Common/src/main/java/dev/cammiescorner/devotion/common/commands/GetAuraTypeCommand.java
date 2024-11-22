package dev.cammiescorner.devotion.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.cammiescorner.devotion.common.MainHelper;
import dev.cammiescorner.devotion.common.registries.DevotionCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class GetAuraTypeCommand {
	public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
		root.then(Commands.literal("get_auratype")
			.requires(src -> src.hasPermission(Commands.LEVEL_ADMINS))
			.then(Commands.argument("entity", EntityArgument.entity())
				.executes(ctx -> getAuraType(ctx, EntityArgument.getEntity(ctx, "entity")))
			)
			.executes(ctx -> getAuraType(ctx, ctx.getSource().getEntityOrException()))
		);
	}

	private static int getAuraType(CommandContext<CommandSourceStack> context, Entity entity) {
		if(!(entity instanceof LivingEntity living)) {
			context.getSource().sendFailure(Component.translatable(DevotionCommands.translationKey("not_living_entity")));
			return 0;
		}

		context.getSource().sendSuccess(() -> Component.translatable(DevotionCommands.translationKey("get_auratype"), living.getName(), MainHelper.getPrimaryAuraType(living).getSerializedName()), true);

		return Command.SINGLE_SUCCESS;
	}
}
