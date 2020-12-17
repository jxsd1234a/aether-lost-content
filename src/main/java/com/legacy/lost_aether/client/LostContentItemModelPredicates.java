package com.legacy.lost_aether.client;

import com.legacy.lost_aether.registry.LostContentItems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;

public class LostContentItemModelPredicates
{
	public static void init()
	{
		createActivePredicate(LostContentItems.zanite_shield, "blocking");
		createActivePredicate(LostContentItems.gravitite_shield, "blocking");
		createActivePredicate(LostContentItems.jeb_shield, "blocking");
	}

	/**
	 * Activates when the mainhand is active (EX: blocking with a shield, eating a
	 * food item)
	 */
	private static void createActivePredicate(Item item, String predicateName)
	{
		ItemModelsProperties.registerProperty(item, new ResourceLocation(predicateName), (stack, world, entity) -> (entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack) ? 1.0F : 0.0F);
	}
}
