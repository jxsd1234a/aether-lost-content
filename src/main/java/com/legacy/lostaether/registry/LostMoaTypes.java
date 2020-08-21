package com.legacy.lostaether.registry;

import com.gildedgames.the_aether.api.moa.AetherMoaType;
import com.gildedgames.the_aether.api.moa.MoaProperties;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import com.legacy.lostaether.LostAetherContent;

import net.minecraftforge.registries.IForgeRegistry;

public class LostMoaTypes
{
	public static IForgeRegistry<AetherMoaType> moaRegistry;

	public static AetherMoaType brown;

	public static void initialization()
	{
		brown = register("brown", 0x5d5726, new MoaProperties(4, 0.3F));
	}

	public static AetherMoaType register(String name, int hexColor, MoaProperties properties)
	{
		AetherMoaType moaType = new AetherMoaType(hexColor, properties, AetherCreativeTabs.misc);

		moaRegistry.register(moaType.setRegistryName(LostAetherContent.locate(name)));

		return moaType;
	}

}