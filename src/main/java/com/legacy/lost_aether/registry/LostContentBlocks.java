package com.legacy.lost_aether.registry;

import java.util.ArrayList;

import com.aether.item.AetherItemGroups;
import com.google.common.collect.Lists;
import com.legacy.lost_aether.LostContentRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;

public class LostContentBlocks
{
	public static Block songstone, gale_stone, light_gale_stone, gale_stone_slab, gale_stone_stairs, gale_stone_wall;

	public static Block locked_gale_stone, locked_light_gale_stone;

	public static Block crystal_sapling, potted_crystal_sapling, holiday_sapling, potted_holiday_sapling;

	private static IForgeRegistry<Block> iBlockRegistry;
	public static ArrayList<Block> blockList = Lists.newArrayList();

	public static void init(Register<Block> event)
	{
		LostContentBlocks.iBlockRegistry = event.getRegistry();

		if (iBlockRegistry == null)
			return;

		songstone = register("songstone", new Block(Block.Properties.from(Blocks.BEDROCK)));
		gale_stone = register("gale_stone", new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5f).sound(SoundType.STONE)));
		light_gale_stone = register("light_gale_stone", new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5f).sound(SoundType.STONE).lightValue(11)));

		locked_gale_stone = register("locked_gale_stone", new Block(Block.Properties.from(Blocks.BEDROCK)));
		locked_light_gale_stone = register("locked_light_gale_stone", new Block(Block.Properties.from(Blocks.BEDROCK).lightValue(11)));

		gale_stone_slab = register("gale_stone_slab", new SlabBlock(Block.Properties.from(gale_stone)));
		gale_stone_stairs = register("gale_stone_stairs", new StairsBlock(() -> gale_stone.getDefaultState(), Block.Properties.from(gale_stone)));
		gale_stone_wall = register("gale_stone_wall", new WallBlock(Block.Properties.from(gale_stone)));

		/*crystal_sapling = register("crystal_sapling", new SaplingBlock(new OakTree(), Block.Properties.from(Blocks.OAK_SAPLING)) {});
		holiday_sapling = register("holiday_sapling", new SaplingBlock(new OakTree(), Block.Properties.from(Blocks.OAK_SAPLING)) {});*/
		/*potted_crystal_sapling = registerBlock("potted_crystal_sapling", new AetherFlowerPotBlock(() -> crystal_sapling.delegate.get()));*/
		/*potted_holiday_sapling = registerBlock("potted_holiday_sapling", new AetherFlowerPotBlock(() -> holiday_sapling.delegate.get()));*/
	}

	public static Block register(String name, Block block)
	{
		return register(name, block, AetherItemGroups.AETHER_BLOCKS);
	}

	public static <T extends ItemGroup> Block register(String key, Block block, T itemGroup)
	{
		blockList.add(block);
		return registerBlock(key, block);
	}

	public static Block registerBlock(String key, Block block)
	{
		LostContentRegistry.register(iBlockRegistry, key, block);
		return block;
	}
}