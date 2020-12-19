package com.legacy.lost_aether.world.feature;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.aether.block.AetherBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;

public class CrystalTreeFeature extends TreeFeature
{
	public static final Direction[] HORIZONTAL_DIRECTIONS = Arrays.stream(Direction.values()).filter(d -> d.getAxis() != Axis.Y).toArray(Direction[]::new);

	public CrystalTreeFeature(Codec<BaseTreeFeatureConfig> codec)
	{
		super(codec);
	}

	@Override
	public boolean place(IWorldGenerationReader generationReader, Random rand, BlockPos pos, Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, MutableBoundingBox bounds, BaseTreeFeatureConfig configIn)
	{
		if (generationReader instanceof ISeedReader)
		{
			ISeedReader world = (ISeedReader) generationReader;

			if (!isAreaOk(world, pos, 7, 1, 8, 3))
				return false;

			for (int i = 0; i < 7; ++i)
				setBlock(changedLogs, world, pos.up(i), AetherBlocks.SKYROOT_LOG.getDefaultState(), bounds);

			setBlock(changedLeaves, world, pos.up(7), this.getRandomLeaf(world.getRandom()), bounds);

			int smallWidth = 1;

			for (int i = 0; i < 5; ++i)
			{
				for (Direction dir : HORIZONTAL_DIRECTIONS)
					setBlock(changedLeaves, world, pos.up(2 + i).offset(dir), this.getRandomLeaf(world.getRandom()), bounds);

				if (i == 1 || i == 3)
				{
					for (int x = -smallWidth; x <= smallWidth; x++)
						for (int z = -smallWidth; z <= smallWidth; z++)
							setBlock(changedLeaves, world, pos.add(x, 2 + i, z), this.getRandomLeaf(world.getRandom()), bounds);

					for (Direction dir : HORIZONTAL_DIRECTIONS)
						setBlock(changedLeaves, world, pos.up(2 + i).offset(dir, 2), this.getRandomLeaf(world.getRandom()), bounds);
				}
			}

			int width = 2;

			for (int x = -width; x <= width; x++)
				for (int z = -width; z <= width; z++)
					if (!(Math.abs(x) == width && Math.abs(x) == Math.abs(z)))
						setBlock(changedLeaves, world, pos.add(x, 2, z), this.getRandomLeaf(world.getRandom()), bounds);

			return true;
		}

		return false;
	}

	private BlockState getRandomLeaf(Random rand)
	{
		return rand.nextFloat() < 0.05F ? AetherBlocks.CRYSTAL_FRUIT_LEAVES.getDefaultState() : AetherBlocks.CRYSTAL_LEAVES.getDefaultState();
	}

	protected void setBlock(@Nullable Set<BlockPos> changedBlocks, ISeedReader world, BlockPos pos, BlockState state, MutableBoundingBox bounds)
	{
		if (world.isAirBlock(pos))
		{
			TreeFeature.func_236408_b_(world, pos, state);
			bounds.expandTo(new MutableBoundingBox(pos, pos));
			if (changedBlocks != null)
				changedBlocks.add(pos.toImmutable());
		}
	}

	public boolean isAreaOk(ISeedReader world, BlockPos pos, int trunkHeight, int trunkWidth, int leafStartHeight, int leafWidth)
	{
		if (pos.getY() >= 1 && pos.getY() + trunkHeight + 1 < world.getHeight())
		{
			// Trunk check
			if (trunkWidth == 1)
			{
				for (int y = 0; y <= 1 + trunkHeight && pos.getY() + y >= 1 && pos.getY() + y < world.getHeight(); y++)
					if (!this.isReplaceableByLogs(world, pos.up(y)) && !this.isLeavesOrLog(world.getBlockState(pos.up(y))))
						return false;
			}
			else
			{
				for (int y = 0; y <= 1 + trunkHeight && pos.getY() + y >= 1 && pos.getY() + y < world.getHeight(); y++)
					for (int x = 0; x < trunkWidth; x++)
						for (int z = 0; z < trunkWidth; z++)
							if (!this.isReplaceableByLogs(world, pos.add(x, y, z)) && !this.isLeavesOrLog(world.getBlockState(pos.add(x, y, z))))
								return false;
			}

			// Leaf check
			int width = Math.max((leafWidth - 1) / 2, 1);
			for (int y = leafStartHeight; y <= trunkHeight + 2 && pos.getY() + y >= 0 && pos.getY() + y < world.getHeight(); y++)
				for (int x = -width; x <= width; x++)
					for (int z = -width; z <= width; z++)
						if (!this.isReplaceableByLeaves(world, pos.add(x, y, z)) && !this.isLeavesOrLog(world.getBlockState(pos.add(x, y, z))))
							return false;

			return true;
		}

		return false;
	}

	public boolean isReplaceableByLeaves(ISeedReader world, BlockPos pos)
	{
		BlockState state = world.getBlockState(pos);
		return state.canBeReplacedByLeaves(world, pos) || state.getBlock() == Blocks.SNOW;
	}

	public boolean isReplaceableByLogs(ISeedReader world, BlockPos pos)
	{
		BlockState state = world.getBlockState(pos);
		return state.canBeReplacedByLogs(world, pos) || state.getBlock() instanceof BushBlock || state.getBlock() == Blocks.SNOW || state.getFluidState().isTagged(FluidTags.WATER);
	}

	public boolean isLeavesOrLog(BlockState state)
	{
		return state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.LEAVES);
	}
}
