package com.legacy.lost_aether.world.structure;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class PlatinumDungeonConfig implements IFeatureConfig
{
	public final double probability;

	public PlatinumDungeonConfig()
	{
		this.probability = 0.1D;
	}

	public <T> Dynamic<T> serialize(DynamicOps<T> ops)
	{
		return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("probability"), ops.createDouble(this.probability))));
	}

	public static <T> PlatinumDungeonConfig deserialize(Dynamic<T> dynamic)
	{
		return new PlatinumDungeonConfig();
	}
}