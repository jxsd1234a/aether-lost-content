package com.legacy.lostaether;

import com.legacy.aether.api.accessories.AccessoryType;
import com.legacy.aether.api.accessories.AetherAccessory;
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
}