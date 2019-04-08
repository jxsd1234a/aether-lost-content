package com.legacy.lostaether;

import com.legacy.aether.blocks.BlockAetherSapling;
import com.legacy.aether.blocks.decorative.BlockAetherStairs;
import com.legacy.aether.blocks.decorative.BlockAetherWall;
import com.legacy.aether.registry.creative_tabs.AetherCreativeTabs;
import com.legacy.lostaether.blocks.BlockLostDungeonBase;
import com.legacy.lostaether.world.AetherGenCrystalTree;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class BlocksLostAether
{

	public static Block crystal_sapling;

	public static Block songstone;
	
	public static Block gale_stone, light_gale_stone, locked_gale_stone, locked_light_gale_stone;

	public static Block gale_stairs, light_gale_stairs, gale_wall;
	
	public static Block gale_slab;
	
	public static Block gale_double_slab;
	
	private static IForgeRegistry<Block> iBlockRegistry;

	private static IForgeRegistry<Item> iItemRegistry;

	public static void initialization()
	{
		if (iBlockRegistry == null || iItemRegistry == null)
		{
			return;
		}

		crystal_sapling = register("crystal_sapling", new BlockAetherSapling(new AetherGenCrystalTree()));
		
		songstone = register("songstone", new Block(Material.IRON).setBlockUnbreakable());
		
		gale_stone = register("gale_stone", new BlockLostDungeonBase(false));
		light_gale_stone = register("light_gale_stone", new BlockLostDungeonBase(false));
		locked_gale_stone = register("locked_gale_stone", new BlockLostDungeonBase(true)).setCreativeTab(null);
		locked_light_gale_stone = register("locked_light_gale_stone", new BlockLostDungeonBase(true)).setCreativeTab(null);
		
		gale_stairs = register("gale_stairs", new BlockAetherStairs(gale_stone.getDefaultState()));
		
		gale_wall = register("gale_wall", new BlockAetherWall(gale_stone.getDefaultState()));
	}
	
	public static void setItemRegistry(IForgeRegistry<Item> iItemRegistry)
	{
		BlocksLostAether.iItemRegistry = iItemRegistry;
	}

	public static void setBlockRegistry(IForgeRegistry<Block> iBlockRegistry)
	{
		BlocksLostAether.iBlockRegistry = iBlockRegistry;
	}
	
	public static Block register(String name, Block block)
	{
		return register(name, block, new ItemBlock(block));
	}

	public static Block register(String name, Block block, ItemBlock item)
	{
		block.setTranslationKey(name);

		block.setRegistryName(LostAetherContent.locate(name));
		item.setRegistryName(LostAetherContent.locate(name));

		iBlockRegistry.register(block);
		iItemRegistry.register(item);

		block.setCreativeTab(AetherCreativeTabs.blocks);

		return block;
	}
}