package com.legacy.lost_aether.registry;

import java.util.Map.Entry;
import java.util.function.Supplier;

import com.aether.Aether;
import com.aether.item.AetherItemGroups;
import com.legacy.lost_aether.LostContentRegistry;
import com.legacy.lost_aether.item.LostShieldItem;
import com.legacy.lost_aether.item.ParentingBlockItem;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;

public class LostContentItems
{
	public static Item phoenix_pickaxe, phoenix_axe, phoenix_shovel, phoenix_sword;

	public static Item phoenix_cape, sentry_shield, invisibility_gem, power_gloves;

	public static Item agility_boots, swetty_mask;

	public static Item iron_shield, zanite_shield, gravitite_shield, jeb_shield;

	public static Item platinum_key;

	private static IForgeRegistry<Item> iItemRegistry;

	public static void init(Register<Item> event)
	{
		LostContentItems.iItemRegistry = event.getRegistry();

		phoenix_shovel = register("phoenix_shovel", new ShovelItem(ItemTier.DIAMOND, 1.5F, -3.0F, (new Item.Properties().rarity(Aether.AETHER_LOOT)).group(AetherItemGroups.AETHER_TOOLS)));
		phoenix_pickaxe = register("phoenix_pickaxe", new PickaxeItem(ItemTier.DIAMOND, 1, -2.8F, (new Item.Properties().rarity(Aether.AETHER_LOOT)).group(AetherItemGroups.AETHER_TOOLS)));
		phoenix_axe = register("phoenix_axe", new AxeItem(ItemTier.DIAMOND, 5.0F, -3.0F, (new Item.Properties().rarity(Aether.AETHER_LOOT)).group(AetherItemGroups.AETHER_TOOLS)));
		phoenix_sword = register("phoenix_sword", new SwordItem(ItemTier.DIAMOND, 3, -2.4F, (new Item.Properties().rarity(Aether.AETHER_LOOT)).group(AetherItemGroups.AETHER_COMBAT)));

		phoenix_cape = register("phoenix_cape", new Item(new Item.Properties().maxDamage(50).group(AetherItemGroups.AETHER_ACCESSORIES).rarity(Aether.AETHER_LOOT)));
		sentry_shield = register("sentry_shield", new Item(new Item.Properties().maxDamage(30).group(AetherItemGroups.AETHER_ACCESSORIES).rarity(Aether.AETHER_LOOT)));
		invisibility_gem = register("invisibility_gem", new Item(new Item.Properties().maxStackSize(1).group(AetherItemGroups.AETHER_ACCESSORIES).rarity(Aether.AETHER_LOOT)));
		power_gloves = register("power_gloves", new Item(new Item.Properties().maxDamage(300).group(AetherItemGroups.AETHER_ACCESSORIES).rarity(Aether.AETHER_LOOT)));

		// swetty_mask = register("swetty_mask", new ItemAetherArmor(EntityEquipmentSlot.HEAD, ArmorMaterial.LEATHER, "swetty", null));
		// agility_boots = register("agility_boots", new ItemLostArmor(EntityEquipmentSlot.FEET, ArmorMaterial.IRON, "agility", null));

		zanite_shield = register("zanite_shield", new LostShieldItem(new Item.Properties().maxDamage(672).group(AetherItemGroups.AETHER_COMBAT)));
		gravitite_shield = register("gravitite_shield", new LostShieldItem(new Item.Properties().maxDamage(1008).group(AetherItemGroups.AETHER_COMBAT)));
		jeb_shield = register("jeb_shield", new LostShieldItem(new Item.Properties().maxDamage(1344).rarity(Aether.AETHER_LOOT).group(AetherItemGroups.AETHER_COMBAT)));

		platinum_key = register("platinum_key", new Item(new Item.Properties().maxStackSize(1).group(AetherItemGroups.AETHER_MISC).rarity(Aether.AETHER_LOOT)));
	}

	private static Item register(String name, Item item)
	{
		LostContentRegistry.register(iItemRegistry, name, item);
		return item;
	}

	public static void registerBlockItems(IForgeRegistry<Item> registry)
	{
		for (Entry<Block, Pair<ItemGroup, Supplier<Item>>> entry : LostContentBlocks.blockItemMap.entrySet())
			LostContentRegistry.register(registry, entry.getKey().getRegistryName().getPath(), new ParentingBlockItem(entry.getKey(), new Item.Properties().group(entry.getValue().getFirst()), entry.getValue().getSecond()));

		LostContentBlocks.blockItemMap.clear();

		for (Entry<Block, Item.Properties> entry : LostContentBlocks.blockItemPropertiesMap.entrySet())
			LostContentRegistry.register(registry, entry.getKey().getRegistryName().getPath(), new BlockItem(entry.getKey(), entry.getValue()));

		LostContentBlocks.blockItemPropertiesMap.clear();
	}
}
