package com.legacy.lost_aether.world.structure;

import java.util.Random;

import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;
import com.legacy.structure_gel.worldgen.structure.GelConfigStructure;
import com.legacy.structure_gel.worldgen.structure.GelStructureStart;
import com.mojang.serialization.Codec;

import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
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

	/*protected ChunkPos getStartPositionForPosition(ChunkGenerator chunkGenerator, Random random, int posX, int posZ, int spacingOffsetsX, int spacingOffsetsZ)
	{
		int distance = 80;
		int separation = 20;
		int x = posX + distance * spacingOffsetsX;
		int z = posZ + distance * spacingOffsetsZ;
		int x1 = x < 0 ? x - distance + 1 : x;
		int z1 = z < 0 ? z - distance + 1 : z;
		int x2 = x1 / distance;
		int z2 = z1 / distance;
		((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), x2, z2, 10387313);
		x2 = x2 * distance;
		z2 = z2 * distance;
		x2 = x2 + (random.nextInt(distance - separation) + random.nextInt(distance - separation)) / 2;
		z2 = z2 + (random.nextInt(distance - separation) + random.nextInt(distance - separation)) / 2;
		return new ChunkPos(x2, z2);
	}*/

	@Override
	protected boolean func_230363_a_(ChunkGenerator chunkGen, BiomeProvider biomeProvider, long seed, SharedSeedRandom sharedSeedRand, int chunkPosX, int chunkPosZ, Biome biomeIn, ChunkPos chunkPosIn, NoFeatureConfig config)
	{
		return super.func_230363_a_(chunkGen, biomeProvider, seed, sharedSeedRand, chunkPosX, chunkPosZ, biomeIn, chunkPosIn, config);
		/*ChunkPos chunkPos = getChunkPosForStructure(this.getSeparationSettings(), seed, sharedSeedRand, chunkPosX, chunkPosZ);
		
		if (chunkPos.x == chunkPosX && chunkPos.z == chunkPosZ)
		{
			sharedSeedRand.setLargeFeatureSeedWithSalt(seed, chunkPosX, chunkPosZ, this.getSeed());
			int height = getYValue(chunkGen, chunkPos.x * 16, chunkPos.z * 16);
			return sharedSeedRand.nextDouble() < getProbability() && height <= 80 && height >= 60;
		}
		else
			return false;*/
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
			int i = PlatinumDungeonStructure.getYValue(chunkGen, chunkX, chunkZ);

			BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
			PlatinumDungeonPieces.init(templateManagerIn, blockpos, rotation, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}
