package com.legacy.lost_aether.registry;

import java.util.Locale;

import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.world.structure.PlatinumDungeonPieces;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class LostContentStructurePieceTypes
{

	public static final IStructurePieceType PLATINUM_DUNGEON = register(PlatinumDungeonPieces.Piece::new, "platinum_dungeon");

	public static void init()
	{
	}

	static IStructurePieceType register(IStructurePieceType pieceType, String key)
	{
		return Registry.register(Registry.STRUCTURE_PIECE, LostContentMod.locate(key.toLowerCase(Locale.ROOT)), pieceType);
	}
}
