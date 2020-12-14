package com.legacy.lost_aether.item;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ParentingBlockItem extends BlockItem
{
	private final Supplier<Item> parentItem;

	public ParentingBlockItem(Block blockIn, Properties builder, Supplier<Item> entryPoint)
	{
		super(blockIn, builder);

		this.parentItem = entryPoint;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if (this.parentItem == null || this.parentItem != null && this.parentItem.get() == null)
		{
			super.fillItemGroup(group, items);
			return;
		}

		List<Item> groupItems = items.stream().map(ItemStack::getItem).collect(Collectors.toList());

		if (this.isInGroup(group) && groupItems.contains(this.parentItem.get()))
			items.add(groupItems.indexOf(this.parentItem.get()) + 1, new ItemStack(this));
	}
}
