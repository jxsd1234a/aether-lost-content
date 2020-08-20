package com.legacy.lostaether.client;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.AetherConfig;
import com.gildedgames.the_aether.client.gui.menu.AetherMainMenu;
import com.legacy.lostaether.LostContentConfig;
import com.legacy.lostaether.client.gui.GuiLostAetherMenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LostClientEvents
{
	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent event)
	{
		if (event.getGui() == null)
			return;

		if (LostContentConfig.visual.aether_menu)
		{
			if (event.getGui() instanceof AetherMainMenu)
				event.setGui(new GuiLostAetherMenu());

			if (AetherConfig.visual_options.menu_enabled && event.getGui().getClass() == AetherMainMenu.class)
				event.setGui(new GuiLostAetherMenu());
		}
		
		ResourceLocation backgroundLocation = LostContentConfig.visual.aether_menu ? Aether.locate("textures/blocks/aether_dirt.png") : new ResourceLocation("textures/gui/options_background.png");

		if (Gui.OPTIONS_BACKGROUND.getPath() != backgroundLocation.getPath())
			Gui.OPTIONS_BACKGROUND = backgroundLocation;
	}

	@SubscribeEvent
	public void onDrawGui(GuiScreenEvent.DrawScreenEvent.Pre event)
	{
		if ((!AetherConfig.visual_options.menu_enabled || !LostContentConfig.visual.aether_menu) && event.getGui().getClass() == GuiLostAetherMenu.class)
			Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
	}
}