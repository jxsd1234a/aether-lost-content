package com.legacy.lost_aether.registry;

import com.legacy.lost_aether.LostContentConfig;
import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.world.structure.PlatinumDungeonPieces;
import com.legacy.lost_aether.world.structure.PlatinumDungeonStructure;
import com.legacy.structure_gel.registrars.StructureRegistrar;

import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent.Register;

public class LostContentStructures
{
	public static StructureRegistrar<NoFeatureConfig, PlatinumDungeonStructure> PLATINUM_DUNGEON;

	public static void init(Register<Structure<?>> event)
	{
		PLATINUM_DUNGEON = StructureRegistrar.of(LostContentMod.locate("platinum_dungeon"), new PlatinumDungeonStructure(NoFeatureConfig.field_236558_a_, LostContentConfig.COMMON.platinumDungeon), PlatinumDungeonPieces.Piece::new, NoFeatureConfig.NO_FEATURE_CONFIG, Decoration.SURFACE_STRUCTURES).handle().handleForge(event.getRegistry());
	}
}
