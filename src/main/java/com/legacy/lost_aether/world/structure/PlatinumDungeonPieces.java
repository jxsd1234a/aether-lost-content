package com.legacy.lost_aether.world.structure;

import java.util.List;
import java.util.Random;

import com.aether.block.AetherBlocks;
import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.entity.AerwhaleKingEntity;
import com.legacy.lost_aether.registry.LostContentBlocks;
import com.legacy.lost_aether.registry.LostContentEntityTypes;
import com.legacy.lost_aether.registry.LostContentStructures;
import com.legacy.structure_gel.worldgen.structure.GelTemplateStructurePiece;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
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

	public static class Piece extends GelTemplateStructurePiece
	{
		private final ResourceLocation location;
		private final Rotation rotation;

		public Piece(TemplateManager templateManager, ResourceLocation location, BlockPos pos, Rotation rotation)
		{
			super(LostContentStructures.PLATINUM_DUNGEON.getPieceType(), location, 0);
			this.location = location;
			this.templatePosition = pos;
			this.rotation = rotation;
			this.setupTemplate(templateManager);
		}

		public Piece(TemplateManager templateManager, CompoundNBT nbtCompound)
		{
			super(LostContentStructures.PLATINUM_DUNGEON.getPieceType(), nbtCompound);
			this.location = new ResourceLocation(nbtCompound.getString("Template"));
			this.rotation = Rotation.valueOf(nbtCompound.getString("Rot"));
			this.setupTemplate(templateManager);
		}

		@Override
		public BlockState modifyState(IServerWorld world, Random rand, BlockPos pos, BlockState originalState)
		{
			// Replace Gale Stone randomly with the light variant.
			if (originalState.getBlock() == LostContentBlocks.locked_gale_stone && rand.nextFloat() < 0.05F)
				return LostContentBlocks.locked_light_gale_stone.getDefaultState();

			return super.modifyState(world, rand, pos, originalState);
		}

		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putString("Template", this.location.toString());
			tagCompound.putString("Rot", this.rotation.name());
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb)
		{
			if (function.contains("boss"))
			{
				AerwhaleKingEntity entity = new AerwhaleKingEntity(LostContentEntityTypes.AERWHALE_KING, worldIn.getWorld());
				entity.setPosition((double) pos.getX() - 16.0D, (double) pos.getY() + 12.0D, (double) pos.getZ());
				entity.setDungeon(pos.getX(), pos.getY(), pos.getZ());
				entity.setRotationYawHead(180);
				worldIn.addEntity(entity);
				worldIn.setBlockState(pos.add(0, 0, -7), LostContentBlocks.songstone.getDefaultState(), 17);
			}
			// TODO: re-add once all the aether content is added.
			else if (function.contains("platinum_chest"))
			{
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);

				if (worldIn.getBlockState(pos.down()).hasProperty(ChestBlock.FACING))
					worldIn.setBlockState(pos.down(), AetherBlocks.TREASURE_CHEST.getDefaultState().with(ChestBlock.FACING, worldIn.getBlockState(pos.down()).get(ChestBlock.FACING)), 3);
			}
			/*else if (function.contains("loot_chest"))
			{
				BlockPos blockpos = pos.down();
				TileEntity tileentity = worldIn.getTileEntity(blockpos);
				if (tileentity instanceof TileEntityChest)
				{
					if (rand.nextInt(2) == 0)
					{
						int u;
						for (u = 0; u < 3 + rand.nextInt(3); u++)
						{
							((TileEntityChest) tileentity).setInventorySlotContents(rand.nextInt(((TileEntityChest) tileentity).getSizeInventory()), this.getNormalLoot(rand));
						}
					}
					else
					{
						if (blockpos.getY() > 145)
						{
							if (worldIn.getBlockState(blockpos.down()) == BlocksLostAether.locked_light_gale_stone.getDefaultState())
							{
								worldIn.setBlockState(blockpos, AetherBlocks.chest_mimic.getDefaultState().withRotation(Rotation.CLOCKWISE_90));
							}
							else
							{
								worldIn.setBlockState(blockpos, AetherBlocks.chest_mimic.getDefaultState().withRotation(Rotation.COUNTERCLOCKWISE_90));
							}
						}
						else
						{
							worldIn.setBlockState(blockpos, AetherBlocks.chest_mimic.getDefaultState());
						}
					}
				}
			
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			}*/

		}

		/*private ItemStack getNormalLoot(Random random)
		{
			int item = random.nextInt(10);
			switch (item)
			{
			case 0:
				return new ItemStack(AetherItems.ZANITE_PICKAXE);
			case 1:
				return new ItemStack(AetherItems.SKYROOT_REMEDY_BUCKET, 3);
			case 2:
				return new ItemStack(AetherItems.ENCHANTED_DART_SHOOTER, 2);
			case 3:
				return ItemMoaEgg.getStackFromType(LostMoaTypes.brown);
			case 4:
				return new ItemStack(AetherItems.WHITE_CAPE);
			case 5:
			{
				if (random.nextInt(2) == 0)
					return new ItemStack(LostContentItems.zanite_shield);
				break;
			}
			case 6:
			{
				if (random.nextInt(20) == 0)
					return new ItemStack(AetherItems.ICE_PENDANT);
			}
			case 7:
			{
				if (random.nextInt(20) == 0)
					return new ItemStack(AetherItems.ICE_RING);
			}
			case 8:
			{
				if (random.nextInt(15) == 0)
					return new ItemStack(AetherItems.ZANITE_RING);
			}
			}
		
			return new ItemStack(AetherBlocks.COLD_AERCLOUD, random.nextInt(4) + 1);
		}
		
		public static ItemStack getPlatinumLoot(Random random)
		{
			int item = random.nextInt(11);
			switch (item)
			{
			case 0:
				if (random.nextBoolean())
					return new ItemStack(AetherItems.MUSIC_DISC_WELCOMING_SKIES);
				else
					return new ItemStack(AetherItems.MUSIC_DISC_LEGACY);
			case 1:
				if (random.nextBoolean())
					return new ItemStack(LostContentItems.agility_boots);
			case 2:
				return new ItemStack(LostContentItems.power_gloves);
			case 3:
				if (random.nextInt(4) == 0)
					return new ItemStack(LostContentItems.jeb_shield);
			case 4:
				return new ItemStack(AetherBlocks.ENCHANTED_GRAVITITE, random.nextInt(2) + 1);
			case 5:
				return new ItemStack(LostContentItems.sentry_shield);
			case 6:
				if (random.nextBoolean())
					return new ItemStack(LostContentItems.invisibility_gem);
			case 7:
				return new ItemStack(AetherItems.LIFE_SHARD);
			case 8:
				if (random.nextInt(6) == 0)
					return new ItemStack(AetherItems.repulsion_shield);
			case 9:
				if (random.nextInt(4) == 0)
					return new ItemStack(LostContentItems.phoenix_axe);
				if (random.nextInt(6) == 0)
					return new ItemStack(LostContentItems.phoenix_pickaxe);
				if (random.nextBoolean())
					return new ItemStack(LostContentItems.phoenix_shovel);
				if (random.nextInt(5) == 0)
					return new ItemStack(LostContentItems.phoenix_sword);
			}
		
			return new ItemStack(AetherItems.CLOUD_STAFF);
		}*/
	}
}
