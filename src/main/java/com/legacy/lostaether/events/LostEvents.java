package com.legacy.lostaether.events;

import java.util.Random;

import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.entities.passive.mountable.EntityMoa;
import com.legacy.lostaether.blocks.BlocksLostAether;
import com.legacy.lostaether.registry.LostMoaTypes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LostEvents
{
	@SubscribeEvent
	public void onEntityJump(LivingEvent.LivingJumpEvent event)
	{
		if (event.getEntity() instanceof EntityMoa)
		{
			EntityMoa moa = (EntityMoa) event.getEntity();

			if (moa.getMoaType() == LostMoaTypes.brown && moa.isBeingRidden())
			{
				moa.motionY = 1.1F;
			}
		}
	}

	@SubscribeEvent
	public void onBlockHarvested(HarvestDropsEvent event)
	{
		if (event.getWorld().isRemote)
			return;

		if (!(event.getState().getBlock() == BlocksAether.crystal_leaves))
			return;

		Random rand = event.getWorld().rand;
		int chance = 20;

		if (event.getFortuneLevel() > 0)
		{
			chance -= 2 << event.getFortuneLevel();
			if (chance < 10)
				chance = 10;
		}

		if (rand.nextInt(chance) == 0)
		{
			int amountDropped = 1;
			ItemStack drop = new ItemStack(BlocksLostAether.crystal_sapling, amountDropped);
			if (!drop.isEmpty())
				event.getDrops().add(drop);
		}
	}
}
