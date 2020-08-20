package com.legacy.lostaether.world;

import java.util.Random;

import com.gildedgames.the_aether.blocks.BlocksAether;
import com.gildedgames.the_aether.blocks.natural.BlockCrystalLeaves;
import com.gildedgames.the_aether.blocks.util.EnumCrystalType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class AetherGenCrystalTree extends WorldGenerator
{

	public AetherGenCrystalTree()
	{
	}

	// what in god's name is this code anyway? I've taken it from the crystal tree
	// gen and cleaned it to the best of my ability.
	@Override
	public boolean generate(World world, Random random, BlockPos pos)
	{
		boolean cangen = true;

		if (cangen)
		{

			for (int y = pos.getY(); y <= pos.getY() + 6; y++)
			{
				world.setBlockState(new BlockPos(pos.getX(), y, pos.getZ()), BlocksAether.aether_log.getDefaultState());
			}

			setLeaves(pos.up(7), world, random);

			BlockPos newPos = pos.up(2);

			for (int z = -1; z < 2; ++z)
			{
				if (z != 0)
					world.setBlockState(newPos.south(z), BlocksAether.aether_log.getDefaultState());
			}
			
			for (int x = -1; x < 2; ++x)
			{
				if (x != 0)
					world.setBlockState(newPos.east(x), BlocksAether.aether_log.getDefaultState());
			}

			for (int z = -2; z < 3; ++z)
			{
				if (z != 0 || z != 1)
					setLeaves(newPos.south(z), world, random);
			}

			for (int x = -2; x < 3; ++x)
			{
				if (x != 0 || x != 1)
					setLeaves(newPos.east(x), world, random);
			}

			for (int x = -1; x < 2; ++x)
			{
				if (x != 0)
					setLeaves(newPos.add(x, 0, -2), world, random);
			}

			for (int x = -1; x < 2; ++x)
			{
				if (x != 0)
					setLeaves(newPos.add(x, 0, 2), world, random);
			}

			for (int z = -1; z < 2; ++z)
			{
				if (z != 0)
					setLeaves(newPos.add(-2, 0, z), world, random);
			}

			for (int z = -1; z < 2; ++z)
			{
				if (z != 0)
					setLeaves(newPos.add(2, 0, z), world, random);
			}

			for (int x = -1; x < 2; ++x)
			{
				for (int z = 1; z > -2; --z)
				{
					if (x != 0 || z != 0)
					{
						setLeaves(newPos.add(x, 0, z), world, random);
					}
				}
			}

			setLeaves(newPos.add(1, 0, 1), world, random);
			setLeaves(newPos.add(-1, 0, -1), world, random);

			newPos = pos.up(3);

			for (int z = -2; z < 3; ++z)
			{
				if (z != 0 || z != 1)
					setLeaves(newPos.south(z), world, random);
			}

			for (int x = -2; x < 3; ++x)
			{
				if (x != 0 || x != 1)
					setLeaves(newPos.east(x), world, random);
			}

			for (int x = -1; x < 2; ++x)
			{
				for (int z = 1; z > -2; --z)
				{
					if (x != 0 || z != 0)
					{
						setLeaves(newPos.add(x, 0, z), world, random);
					}
				}
			}

			setLeaves(newPos.add(1, 0, 1), world, random);
			setLeaves(newPos.add(-1, 0, -1), world, random);

			for (int z = -1; z < 2; ++z)
			{
				if (z != 0)
					setLeaves(newPos.south(z), world, random);
			}

			for (int x = -1; x < 2; ++x)
			{
				if (x != 0)
					setLeaves(newPos.east(x), world, random);
			}

			newPos = pos.up(4);

			for (int z = -1; z < 2; ++z)
			{
				if (z != 0)
					setLeaves(newPos.south(z), world, random);
			}

			for (int x = -1; x < 2; ++x)
			{
				if (x != 0)
					setLeaves(newPos.east(x), world, random);
			}

			newPos = pos.up(5);

			for (int z = -1; z < 2; ++z)
			{
				if (z != 0)
					world.setBlockState(newPos.south(z), BlocksAether.aether_log.getDefaultState());
			}

			for (int x = -1; x < 2; ++x)
			{
				if (x != 0)
					world.setBlockState(newPos.east(x), BlocksAether.aether_log.getDefaultState());
			}

			for (int z = -2; z < 3; ++z)
			{
				if (z != 0 || z != 1)
					setLeaves(newPos.south(z), world, random);
			}

			for (int x = -2; x < 3; ++x)
			{
				if (x != 0 || x != 1)
					setLeaves(newPos.east(x), world, random);
			}

			for (int x = -1; x < 2; ++x)
			{
				for (int z = 1; z > -2; --z)
				{
					if (x != 0 || z != 0)
					{
						setLeaves(newPos.add(x, 0, z), world, random);
					}
				}
			}

			setLeaves(newPos.add(1, 0, 1), world, random);
			setLeaves(newPos.add(-1, 0, -1), world, random);

			newPos = pos.up(6);

			for (int z = -1; z < 2; ++z)
			{
				if (z != 0)
					setLeaves(newPos.south(z), world, random);
			}

			for (int x = -1; x < 2; ++x)
			{
				if (x != 0)
					setLeaves(newPos.east(x), world, random);
			}

			return true;
		}

		return false;
	}

	protected void setLeaves(BlockPos pos, World world, Random random)
	{
		if (world.getBlockState(pos).getBlock() == Blocks.AIR)
			return;

		IBlockState state;

		int nextInt = random.nextInt(3);

		if (nextInt == 0)
			state = BlocksAether.crystal_leaves.getDefaultState().withProperty(BlockCrystalLeaves.leaf_type, EnumCrystalType.Crystal_Fruited);

		state = BlocksAether.crystal_leaves.getDefaultState();

		world.setBlockState(pos, state);
	}

}