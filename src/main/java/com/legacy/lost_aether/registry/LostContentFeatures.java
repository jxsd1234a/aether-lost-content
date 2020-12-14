package com.legacy.lost_aether.registry;

import com.aether.Aether;
import com.aether.block.AetherBlocks;
import com.google.common.collect.ImmutableList;
import com.legacy.structure_gel.access_helpers.BiomeAccessHelper;
import com.legacy.structure_gel.events.RegisterDimensionEvent;
import com.legacy.structure_gel.util.GelCollectors;

import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.MobSpawnInfo;

public class LostContentFeatures
{
	/**
	 * Using the dimension register event to add things to the Aether biome. This
	 * allows us to access the JSON based world files... although in a hacky way,
	 * but this is all we've got. Awesome.
	 * 
	 * @param event
	 */
	@SuppressWarnings("deprecation")
	public static void onDimensionRegistry(final RegisterDimensionEvent event)
	{
		// why
		event.getDimensionRegistry().forEach((dim) ->
		{
			if (dim.getDimensionType().getEffects().getNamespace().equals(Aether.MODID))
				dim.getChunkGenerator().func_235957_b_().field_236193_d_ = GelCollectors.addToMap(dim.getChunkGenerator().func_235957_b_().field_236193_d_, LostContentStructures.PLATINUM_DUNGEON.getStructure(), LostContentStructures.PLATINUM_DUNGEON.getStructure().getSeparationSettings());
		});

		// WHY
		event.getBiomeRegistry().forEach((biome) ->
		{
			if (biome.getGenerationSettings().getSurfaceBuilder().get().getConfig().getTop().getBlock() == AetherBlocks.AETHER_GRASS_BLOCK)
			{
				BiomeAccessHelper.addSpawn(biome, EntityClassification.CREATURE, new MobSpawnInfo.Spawners(LostContentEntityTypes.ZEPHYROO, 10, 2, 2));
				BiomeAccessHelper.addStructure(biome, LostContentStructures.PLATINUM_DUNGEON.getStructureFeature(), LostContentStructures.PLATINUM_DUNGEON.getStructure().getSeparationSettings(), ImmutableList.of());
			}
		});
	}
}