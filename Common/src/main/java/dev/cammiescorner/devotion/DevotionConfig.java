package dev.cammiescorner.devotion;

import com.teamresourceful.resourcefulconfig.api.annotations.*;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;

@ConfigInfo(
	title = "Devotion",
	links = {
		@ConfigInfo.Link(
			value = "https://modrinth.com/mod/devotion",
			icon = "modrinth",
			text = "Modrinth"
		),
		@ConfigInfo.Link(
			value = "https://www.curseforge.com/minecraft/mc-mods/devotion",
			icon = "curseforge",
			text = "Curseforge"
		),
		@ConfigInfo.Link(
			value = "https://github.com/CammiePone/Devotion",
			icon = "github",
			text = "Github"
		),
		@ConfigInfo.Link(
			value = "https://cammiescorner.dev/discord",
			icon = "gamepad-2",
			text = "Discord"
		)
	}
)
@Config(
	value = Devotion.MOD_ID,
	categories = {
		DevotionConfig.Client.class
	}
)
public class DevotionConfig {
	@Category(value = "Client")
	public static class Client {
		@ConfigEntry(
			type = EntryType.FLOAT,
			id = "auraSharpness",
			translation = ""
		)
		@ConfigOption.Range(min = 0, max = 20)
		public static float auraSharpness = 6f;

		@ConfigEntry(
			type = EntryType.FLOAT,
			id = "auraGradiant",
			translation = ""
		)
		@ConfigOption.Range(min = 0, max = 20)
		public static float auraGradiant = 3f;
	}
}
