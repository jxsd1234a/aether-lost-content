package com.legacy.lostaether.items.tools;

import com.legacy.aether.items.ItemsAether;
import com.legacy.aether.registry.creative_tabs.AetherCreativeTabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;

public class ItemPhoenixSword extends ItemSword
{

    public ItemPhoenixSword()
    {
        super(ToolMaterial.DIAMOND);
    }

	@Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
    	if (tab == AetherCreativeTabs.weapons || tab == CreativeTabs.SEARCH)
    	{
            items.add(new ItemStack(this));
    	}
    }

    @Override
    public boolean getIsRepairable(ItemStack repairingItem, ItemStack material)
    {
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
    	return ItemsAether.aether_loot;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase hitentity, EntityLivingBase player)
    {
    	hitentity.setFire(5);

        return super.hitEntity(itemstack, hitentity, player);
    }

}