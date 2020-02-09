package com.legacy.lostaether.events;

import java.util.Random;

import com.legacy.aether.blocks.BlocksAether;
import com.legacy.aether.blocks.natural.BlockCrystalLeaves;
import com.legacy.aether.items.tools.ItemSkyrootTool;
import com.legacy.lostaether.BlocksLostAether;

import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LostBlockEvents
{
	@SubscribeEvent
	public void onBlockHarvested(HarvestDropsEvent event) {
//		if (!event.getWorld().isRemote)
//			return;
		
		if (!(event.getState().getBlock() == BlocksAether.crystal_leaves))
			return;
		
		BlockCrystalLeaves block = (BlockCrystalLeaves) event.getState().getBlock();
		
		Random rand = event.getWorld().rand;
        int chance = 20;

        if (event.getFortuneLevel() > 0)
        {
            chance -= 2 << event.getFortuneLevel();
            if (chance < 10) chance = 10;
        }

        if (rand.nextInt(chance) == 0)
        {
        	int amountDropped = 1;
//        	
//        	if (event.getState().getValue(BlockLeaves.DECAYABLE) && event.getHarvester().getActiveItemStack().getItem() instanceof ItemSkyrootTool) {
//        		amountDropped = 2;
//        	}
        	
        	ItemStack drop = new ItemStack(BlocksLostAether.crystal_sapling, amountDropped);
            if (!drop.isEmpty())
                event.getDrops().add(drop);
        }
	}
}
