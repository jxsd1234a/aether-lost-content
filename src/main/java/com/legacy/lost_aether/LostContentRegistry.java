package com.legacy.lost_aether;

import com.aether.item.AetherItemGroups;
import com.legacy.lost_aether.registry.LostContentBlocks;
import com.legacy.lost_aether.registry.LostContentFeatures;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = LostContentMod.MODID, bus = Bus.MOD)
public class LostContentRegistry
{
	@SubscribeEvent
	public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> event)
	{
		/*LostContentSounds.init(event.getRegistry());*/
	}

	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
	{
		LostContentBlocks.init(event);
	}

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event)
	{
		/*LostContentItems.init(event);*/
		
		for (int i3 = 0; i3 < LostContentBlocks.blockList.size(); ++i3)
		{
			LostContentRegistry.register(event.getRegistry(), LostContentBlocks.blockList.get(i3).getRegistryName().toString().replace(LostContentMod.MODID + ":", ""), new BlockItem(LostContentBlocks.blockList.get(i3), (new Item.Properties().group(AetherItemGroups.AETHER_BLOCKS))));
		}
	}

	@SubscribeEvent
	public static void onRegisterEntityTypes(Register<EntityType<?>> event)
	{
		/*LostContentEntityTypes.init(event);
		LostContentEntityTypes.registerSpawnPlacements();*/
	}

	@SubscribeEvent
	public static void registerFeatures(Register<Feature<?>> event)
	{
		LostContentFeatures.init(event);
	}

	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> registry, String name, T object)
	{
		object.setRegistryName(LostContentMod.locate(name));
		registry.register(object);
	}
}