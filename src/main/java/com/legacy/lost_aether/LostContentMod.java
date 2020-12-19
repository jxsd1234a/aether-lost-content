package com.legacy.lost_aether;

import java.util.logging.Logger;

import com.legacy.lost_aether.client.LostContentEntityRendering;
import com.legacy.lost_aether.client.LostContentItemModelPredicates;
import com.legacy.lost_aether.data.LostContentTags;
import com.legacy.lost_aether.event.LostContentEvents;
import com.legacy.lost_aether.registry.LostContentFeatures;
import com.legacy.structure_gel.events.RegisterDimensionEvent;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LostContentMod.MODID)
public class LostContentMod
{
	public static final String NAME = "Aether: Lost Content";
	public static final String MODID = "lost_aether_content";
	public static Logger LOGGER = Logger.getLogger(MODID);

	public LostContentMod()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LostContentConfig.COMMON_SPEC);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(LostContentMod::commonInit);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(LostContentMod::clientInit));

		MinecraftForge.EVENT_BUS.addListener((RegisterDimensionEvent event) -> LostContentFeatures.onDimensionRegistry(event));
		MinecraftForge.EVENT_BUS.register(LostContentEvents.class);
	}

	public static void clientInit(FMLClientSetupEvent event)
	{
		LostContentEntityRendering.init();
		LostContentItemModelPredicates.init();

		// MinecraftForge.EVENT_BUS.register(new LostContentClientEvents());
	}

	public static void commonInit(FMLCommonSetupEvent event)
	{
		LostContentTags.Blocks.init();
		LostContentTags.Items.init();
	}

	public static ResourceLocation locate(String name)
	{
		return new ResourceLocation(MODID, name);
	}

	public static String find(String name)
	{
		return MODID + ":" + name;
	}
}
