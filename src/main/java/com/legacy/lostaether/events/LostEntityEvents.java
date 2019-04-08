package com.legacy.lostaether.events;


import com.legacy.aether.api.AetherAPI;
import com.legacy.aether.api.player.IPlayerAether;
import com.legacy.aether.entities.passive.mountable.EntityMoa;
import com.legacy.lostaether.LostMoaTypes;
import com.legacy.lostaether.entities.EntityKingAerwhale;
import com.legacy.lostaether.items.tools.ItemAetherShield;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LostEntityEvents
{
	@SubscribeEvent
	public void onEntityUpdate(net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent event)
	{
		if (event.getEntity() instanceof EntityLiving)
		{
			
		}
	}
	
	@SubscribeEvent
	public void onEntityJump(net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent event)
	{
		if (event.getEntity() instanceof EntityMoa)
		{
			EntityMoa moa = (EntityMoa)event.getEntity();

			if (moa.getMoaType() == LostMoaTypes.brown && moa.isBeingRidden())
			{
				moa.motionY = 1.1F;
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event)
	{
		if (event.getEntityLiving() instanceof EntityPlayer)
		{
			//IPlayerAether playerAether = AetherAPI.getInstance().get((EntityPlayer) event.getEntityLiving());
			EntityPlayer entity = (EntityPlayer)event.getEntityLiving();
			float damage = event.getAmount();
			
			if (entity.getActiveItemStack().getItem() instanceof ItemAetherShield && entity.getCooldownTracker().hasCooldown(entity.getActiveItemStack().getItem()))
			{
				entity.resetActiveHand();
			}

			if (damage >= 3.0F && entity.getActiveItemStack().getItem() instanceof ItemAetherShield)
	        {
	            ItemStack copyBeforeUse = entity.getActiveItemStack().copy();
	            int i = 1 + MathHelper.floor(damage);
	            entity.getActiveItemStack().damageItem(i, entity);

	            if (entity.getActiveItemStack().isEmpty())
	            {
	                EnumHand enumhand = entity.getActiveHand();
	                
	                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(entity, copyBeforeUse, enumhand);

	                if (enumhand == EnumHand.MAIN_HAND)
	                {
	                    entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
	                }
	                else
	                {
	                	entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
	                }

	                entity.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + entity.world.rand.nextFloat() * 0.4F);
	            }
	            
	            if (event.getSource().getImmediateSource() instanceof EntityKingAerwhale)
	            {
	            	entity.getCooldownTracker().setCooldown(entity.getActiveItemStack().getItem(), 200);
	            	entity.world.setEntityState(entity, (byte)30);
	            	//
	            }
	        }
		}
		
		/*if (event.getSource().getImmediateSource() instanceof EntityPlayer)
		{
			EntityPlayer player = ((EntityPlayer)event.getSource().getImmediateSource());
			
			IPlayerAether playerAether = AetherAPI.getInstance().get(player);
			 
			
		}*/
	}
}
