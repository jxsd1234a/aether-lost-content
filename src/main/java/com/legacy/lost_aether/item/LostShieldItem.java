package com.legacy.lost_aether.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;

public class LostShieldItem extends ShieldItem
{
	public LostShieldItem(Properties builder)
	{
		super(builder);
	}

	@Override
	public boolean isShield(ItemStack stack, LivingEntity entity)
	{
		return true;
	}
}
