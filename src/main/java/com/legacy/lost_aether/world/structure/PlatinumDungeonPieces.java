package com.legacy.lost_aether.world.structure;

import java.util.List;
import java.util.Random;

import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.registry.LostContentStructurePieceTypes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class PlatinumDungeonPieces
{
	private static final ResourceLocation ground_1 = locatePiece("ground_1");
	private static final ResourceLocation ground_2 = locatePiece("ground_2");
	private static final ResourceLocation ground_3 = locatePiece("ground_3");
	private static final ResourceLocation ground_4 = locatePiece("ground_4");

	private static final ResourceLocation dungeon_1 = locatePiece("dungeon_1");
	private static final ResourceLocation dungeon_2 = locatePiece("dungeon_2");
	private static final ResourceLocation dungeon_3 = locatePiece("dungeon_3");
	private static final ResourceLocation dungeon_4 = locatePiece("dungeon_4");

	public static void init(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> structurePieces, Random random)
	{
		structurePieces.add(new PlatinumDungeonPieces.Piece(templateManager, ground_1, pos, rotation));
		structurePieces.add(new PlatinumDungeonPieces.Piece(templateManager, ground_2, pos.add(0, 0, -32), rotation));
		structurePieces.add(new PlatinumDungeonPieces.Piece(templateManager, ground_3, pos.add(-32, 0, 0), rotation));
		structurePieces.add(new PlatinumDungeonPieces.Piece(templateManager, ground_4, pos.add(-32, 0, -32), rotation));

		structurePieces.add(new PlatinumDungeonPieces.Piece(templateManager, dungeon_1, pos.add(0, 19, 0), rotation));
		structurePieces.add(new PlatinumDungeonPieces.Piece(templateManager, dungeon_2, pos.add(0, 19, -32), rotation));
		structurePieces.add(new PlatinumDungeonPieces.Piece(templateManager, dungeon_3, pos.add(-32, 19, 0), rotation));
		structurePieces.add(new PlatinumDungeonPieces.Piece(templateManager, dungeon_4, pos.add(-32, 19, -32), rotation));
	}

	static ResourceLocation locatePiece(String location)
	{
		return LostContentMod.locate("platinum_dungeon/platinum_" + location);
	}

	public static class Piece extends TemplateStructurePiece
	{
		private final ResourceLocation location;
		private final Rotation rotation;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			super(LostContentStructurePieceTypes.PLATINUM_DUNGEON, 0);
			this.location = location;
			this.templatePosition = pos;
			this.rotation = rotation;
			this.setupTemplate(templateManager);
		}

		public Piece(TemplateManager templateManager, CompoundNBT nbtCompound)
		{
			super(LostContentStructurePieceTypes.PLATINUM_DUNGEON, nbtCompound);
			this.location = new ResourceLocation(nbtCompound.getString("Template"));
			this.rotation = Rotation.valueOf(nbtCompound.getString("Rot"));
			this.setupTemplate(templateManager);
		}

		private void setupTemplate(TemplateManager templateManager)
		{
			Template template = templateManager.getTemplateDefaulted(this.location);
			BlockPos sizePos = templateManager.getTemplate(this.location).getSize();
			BlockPos centerPos = new BlockPos(sizePos.getX() / 2, 0, sizePos.getZ() / 2);
			PlacementSettings placementSettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).setCenterOffset(centerPos).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
			this.setup(template, this.templatePosition, placementSettings);
		}

		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putString("Template", this.location.toString());
			tagCompound.putString("Rot", this.rotation.name());
		}

		protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb)
		{

		}
	}
}
