package com.legacy.lostaether.blocks;

import com.legacy.aether.registry.creative_tabs.AetherCreativeTabs;
import com.legacy.lostaether.BlocksLostAether;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLostDungeonBase extends Block
{


	public BlockLostDungeonBase(boolean isLocked) 
	{
		super(Material.ROCK);

		if (isLocked)
		{
			this.setResistance(6000000.0F);
			this.setBlockUnbreakable();
		}

		this.setSoundType(SoundType.STONE);
		this.setHardness(isLocked ? -1F : 0.5F);
		this.setCreativeTab(isLocked ? null : AetherCreativeTabs.blocks);
	}

	@Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state.getBlock() != this)
        {
            return state.getLightValue(world, pos);
        }

        if (this == BlocksLostAether.light_gale_stone || this == BlocksLostAether.locked_light_gale_stone)
        {
        	return (int)(15.0F * 0.75f);
        }

        return 0;
    }

	@Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
		return super.getPickBlock(state, target, world, pos, player);
    }
}