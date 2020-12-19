package com.legacy.lost_aether.data;

import com.legacy.lost_aether.LostContentMod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class LostContentTags
{
	public static class Blocks
	{
		public static void init()
		{
		}

		public static final ITag.INamedTag<Block> GALE_STONES = tag("gale_stones");

		private static ITag.INamedTag<Block> tag(String name)
		{
			return BlockTags.makeWrapperTag(LostContentMod.find(name));
		}
	}

	public static class Items
	{
		public static void init()
		{
		}

		public static final ITag.INamedTag<Item> GALE_STONES = tag("gale_stones");

		public static final ITag.INamedTag<Item> PHOENIX_TOOLS = tag("phoenix_tools");

		private static ITag.INamedTag<Item> tag(String name)
		{
			return ItemTags.makeWrapperTag(LostContentMod.find(name));
		}
	}
}
