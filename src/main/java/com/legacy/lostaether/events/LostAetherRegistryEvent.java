package com.legacy.lostaether.events;

import com.gildedgames.the_aether.api.accessories.AetherAccessory;
import com.gildedgames.the_aether.api.enchantments.AetherEnchantment;
import com.gildedgames.the_aether.api.moa.AetherMoaType;
import com.legacy.lostaether.blocks.BlocksLostAether;
import com.legacy.lostaether.client.sounds.LostSounds;
import com.legacy.lostaether.items.ItemsLostAether;
import com.legacy.lostaether.registry.LostAetherRegistries;
import com.legacy.lostaether.registry.LostMoaTypes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LostAetherRegistryEvent
{
	@SubscribeEvent
	public void onRegisterSounds(RegistryEvent.Register<SoundEvent> event)
	{
		LostSounds.soundRegistry = event.getRegistry();
		LostSounds.initialization();
	}

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

		BlocksLostAether.init();
		ItemsLostAether.initialization();
	}

	@SubscribeEvent
	public void onRegisterAccessoryEvent(RegistryEvent.Register<AetherAccessory> event)
	{
		LostAetherRegistries.initializeAccessories(event.getRegistry());
	}

	@SubscribeEvent
	public void onRegisterEnchantmentEvent(RegistryEvent.Register<AetherEnchantment> event)
	{
		LostAetherRegistries.initializeEnchantments(event.getRegistry());
	}

	@SubscribeEvent
	public void onRegisterMoaTypeEvent(RegistryEvent.Register<AetherMoaType> event)
	{
		LostMoaTypes.moaRegistry = event.getRegistry();
		LostMoaTypes.initialization();
	}
}