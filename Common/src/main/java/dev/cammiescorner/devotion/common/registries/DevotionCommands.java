package dev.cammiescorner.devotion.common.registries;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.cammiescorner.devotion.Devotion;
import dev.cammiescorner.devotion.common.commands.GetAuraCommand;
import dev.cammiescorner.devotion.common.commands.GetAuraTypeCommand;
import dev.cammiescorner.devotion.common.commands.SetAuraCommand;
import dev.cammiescorner.devotion.common.commands.SetAuraTypeCommand;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class DevotionCommands {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection environment) {
		LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(Devotion.MOD_ID);

		GetAuraCommand.register(root);
		SetAuraCommand.register(root);
		GetAuraTypeCommand.register(root);
		SetAuraTypeCommand.register(root);

		dispatcher.register(root);
	}

	public static String translationKey(String name) {
		return "command." + Devotion.MOD_ID + "." + name;
	}
}
