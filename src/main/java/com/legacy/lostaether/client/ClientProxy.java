package com.legacy.lostaether.client;

import com.legacy.lostaether.CommonProxy;
import com.legacy.lostaether.client.audio.LostMusicHandler;
import com.legacy.lostaether.client.renders.LostAetherRendering;
import com.legacy.lostaether.client.renders.LostEntityRenders;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy extends CommonProxy
{

	@Override
	public void preInitialization()
	{
		// registerEvent(new LostAetherBlockRendering());
		registerEvent(new LostAetherRendering());
		registerEvent(new LostClientEvents());
		registerEvent(new LostMusicHandler());
		LostEntityRenders.initialize();
	}

	@Override
	public void initialization()
	{
		// AetherEntityRenderingRegistry.initializePlayerLayers();
	}

	@Override
	public void postInitialization()
	{
	}

	@Override
	public EntityPlayer getThePlayer()
	{
		return Minecraft.getMinecraft().player;
	}
}