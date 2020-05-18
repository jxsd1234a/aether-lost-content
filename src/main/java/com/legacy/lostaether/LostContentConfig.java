package com.legacy.lostaether;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = LostAetherContent.MODID)
@Config.LangKey(LostAetherContent.MODID + ".config.title")
public class LostContentConfig
{
	public static final VisualChanges visual = new VisualChanges();

	public static class VisualChanges
	{
		@Config.Comment("Enables the Aether Menu.")
		public boolean aether_menu = false;
	}

	@Mod.EventBusSubscriber(modid = LostAetherContent.MODID)
	private static class EventHandler
	{
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event)
		{
			if (event.getModID().equals(LostAetherContent.MODID))
			{
				ConfigManager.sync(LostAetherContent.MODID, Config.Type.INSTANCE);
			}
		}
	}
}