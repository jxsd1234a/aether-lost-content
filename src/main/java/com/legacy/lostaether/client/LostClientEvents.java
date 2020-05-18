package com.legacy.lostaether.client;

import com.legacy.lostaether.LostContentConfig;
import com.legacy.lostaether.client.gui.GuiAetherMenu;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LostClientEvents
{
	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent event)
	{
		if (LostContentConfig.visual.aether_menu && event.getGui() instanceof GuiMainMenu)
		{
			event.setGui(new GuiAetherMenu());
		}
	}
}