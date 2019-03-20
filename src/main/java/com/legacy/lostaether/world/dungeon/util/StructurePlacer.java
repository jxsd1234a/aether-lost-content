package com.legacy.lostaether.world.dungeon.util;

import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public abstract class StructurePlacer extends WorldGenerator
{
	/**
     * Places a structure from "template" with rotation.
     */
	public void placeStructure(World worldIn, Template template, BlockPos pos, Rotation rotation)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
		
		PlacementSettings placementSettings = (new PlacementSettings()).setMirror(Mirror.NONE)
				.setIgnoreStructureBlock(false)
				.setRotation(rotation);
		
		template.addBlocksToWorld(worldIn, pos, placementSettings, 3);
		
		dataBlocks(worldIn, template, pos, placementSettings);
	}
	
	/**
     * Places a structure from "template".
     */
	public void placeStructure(World worldIn, Template template, BlockPos pos)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
		
		PlacementSettings placementSettings = (new PlacementSettings())
				.setIgnoreStructureBlock(false);
		
		template.addBlocksToWorld(worldIn, pos, placementSettings, 3);
		
		dataBlocks(worldIn, template, pos, placementSettings);
	}
	
	/**
     * Places a structure from "template" with rotation and integrity.
     */
	public void placeStructure(World worldIn, Template template, BlockPos pos, Rotation rotation, float integrity)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
		
		PlacementSettings placementSettings = (new PlacementSettings())
				.setIgnoreStructureBlock(false)
				.setRotation(rotation)
				.setIntegrity(integrity);
		
		template.addBlocksToWorld(worldIn, pos, placementSettings, 3);
		
		dataBlocks(worldIn, template, pos, placementSettings);
	}
	
	/**
     * Places a structure from "template" with integrity.
     */
	public void placeStructure(World worldIn, Template template, BlockPos pos, float integrity)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
		
		PlacementSettings placementSettings = (new PlacementSettings())
				.setIgnoreStructureBlock(false)
				.setIntegrity(integrity);
		
		template.addBlocksToWorld(worldIn, pos, placementSettings, 3);
		
		dataBlocks(worldIn, template, pos, placementSettings);
	}
	
	private void dataBlocks(World worldIn, Template template, BlockPos pos, PlacementSettings placementSettings)
	{
		Random rand = new Random();
		
		Map<BlockPos, String> map = template.getDataBlocks(pos, placementSettings);

        for (Entry<BlockPos, String> entry : map.entrySet())
        {
            String s = entry.getValue();
            handleDataMarker(s, entry.getKey(), worldIn, rand);
        }
	}
	
	/**
     * Override this method to tell data structure blocks what to do.
     */
	public abstract void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand);
	
}
