package dev.cammiescorner.devotion;

import com.teamresourceful.resourcefulconfig.api.annotations.Config;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;

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
@Config(value = Devotion.MOD_ID)
public class DevotionConfig {

}
