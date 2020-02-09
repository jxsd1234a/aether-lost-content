package com.legacy.lostaether.events;

import com.legacy.aether.api.AetherAPI;
import com.legacy.aether.api.player.IPlayerAether;
import com.legacy.aether.blocks.dungeon.BlockTreasureChest;
import com.legacy.aether.tile_entities.TileEntityTreasureChest;
import com.legacy.lostaether.items.ItemsLostAether;
import com.legacy.lostaether.world.dungeon.PlatinumDungeonGenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Visibility;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerLostAetherEvents
{

	private boolean invisibilityUpdate, stepUpdate;

	@SubscribeEvent
	public void checkPlayerVisibility(Visibility event)
	{
		IPlayerAether playerAether = AetherAPI.getInstance().get(event.getEntityPlayer());

		if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(ItemsLostAether.invisibility_gem)))
		{
			event.modifyVisibility(0.0D);
		}
	}

	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event)
	{
		if ((event.getEntityLiving() instanceof EntityPlayer))
		{
			IPlayerAether playerAether = AetherAPI.getInstance().get((EntityPlayer) event.getEntityLiving());

			EntityPlayer player = ((EntityPlayer) event.getEntityLiving());

			if (playerAether != null)
			{
				if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(ItemsLostAether.sentry_shield)))
				{
				}

				if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(ItemsLostAether.invisibility_gem)))
				{
					this.invisibilityUpdate = true;
					playerAether.getEntity().setInvisible(true);
				}
				else if (!playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(ItemsLostAether.invisibility_gem)) && !playerAether.getEntity().isPotionActive(Potion.getPotionById(14)))
				{
					if (this.invisibilityUpdate)
					{
						playerAether.getEntity().setInvisible(false);
						this.invisibilityUpdate = false;
					}
				}

				if (player.inventory.armorInventory.get(0).getItem() == ItemsLostAether.agility_boots)
				{
					this.stepUpdate = true;
					playerAether.getEntity().stepHeight = 1.0F;
				}
				else
				{
					if (this.stepUpdate)
					{
						playerAether.getEntity().stepHeight = 0.5F;
						this.stepUpdate = false;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event)
	{
		if (event.getEntityLiving() instanceof EntityPlayer)
		{
			IPlayerAether playerAether = AetherAPI.getInstance().get((EntityPlayer) event.getEntityLiving());

			if (playerAether != null)
			{
				if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(ItemsLostAether.sentry_shield)))
				{
					// TODO: Add ability similar to the Sentry Boots, but on the shield slot!
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState state = world.getBlockState(pos);
		EntityPlayer player = event.getEntityPlayer();

		if (state.getBlock() instanceof BlockTreasureChest && player.getHeldItemMainhand().getItem() == ItemsLostAether.platinum_key)
		{
			TileEntityTreasureChest treasurechest = (TileEntityTreasureChest) world.getTileEntity(pos);

			ItemStack guiID = player.getHeldItemMainhand();

			if (treasurechest.isLocked())
			{

				treasurechest.unlock(3);

				int p;

				for (p = 0; p < 5 + world.rand.nextInt(1); ++p)
				{
					treasurechest.setInventorySlotContents(world.rand.nextInt(treasurechest.getSizeInventory()), PlatinumDungeonGenerator.getPlatinumLoot(world.rand));
				}

				guiID.shrink(1);
			}
		}
	}
}