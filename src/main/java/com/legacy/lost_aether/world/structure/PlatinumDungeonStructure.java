package com.legacy.lost_aether.world.structure;

import java.util.Random;
import java.util.function.Function;

import com.legacy.lost_aether.LostContentMod;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class PlatinumDungeonStructure extends Structure<PlatinumDungeonConfig>
{
	public PlatinumDungeonStructure(Function<Dynamic<?>, ? extends PlatinumDungeonConfig> config)
	{
		super(config);
	}

	protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int posX, int posZ, int spacingOffsetsX, int spacingOffsetsZ)
	{
		int distance = 20;
		int separation = 11;
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
	}

	@Override
	public boolean hasStartAt(ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ)
	{
		ChunkPos chunkPos = this.getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);

		if (chunkPosX == chunkPos.x && chunkPosZ == chunkPos.z)
		{
			int y = getYValue(chunkGen, chunkPos.x, chunkPos.z);
			return y >= 50;
		}
		else
		{
			return false;
		}
	}

	@Override
	public IStartFactory getStartFactory()
	{
		return PlatinumDungeonStructure.Start::new;
	}

	@Override
	public String getStructureName()
	{
		return LostContentMod.locate("platinum_dungeon").toString();
	}

	@Override
	public int getSize()
	{
		return 3;
	}

	public static int getYValue(ChunkGenerator<?> chunkGen, int chunkX, int chunkZ)
	{
		Random random = new Random();
		return 100 + random.nextInt(50);
	}

	public static class Start extends StructureStart
	{
		public Start(Structure<?> structure, int chunkX, int chunkZ, Biome biome, MutableBoundingBox boundingBox, int referenceIn, long seed)
		{
			super(structure, chunkX, chunkZ, biome, boundingBox, referenceIn, seed);
		}

		public void init(ChunkGenerator<?> chunkGen, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			Rotation rotation = Rotation.NONE;
			int i = PlatinumDungeonStructure.getYValue(chunkGen, chunkX, chunkZ);

			BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
			// System.out.printf("small_pigman_village: (%d %d %d)\n/tp @s %d %d %d\n",
			// blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX(),
			// blockpos.getY(), blockpos.getZ());
			PlatinumDungeonPieces.init(templateManagerIn, blockpos, rotation, this.components, this.rand);
			this.recalculateStructureSize();
		}
	}
}
