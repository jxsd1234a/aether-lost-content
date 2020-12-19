package com.legacy.lost_aether.block.util;

import java.util.Random;

import com.legacy.lost_aether.registry.LostContentFeatures;
import com.legacy.lost_aether.world.feature.CrystalTreeFeature;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class CrystalTree extends Tree
{
	public ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random random, boolean what)
	{
		return new CrystalTreeFeature(BaseTreeFeatureConfig.CODEC).withConfiguration(LostContentFeatures.DUMMY_TREE_CONFIG);
	}
}