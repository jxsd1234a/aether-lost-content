package com.legacy.lost_aether.registry;

import java.util.Map.Entry;
import java.util.function.Supplier;

import com.legacy.lost_aether.LostContentRegistry;
import com.legacy.lost_aether.item.ParentingBlockItem;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;

public class LostContentItems
{
	public static void registerBlockItems(IForgeRegistry<Item> registry)
	{
		for (Entry<Block, Pair<ItemGroup, Supplier<Item>>> entry : LostContentBlocks.blockItemMap.entrySet())
		{
			LostContentRegistry.register(registry, entry.getKey().getRegistryName().getPath(), new ParentingBlockItem(entry.getKey(), new Item.Properties().group(entry.getValue().getFirst()), entry.getValue().getSecond()));
		}
		LostContentBlocks.blockItemMap.clear();

		for (Entry<Block, Item.Properties> entry : LostContentBlocks.blockItemPropertiesMap.entrySet())
		{
			LostContentRegistry.register(registry, entry.getKey().getRegistryName().getPath(), new BlockItem(entry.getKey(), entry.getValue()));
		}
		LostContentBlocks.blockItemPropertiesMap.clear();
	}
}
