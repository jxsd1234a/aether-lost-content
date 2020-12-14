package com.legacy.lost_aether;

import org.apache.commons.lang3.tuple.Pair;

import com.legacy.structure_gel.util.ConfigTemplates.StructureConfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class LostContentConfig
{
	public static final Common COMMON;
	protected static final ForgeConfigSpec COMMON_SPEC;
	static
	{
		Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static class Common
	{
		public final StructureConfig platinumDungeon;

		protected Common(ForgeConfigSpec.Builder builder)
		{
			this.platinumDungeon = new StructureConfig(builder, "platinum_dungeon").spacing(80).offset(20).biomes(true, "aether:aether_skylands");
		}
	}
}
