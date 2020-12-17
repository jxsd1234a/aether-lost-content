package com.legacy.lost_aether.block;

import java.util.List;

import com.aether.api.AetherAPI;
import com.legacy.lost_aether.entity.AerwhaleKingEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class SongstoneBlock extends Block
{
	public SongstoneBlock(Block.Properties props)
	{
		super(props);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hit)
	{
		if (worldIn.isRemote)
		{
			return ActionResultType.SUCCESS;
		}
		else
		{
			worldIn.playSound(null, pos, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.BLOCKS, 0.7F, 2.0F);
			AxisAlignedBB radiusCheck = playerIn.getBoundingBox().grow(20.0D, 15.0D, 20.0D);
			List<PlayerEntity> list = worldIn.<PlayerEntity>getEntitiesWithinAABB(PlayerEntity.class, radiusCheck);
			List<AerwhaleKingEntity> aerwhaleList = worldIn.<AerwhaleKingEntity>getEntitiesWithinAABB(AerwhaleKingEntity.class, radiusCheck);

			aerwhaleList.forEach((aerwhaleKing) ->
			{
				if (!list.isEmpty())
				{
					if (list.size() > 1)
						aerwhaleKing.setAttackTarget(list.get(list.size()));
					else
						aerwhaleKing.setAttackTarget(playerIn);
				}

				list.forEach((players) ->
				{
					AetherAPI.get(players).ifPresent((aetherPlayer) -> aetherPlayer.setFocusedBoss(aerwhaleKing));
					AerwhaleKingEntity.setDoor(aerwhaleKing);
				});

				return;
			});

			worldIn.destroyBlock(pos, false);
			return ActionResultType.SUCCESS;
		}
	}
}
