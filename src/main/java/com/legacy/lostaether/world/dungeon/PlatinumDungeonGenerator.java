package com.legacy.lostaether.world.dungeon;

import java.util.Random;

import com.legacy.aether.blocks.BlocksAether;
import com.legacy.aether.items.ItemMoaEgg;
import com.legacy.aether.items.ItemsAether;
import com.legacy.lostaether.BlocksLostAether;
import com.legacy.lostaether.LostMoaTypes;
import com.legacy.lostaether.entities.EntityKingAerwhale;
import com.legacy.lostaether.items.ItemsLostAether;
import com.legacy.lostaether.world.dungeon.util.StructurePlacer;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlatinumDungeonGenerator extends StructurePlacer
{
	public boolean generate (World worldIn, Random rand, BlockPos pos) 
	{		
		PlatinumDungeonPieces dungeon = new PlatinumDungeonPieces(worldIn, rand);
		
		pos = pos.add(dungeon.ground_4.getSize().getX() / -2 * 2, 0, dungeon.ground_4.getSize().getZ() / -2 * 2);
		
		if (pos.getY() >= 110 && pos.getY() <= 120 && worldIn.getBlockState(pos).getBlock() == Blocks.AIR)
		{
			System.out.println("Platinum Dungeon at: (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")");
			//System.out.println("If you noticed cascading worldgen lag, IGNORE IT. We will fix this issue at a later date.");
			
			placeStructure(worldIn, dungeon.ground_1, pos, Rotation.NONE);
			placeStructure(worldIn, dungeon.ground_2, pos.add(0, 0, -32), Rotation.NONE);
			placeStructure(worldIn, dungeon.ground_3, pos.add(-32, 0, 0), Rotation.NONE);
			placeStructure(worldIn, dungeon.ground_4, pos.add(-32, 0, -32), Rotation.NONE);
			
			placeStructure(worldIn, dungeon.tower_1, pos.add(0, 19, 0), Rotation.NONE);
			placeStructure(worldIn, dungeon.tower_2, pos.add(0, 19, -32), Rotation.NONE);
			placeStructure(worldIn, dungeon.tower_3, pos.add(-32, 19, 0), Rotation.NONE);
			placeStructure(worldIn, dungeon.tower_4, pos.add(-32, 19, -32), Rotation.NONE);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand)
    {
        if (function.contains("boss"))
        {
        	EntityKingAerwhale entity = new EntityKingAerwhale(worldIn);
        	entity.setPosition((double)pos.getX() - 16.0D, (double)pos.getY() + 12.0D, (double)pos.getZ()); //this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ
        	entity.setDungeon(pos.getX(), pos.getY(), pos.getZ());
        	entity.setRotationYawHead(180);
        	worldIn.spawnEntity(entity);
        	worldIn.setBlockToAir(pos);
        }
        else if (function.contains("platinum_chest"))
        {
        	worldIn.setBlockToAir(pos);
        }
        else if (function.contains("loot_chest"))
        {
        	BlockPos blockpos = pos.down();
			TileEntity tileentity = worldIn.getTileEntity(blockpos);
			if (tileentity instanceof TileEntityChest)
			{
				if (rand.nextInt(2) == 0)
				{
					int u;
					// ((TileEntityChest)tileentity).setLootTable(LostLootTables.platinum_normal_loot, rand.nextLong());

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
							worldIn.setBlockState(blockpos, BlocksAether.chest_mimic.getDefaultState().withRotation(Rotation.CLOCKWISE_90));	
						}
						else
						{
							worldIn.setBlockState(blockpos, BlocksAether.chest_mimic.getDefaultState().withRotation(Rotation.COUNTERCLOCKWISE_90));
						}
					}
					else
					{
						worldIn.setBlockState(blockpos, BlocksAether.chest_mimic.getDefaultState());	
					}
				}
			}
			
        	worldIn.setBlockToAir(pos);
        }
        
    }
	
	private ItemStack getNormalLoot(Random random)
	{
		int item = random.nextInt(10);
		switch(item)
		{
			case 0 :
				return new ItemStack(ItemsAether.zanite_pickaxe);
			case 1 :
				return new ItemStack(ItemsAether.skyroot_bucket, 1, 4);
			case 2 :
				return new ItemStack(ItemsAether.dart_shooter, 1, 2);
			case 3 :
				return ItemMoaEgg.getStackFromType(LostMoaTypes.brown);
			case 4 :
				return new ItemStack(ItemsAether.white_cape);
			case 5 :
			{
				if(random.nextInt(2) == 0)
					return new ItemStack(ItemsLostAether.zanite_shield);
				break;
			}
			case 6 : 
			{
				if(random.nextInt(20) == 0)
					return new ItemStack(ItemsAether.ice_pendant);
			}
			case 7 : 
			{
				if(random.nextInt(20) == 0)
					return new ItemStack(ItemsAether.ice_ring);
			}
			case 8 : 
			{
				if(random.nextInt(15) == 0)
					return new ItemStack(ItemsAether.zanite_ring);
			}
		}

		return new ItemStack(BlocksAether.aercloud, random.nextInt(4) + 1);
	}
	
	public static ItemStack getPlatinumLoot(Random random)
	{
		int item = random.nextInt(11);
		switch(item)
		{
			case 0 :
				return new ItemStack(ItemsAether.welcoming_skies);
			case 1 :
				if(random.nextInt(2) == 0)
				return new ItemStack(ItemsLostAether.agility_boots);
			case 2 :
				return new ItemStack(ItemsLostAether.power_gloves);
			case 3 :
				if(random.nextInt(4) == 0)
				return new ItemStack(ItemsLostAether.jeb_shield);
			case 4 :
				return new ItemStack(BlocksAether.enchanted_gravitite, random.nextInt(2) + 1);
			case 5 :
				return new ItemStack(ItemsLostAether.sentry_shield);
			case 6 :
				if(random.nextInt(2) == 0)
				return new ItemStack(ItemsLostAether.invisibility_gem);
			case 7 :
				return new ItemStack(ItemsAether.life_shard);
			case 8 :
				if(random.nextInt(6) == 0)
				return new ItemStack(ItemsAether.repulsion_shield);
			case 9 :
				if(random.nextInt(4) == 0)
					return new ItemStack(ItemsLostAether.phoenix_axe);
				if(random.nextInt(6) == 0)
					return new ItemStack(ItemsLostAether.phoenix_pickaxe);
				if(random.nextInt(2) == 0)
					return new ItemStack(ItemsLostAether.phoenix_shovel);
				if(random.nextInt(5) == 0)
					return new ItemStack(ItemsLostAether.phoenix_sword);
		}

		return new ItemStack(ItemsAether.cloud_staff);
	}
}