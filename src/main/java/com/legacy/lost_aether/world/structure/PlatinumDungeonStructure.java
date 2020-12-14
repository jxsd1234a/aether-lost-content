package com.legacy.lost_aether.world.structure;

import java.util.Random;

import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.legacy.structure_gel.worldgen.structure.GelConfigStructure;
import com.legacy.structure_gel.worldgen.structure.GelStructureStart;
import com.mojang.serialization.Codec;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class PlatinumDungeonStructure extends GelConfigStructure<NoFeatureConfig>
{
	public PlatinumDungeonStructure(Codec<NoFeatureConfig> codec, StructureConfig config)
	{
		super(codec, config);
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return PlatinumDungeonStructure.Start::new;
	}

	public static int getYValue(ChunkGenerator chunkGen, int chunkX, int chunkZ)
	{
		Random random = new Random();
		return 100 + random.nextInt(50);
	}

	public static class Start extends GelStructureStart<NoFeatureConfig>
	{
		public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int referenceIn, long seed)
		{
			super(structure, chunkX, chunkZ, boundingBox, referenceIn, seed);
		}

		@Override
		public void func_230364_a_(DynamicRegistries registry, ChunkGenerator chunkGen, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig configIn)
		{
			Rotation rotation = Rotation.NONE;
			int yHeight = PlatinumDungeonStructure.getYValue(chunkGen, chunkX, chunkZ);

			BlockPos blockpos = new BlockPos(chunkX * 16 + 8, yHeight, chunkZ * 16 + 8);
			PlatinumDungeonPieces.init(templateManagerIn, blockpos, rotation, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}
