package com.legacy.lost_aether;

import com.legacy.lost_aether.registry.LostContentBlocks;
import com.legacy.lost_aether.registry.LostContentEntityTypes;
import com.legacy.lost_aether.registry.LostContentItems;
import com.legacy.lost_aether.registry.LostContentSounds;
import com.legacy.lost_aether.registry.LostContentStructures;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.EventPriority;
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
		LostContentSounds.init(event);
	}

	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event)
	{
		LostContentBlocks.init(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterItems(RegistryEvent.Register<Item> event)
	{
		/*LostContentItems.init(event);*/

		LostContentItems.registerBlockItems(event.getRegistry());
	}

	@SubscribeEvent
	public static void onRegisterEntityTypes(Register<EntityType<?>> event)
	{
		LostContentEntityTypes.init(event);
	}

	@SubscribeEvent
	public static void registerStructures(Register<Structure<?>> event)
	{
		LostContentStructures.init(event);
	}

	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> registry, String name, T object)
	{
		object.setRegistryName(LostContentMod.locate(name));
		registry.register(object);
	}
}