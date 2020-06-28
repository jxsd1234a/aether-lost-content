package com.legacy.lost_aether.registry;

import java.util.Locale;

import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.LostContentRegistry;
import com.legacy.lost_aether.world.structure.PlatinumDungeonConfig;
import com.legacy.lost_aether.world.structure.PlatinumDungeonStructure;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(LostContentMod.MODID)
public class LostContentFeatures
{
	public static final Structure<PlatinumDungeonConfig> PLATINUM_DUNGEON = new PlatinumDungeonStructure(PlatinumDungeonConfig::deserialize);

	public static void init(Register<Feature<?>> event)
	{
		LostContentStructurePieceTypes.init();

		registerStructure(event.getRegistry(), "platinum_dungeon", PLATINUM_DUNGEON);
	}

	@SuppressWarnings("deprecation")
	private static void registerStructure(IForgeRegistry<Feature<?>> registry, String key, Structure<?> structure)
	{
		LostContentRegistry.register(registry, key, structure);
		Registry.register(Registry.STRUCTURE_FEATURE, LostContentMod.locate(key.toLowerCase()), structure);
		Feature.STRUCTURES.put(LostContentMod.find(key.toLowerCase(Locale.ROOT)), structure);
	}
}
