package com.legacy.lost_aether.registry;

import com.aether.Aether;
import com.aether.block.AetherBlocks;
import com.google.common.collect.ImmutableList;
import com.legacy.structure_gel.access_helpers.BiomeAccessHelper;
import com.legacy.structure_gel.events.RegisterDimensionEvent;
import com.legacy.structure_gel.util.GelCollectors;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

public class LostContentFeatures
{
	public static final BaseTreeFeatureConfig DUMMY_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(Blocks.OAK_LEAVES.getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build(); // DefaultBiomeFeatures.OAK_TREE_CONFIG;

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