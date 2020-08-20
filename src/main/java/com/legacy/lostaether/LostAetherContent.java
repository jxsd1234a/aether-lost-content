package com.legacy.lostaether;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.legacy.lostaether.client.ClientProxy;
import com.legacy.lostaether.client.renders.LostAetherRendering;
import com.legacy.lostaether.events.LostAetherRegistryEvent;
import com.legacy.lostaether.events.LostEvents;
import com.legacy.lostaether.events.PlayerLostAetherEvents;
import com.legacy.lostaether.registry.LostAetherEntities;
import com.legacy.lostaether.world.AetherStructureGenerator;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = LostAetherContent.MODID, name = LostAetherContent.NAME, version = LostAetherContent.VERSION, dependencies = "required-after:aether_legacy@[1.5.0,);", updateJSON = "https://moddinglegacy.com/supporters-changelogs/lost-content.json")
public class LostAetherContent
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final LostSplashes.Splashes SPLASHES = new LostSplashes.Splashes();

	public static final String MODID = "lost_aether";
	public static final String NAME = "Lost Aether Content";
	public static final String VERSION = "1.0.2";

	@SidedProxy(modId = LostAetherContent.MODID, clientSide = "com.legacy.lostaether.client.ClientProxy", serverSide = "com.legacy.lostaether.CommonProxy")
	public static CommonProxy proxy;

	@Instance(LostAetherContent.MODID)
	public static LostAetherContent instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		CommonProxy.registerEvent(new LostEvents());
		CommonProxy.registerEvent(new LostAetherRegistryEvent());
		ClientProxy.registerEvent(new LostAetherRendering());
		proxy.preInitialization();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		CommonProxy.registerEvent(new PlayerLostAetherEvents());

		LostAetherEntities.initialization();

		GameRegistry.registerWorldGenerator(new AetherStructureGenerator(), 0);
		proxy.initialization();
	}

	public static ResourceLocation locate(String location)
	{
		return new ResourceLocation(MODID, location);
	}

	public static String find()
	{
		return MODID + ":";
	}
}