package com.legacy.lostaether.registry;

import com.gildedgames.the_aether.api.accessories.AccessoryType;
import com.gildedgames.the_aether.api.accessories.AetherAccessory;
import com.gildedgames.the_aether.api.enchantments.AetherEnchantment;
import com.legacy.lostaether.items.ItemsLostAether;

import net.minecraftforge.registries.IForgeRegistry;

public class LostAetherRegistries
{
	public static void initializeAccessories(IForgeRegistry<AetherAccessory> registry)
	{
		registry.register(new AetherAccessory(ItemsLostAether.phoenix_cape, AccessoryType.CAPE));
		registry.register(new AetherAccessory(ItemsLostAether.sentry_shield, AccessoryType.SHIELD));
		registry.register(new AetherAccessory(ItemsLostAether.invisibility_gem, AccessoryType.MISC));
		registry.register(new AetherAccessory(ItemsLostAether.power_gloves, AccessoryType.GLOVE));
	}
	
	public static void initializeEnchantments(IForgeRegistry<AetherEnchantment> registry)
	{
		registry.register(new AetherEnchantment(ItemsLostAether.zanite_shield, 2250));
		registry.register(new AetherEnchantment(ItemsLostAether.gravitite_shield, 5500));
	}
}