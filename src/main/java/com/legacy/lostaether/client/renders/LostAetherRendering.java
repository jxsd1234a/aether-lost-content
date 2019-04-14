package com.legacy.lostaether.client.renders;

import com.legacy.lostaether.BlocksLostAether;
import com.legacy.lostaether.LostAetherContent;
import com.legacy.lostaether.items.ItemsLostAether;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LostAetherRendering
{

	@SubscribeEvent
	public void initItems(ModelRegistryEvent event)
	{
		register(ItemsLostAether.sentry_shield, "sentry_shield");
		register(ItemsLostAether.phoenix_cape, "phoenix_cape");
		register(ItemsLostAether.invisibility_gem, "invisibility_gem");

		register(ItemsLostAether.agility_boots, "agility_boots");
		register(ItemsLostAether.swetty_mask, "swetty_mask");
		register(ItemsLostAether.power_gloves, "power_gloves");

		register(ItemsLostAether.phoenix_pickaxe, "phoenix_pickaxe");
		register(ItemsLostAether.phoenix_axe, "phoenix_axe");
		register(ItemsLostAether.phoenix_shovel, "phoenix_shovel");
		register(ItemsLostAether.phoenix_sword, "phoenix_sword");

		register(ItemsLostAether.zanite_shield, "zanite_shield");
		register(ItemsLostAether.gravitite_shield, "gravitite_shield");
		register(ItemsLostAether.jeb_shield, "jeb_shield");

		register(ItemsLostAether.platinum_key, "platinum_key");
	}
	
	@SubscribeEvent
	public void onModelRegisterEvent(ModelRegistryEvent event)
	{
		register(BlocksLostAether.gale_stone, "gale_stone");
		register(BlocksLostAether.light_gale_stone, "light_gale_stone");
		
		register(BlocksLostAether.locked_gale_stone, "gale_stone");
		register(BlocksLostAether.locked_light_gale_stone, "light_gale_stone");
		
		register(BlocksLostAether.gale_slab, "gale_slab");
		register(BlocksLostAether.gale_double_slab, "gale_double_slab");
		
		register(BlocksLostAether.gale_wall, "gale_wall");
		register(BlocksLostAether.gale_stairs, "gale_stairs");
		register(BlocksLostAether.crystal_sapling, "crystal_sapling");
		
		register(BlocksLostAether.songstone, "songstone");

	}

	public static void register(Item item, String model)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(LostAetherContent.modAddress() + model, "inventory"));
	}
	
	public static void register(Block block, String model)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(LostAetherContent.MODID + ":" + model, "inventory"));
	}
}