package com.legacy.lostaether.items.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.legacy.aether.Aether;
import com.legacy.aether.items.ItemsAether;
import com.legacy.aether.registry.creative_tabs.AetherCreativeTabs;
import com.legacy.lostaether.client.models.ModelAgilityBoots;
import com.legacy.lostaether.items.ItemsLostAether;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLostArmor extends ItemArmor
{

	private String[] defualt_location = new String[] {"textures/models/armor/iron_layer_1.png", "textures/models/armor/iron_layer_2.png"};

	private boolean shouldDefualt = false;

	private String armorName;

	private Item source = null;

	public ItemLostArmor(EntityEquipmentSlot armorType, ArmorMaterial material, String name, Item repair)
	{
		super(material, 0, armorType);

		this.source = repair;
		this.armorName = name;
	}

	@Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
    	if (tab == AetherCreativeTabs.armor || tab == CreativeTabs.SEARCH)
    	{
            items.add(new ItemStack(this));
    	}
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
    	boolean leggings = this.getUnlocalizedName().contains("leggings");
    	String type1 = leggings ? "layer_2" : "layer_1";

        return this.shouldDefualt ? (leggings ? defualt_location[1] : defualt_location[0]) : Aether.modAddress() + "textures/armor/" + this.armorName + "_" + type1 + ".png";
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default)
    {
    	if (this == ItemsLostAether.agility_boots)
    	{
    		return new ModelAgilityBoots();
    	}
    	else
    	{
    		return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
    	}
    }

    @Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
	    return source == null ? false : repair.getItem() == source;
	}

	@Override
    public EnumRarity getRarity(ItemStack stack)
    {
    	return !this.armorName.contains("zanite") && !this.armorName.contains("gravitite")? ItemsAether.aether_loot : super.getRarity(stack);
    }
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.create();

		if (slot == EntityEquipmentSlot.FEET && this == ItemsLostAether.agility_boots)
		{
			multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Speed modifier", 0.05D, 0));
		}

		return multimap;
	}
}