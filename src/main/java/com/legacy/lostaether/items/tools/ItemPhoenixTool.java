package com.legacy.lostaether.items.tools;

import com.legacy.aether.items.ItemsAether;
import com.legacy.aether.items.tools.ItemAetherTool;
import com.legacy.aether.items.util.EnumAetherToolType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPhoenixTool extends ItemAetherTool
{

	public ItemPhoenixTool(EnumAetherToolType toolType) 
	{
		super(ToolMaterial.DIAMOND, toolType);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(new ItemStack(state.getBlock()));
		if (!world.isRemote && itemstack != ItemStack.EMPTY)
		{
			if (state.getBlockHardness(world, pos) > 0.0f)
			{
				EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F);
				
				if (itemstack.getCount() > 1)
				{
					itemstack.setCount(1);
				}
				
				if (entityItem.getItem() != itemstack)
				{
					entityItem.setItem(itemstack);
				}
				
				world.spawnEntity(entityItem);
				world.setBlockToAir(pos);
			}
		}
		return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
	}
	
	@Override
    public EnumRarity getRarity(ItemStack stack)
    {
    	return ItemsAether.aether_loot;
    }

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

}