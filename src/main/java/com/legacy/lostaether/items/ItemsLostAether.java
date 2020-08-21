package com.legacy.lostaether.items;

import com.gildedgames.the_aether.api.accessories.AccessoryType;
import com.gildedgames.the_aether.items.accessories.ItemAccessory;
import com.gildedgames.the_aether.items.armor.ItemAetherArmor;
import com.gildedgames.the_aether.items.util.EnumAetherToolType;
import com.gildedgames.the_aether.registry.creative_tabs.AetherCreativeTabs;
import com.legacy.lostaether.LostAetherContent;
import com.legacy.lostaether.items.armor.ItemLostArmor;
import com.legacy.lostaether.items.tools.ItemAetherShield;
import com.legacy.lostaether.items.tools.ItemPhoenixSword;
import com.legacy.lostaether.items.tools.ItemPhoenixTool;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemsLostAether
{
	public static Item phoenix_pickaxe, phoenix_axe, phoenix_shovel, phoenix_sword;

	public static Item phoenix_cape, sentry_shield, invisibility_gem, power_gloves;

	public static Item agility_boots, swetty_mask;

	public static Item iron_shield, zanite_shield, gravitite_shield, jeb_shield;

	public static Item platinum_key;

	public static IForgeRegistry<Item> itemRegistry;

	public static void initialization()
	{
		phoenix_pickaxe = register("phoenix_pickaxe", new ItemPhoenixTool(EnumAetherToolType.PICKAXE));
		phoenix_axe = register("phoenix_axe", new ItemPhoenixTool(EnumAetherToolType.AXE));
		phoenix_shovel = register("phoenix_shovel", new ItemPhoenixTool(EnumAetherToolType.SHOVEL));
		phoenix_sword = register("phoenix_sword", new ItemPhoenixSword());

		phoenix_cape = register("phoenix_cape", new ItemAccessory(AccessoryType.CAPE).setTexture("phoenix_cape").setElytraTexture("phoenix_cape_elytra").setDungeonLoot().setMaxDamage(50));
		sentry_shield = register("sentry_shield", new ItemAccessory(AccessoryType.SHIELD).setTexture("sentry").setInactiveTexture("sentry").setDungeonLoot().setMaxDamage(30));
		invisibility_gem = register("invisibility_gem", new ItemAccessory(AccessoryType.MISC).setDungeonLoot());
		power_gloves = register("power_gloves", new ItemAccessory(AccessoryType.GLOVE).setTexture("power_gloves").setDungeonLoot().setMaxDamage(300));

		swetty_mask = register("swetty_mask", new ItemAetherArmor(EntityEquipmentSlot.HEAD, ArmorMaterial.LEATHER, "swetty", null));
		agility_boots = register("agility_boots", new ItemLostArmor(EntityEquipmentSlot.FEET, ArmorMaterial.IRON, "agility", null));

		zanite_shield = register("zanite_shield", new ItemAetherShield()).setMaxDamage(672);
		gravitite_shield = register("gravitite_shield", new ItemAetherShield()).setMaxDamage(1008);
		jeb_shield = register("jeb_shield", new ItemAetherShield()).setMaxDamage(1344);

		platinum_key = register("platinum_key", new Item().setCreativeTab(AetherCreativeTabs.misc).setMaxStackSize(1));
	}

	public static Item register(String name, Item item)
	{
		item.setTranslationKey(name);
		itemRegistry.register(item.setRegistryName(LostAetherContent.locate(name)));

		return item;
	}

}