package com.legacy.lostaether.events;

import com.legacy.aether.api.accessories.AetherAccessory;
import com.legacy.aether.api.moa.AetherMoaType;
import com.legacy.lostaether.BlocksLostAether;
import com.legacy.lostaether.LostAetherRegistries;
import com.legacy.lostaether.LostMoaTypes;
import com.legacy.lostaether.items.ItemsLostAether;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LostAetherRegistryEvent
{

	@SubscribeEvent
	public void onRegisterBlockEvent(RegistryEvent.Register<Block> event)
	{
		BlocksLostAether.setBlockRegistry(event.getRegistry());
	}

	@SubscribeEvent
	public void onRegisterItemEvent(RegistryEvent.Register<Item> event)
	{
		BlocksLostAether.setItemRegistry(event.getRegistry());

		ItemsLostAether.itemRegistry = event.getRegistry();

		BlocksLostAether.initialization();
		ItemsLostAether.initialization();
	}

	@SubscribeEvent
	public void onRegisterAccessoryEvent(RegistryEvent.Register<AetherAccessory> event)
	{
		LostAetherRegistries.initializeAccessories(event.getRegistry());
	}

	@SubscribeEvent
	public void onRegisterMoaTypeEvent(RegistryEvent.Register<AetherMoaType> event)
	{
		LostMoaTypes.moaRegistry = event.getRegistry();

		LostMoaTypes.initialization();
	}
}