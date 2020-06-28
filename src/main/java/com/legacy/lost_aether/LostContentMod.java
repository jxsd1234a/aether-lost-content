package com.legacy.lost_aether;

import java.util.logging.Logger;

import com.aether.biome.AetherBiomes;
import com.legacy.lost_aether.registry.LostContentFeatures;
import com.legacy.lost_aether.world.structure.PlatinumDungeonConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
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
		/*ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, LostContentConfig.CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LostContentConfig.SERVER_SPEC);
		MinecraftForge.EVENT_BUS.register(new LostContentEvents());*/

		FMLJavaModLoadingContext.get().getModEventBus().addListener(LostContentMod::commonInit);

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
		{
			FMLJavaModLoadingContext.get().getModEventBus().addListener(LostContentMod::clientInit);
		});
	}

	public static void clientInit(FMLClientSetupEvent event)
	{
		// LostContentEntityRendering.init();
		// MinecraftForge.EVENT_BUS.register(new LostContentClientEvents());
	}

	public static void commonInit(FMLCommonSetupEvent event)
	{
		// LostContentRankings.init();
		AetherBiomes.BIOMES.getEntries().forEach((biome) ->
		{
			System.out.println("burger " + biome.get().getDisplayName());
			biome.get().addStructure(LostContentFeatures.PLATINUM_DUNGEON, new PlatinumDungeonConfig());
			biome.get().addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(LostContentFeatures.PLATINUM_DUNGEON, new PlatinumDungeonConfig(), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
		});

		/*biome.addStructure(LostContentFeatures.PLATINUM_DUNGEON, new PlatinumDungeonConfig());
		biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(LostContentFeatures.PLATINUM_DUNGEON, new PlatinumDungeonConfig(), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));*/

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
