package com.legacy.lostaether.world;

import java.util.Random;

import com.legacy.aether.AetherConfig;
import com.legacy.lostaether.world.dungeon.PlatinumDungeonGenerator;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class AetherStructureGenerator extends WorldGenerator implements IWorldGenerator
{
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) 
	{
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;
		
		if (world.provider.getDimension() == AetherConfig.dimension.aether_dimension_id)
		{
			generateAether(world, rand, blockX + 8, blockZ + 8);
		}
	}
	
	private void generateAether(World world, Random rand, int blockX, int blockZ)
	{	
		if (rand.nextInt(800) == 0)
		{
			BlockPos pos = new BlockPos(blockX, 120, blockZ);
			WorldGenerator platinumDungeon = new PlatinumDungeonGenerator();
			platinumDungeon.generate(world, rand, pos);
		}
	}
	
	public static int getLakeFromAbove(World world, int x, int z)
	{
		int y = 255;
		boolean foundGround = false;
		while(!foundGround && y-- >= 31)
		{
			Block blockAt = world.getBlockState(new BlockPos(x,y,z)).getBlock();
			foundGround =  blockAt == Blocks.WATER||blockAt == Blocks.FLOWING_WATER;
		}

		return y;
	}

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		return false;
	}
}