package com.legacy.lost_aether.registry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.aether.item.AetherItemGroups;
import com.legacy.lost_aether.LostContentRegistry;
import com.legacy.lost_aether.block.SongstoneBlock;
import com.legacy.lost_aether.block.util.CrystalTree;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;

public class LostContentBlocks
{
	public static Block songstone, gale_stone, light_gale_stone, gale_stone_slab, gale_stone_stairs, gale_stone_wall;

	public static Block locked_gale_stone, locked_light_gale_stone;

	public static Block crystal_sapling, potted_crystal_sapling, holiday_sapling, potted_holiday_sapling;

	private static IForgeRegistry<Block> iBlockRegistry;

	public static Map<Block, Pair<ItemGroup, Supplier<Item>>> blockItemMap = new LinkedHashMap<>();
	public static Map<Block, Item.Properties> blockItemPropertiesMap = new LinkedHashMap<>();

	public static void init(Register<Block> event)
	{
		LostContentBlocks.iBlockRegistry = event.getRegistry();

		if (iBlockRegistry == null)
			return;

		songstone = register("songstone", new SongstoneBlock(Block.Properties.from(Blocks.BEDROCK)));
		gale_stone = register("gale_stone", new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5f).sound(SoundType.STONE)));
		light_gale_stone = register("light_gale_stone", new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(0.5f).sound(SoundType.STONE).setLightLevel((light) -> 11)));

		locked_gale_stone = register("locked_gale_stone", new Block(Block.Properties.from(Blocks.BEDROCK)));
		locked_light_gale_stone = register("locked_light_gale_stone", new Block(Block.Properties.from(Blocks.BEDROCK).setLightLevel((light) -> 11)));

		gale_stone_slab = register("gale_stone_slab", new SlabBlock(Block.Properties.from(gale_stone)));
		gale_stone_stairs = register("gale_stone_stairs", new StairsBlock(() -> gale_stone.getDefaultState(), Block.Properties.from(gale_stone)));
		gale_stone_wall = register("gale_stone_wall", new WallBlock(Block.Properties.from(gale_stone)));

		crystal_sapling = register("crystal_sapling", new SaplingBlock(new CrystalTree(), Block.Properties.from(Blocks.OAK_SAPLING)));
		/*holiday_sapling = register("holiday_sapling", new SaplingBlock(new OakTree(), Block.Properties.from(Blocks.OAK_SAPLING)) {});*/
		/*potted_crystal_sapling = registerBlock("potted_crystal_sapling", new AetherFlowerPotBlock(() -> crystal_sapling.delegate.get()));*/
		/*potted_holiday_sapling = registerBlock("potted_holiday_sapling", new AetherFlowerPotBlock(() -> holiday_sapling.delegate.get()));*/
	}

	public static Block register(String name, Block block)
	{
		register(name, block, AetherItemGroups.AETHER_BLOCKS, null);
		return block;
	}

	public static Block register(String name, Block block, Supplier<Item> entryPoint)
	{
		register(name, block, AetherItemGroups.AETHER_BLOCKS, entryPoint);
		return block;
	}

	public static <T extends ItemGroup> Block register(String key, Block block, T itemGroup)
	{
		return register(key, block, itemGroup, null);
	}

	public static <T extends ItemGroup> Block register(String key, Block block, T itemGroup, Supplier<Item> entryPoint)
	{
		blockItemMap.put(block, Pair.of(itemGroup, entryPoint));
		return registerBlock(key, block);
	}

	public static Block registerBlock(String name, Block block)
	{
		if (iBlockRegistry != null)
			LostContentRegistry.register(iBlockRegistry, name, block);

		return block;
	}
}