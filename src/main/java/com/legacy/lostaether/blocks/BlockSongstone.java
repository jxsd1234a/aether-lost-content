package com.legacy.lostaether.blocks;

import java.util.List;

import com.gildedgames.the_aether.api.AetherAPI;
import com.legacy.lostaether.entities.EntityAerwhaleKing;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSongstone extends Block
{
	public BlockSongstone(Material materialIn)
	{
		super(materialIn);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			worldIn.playSound(null, pos, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.BLOCKS, 1.0F, 2.0F);
			AxisAlignedBB radiusCheck = playerIn.getEntityBoundingBox().grow(20.0D, 15.0D, 20.0D);
			List<EntityPlayer> list = worldIn.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, radiusCheck);
			List<EntityAerwhaleKing> aerwhaleList = worldIn.<EntityAerwhaleKing>getEntitiesWithinAABB(EntityAerwhaleKing.class, radiusCheck);
			for (EntityAerwhaleKing nearbyAerwhaleKings : aerwhaleList)
			{
				for (EntityPlayer nearbyPlayers : list)
				{
					AetherAPI.getInstance().get(nearbyPlayers).setFocusedBoss(nearbyAerwhaleKings);
					nearbyAerwhaleKings.setAttackTarget(nearbyPlayers);
					EntityAerwhaleKing.setDoor(nearbyAerwhaleKings);
				}
			}

			worldIn.destroyBlock(pos, false);
			return true;
		}
	}
}
