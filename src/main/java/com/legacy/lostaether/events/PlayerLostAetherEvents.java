package com.legacy.lostaether.events;

import com.legacy.aether.Aether;
import com.legacy.aether.api.AetherAPI;
import com.legacy.aether.api.player.IPlayerAether;
import com.legacy.aether.blocks.dungeon.BlockTreasureChest;
import com.legacy.aether.entities.projectile.darts.EntityDartBase;
import com.legacy.aether.tile_entities.TileEntityTreasureChest;
import com.legacy.lostaether.items.ItemsLostAether;
import com.legacy.lostaether.items.tools.ItemAetherShield;
import com.legacy.lostaether.world.dungeon.PlatinumDungeonGenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Visibility;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerLostAetherEvents
{

	private boolean stepUpdate;

	@SubscribeEvent
	public void checkPlayerVisibility(Visibility event)
	{
		IPlayerAether playerAether = AetherAPI.getInstance().get(event.getEntityPlayer());
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
			EntityLivingBase living = event.getEntityLiving();
			DamageSource source = event.getSource();

			if (playerAether != null)
			{
				boolean isShielding = (living.getActiveItemStack().getItem() instanceof ItemAetherShield) && this.canBlockDamageSource((EntityPlayer) event.getEntityLiving(), source);
				if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(ItemsLostAether.sentry_shield)))
				{
					if (source.isExplosion())
					{
						playerAether.getAccessoryInventory().damageWornStack(1, new ItemStack(ItemsLostAether.sentry_shield));
						event.setCanceled(true);
					}
					else if (living.world.rand.nextBoolean() && !isShielding)
					{
						playerAether.getAccessoryInventory().damageWornStack(1, new ItemStack(ItemsLostAether.sentry_shield));
						living.world.createExplosion(living, living.posX, living.posY, living.posZ, 1, false);
					}
				}

				if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(ItemsLostAether.phoenix_cape)) && source.getImmediateSource() instanceof EntityLivingBase && !isShielding)
				{
					source.getImmediateSource().setFire(3);
					playerAether.getAccessoryInventory().damageWornStack(1, new ItemStack(ItemsLostAether.phoenix_cape));
				}

				if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(ItemsLostAether.invisibility_gem)))
				{
					if (!living.isPotionActive(MobEffects.INVISIBILITY) && !isShielding && living.world.rand.nextFloat() < 0.05F && !living.world.isRemote)
					{
						living.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 160, 0, true, true));
						Aether.proxy.spawnSmoke(living.world, living.getPosition());
					}
				}
			}

			if (living.getActiveItemStack().getItem() instanceof ItemAetherShield && event.getAmount() > 0.0F && this.canBlockDamageSource((EntityPlayer) event.getEntityLiving(), source))
			{
				source.getImmediateSource().playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.4F, 0.8F + living.world.rand.nextFloat() * 0.4F);

				this.damageShield((EntityPlayer) event.getEntityLiving(), event.getAmount());

				if (living.getActiveItemStack().getItem() == ItemsLostAether.gravitite_shield && source.getImmediateSource() instanceof EntityLivingBase)
				{
					((EntityLivingBase) source.getImmediateSource()).knockBack(living, 1.5F, living.posX - source.getImmediateSource().posX, living.posZ - source.getImmediateSource().posZ);
					source.getImmediateSource().setPosition(source.getImmediateSource().posX, source.getImmediateSource().posY + 1D, source.getImmediateSource().posZ);
					source.getImmediateSource().isAirBorne = true;

				}

				if (living.getActiveItemStack().getItem() == ItemsLostAether.jeb_shield)
				{
					Entity projectile = source.getImmediateSource();

					if (source.getImmediateSource() instanceof IProjectile)
					{
						Vec3d vec3d = living.getLook(1.0F);
						double x = -(living.posX - (living.posX + vec3d.x * 16.0D));
						double y = vec3d.y * 8;
						double z = -(living.posZ - (living.posZ + vec3d.z * 16.0D));
						((IProjectile) source.getImmediateSource()).shoot(x, y, z, -15F, 0.0F);

						if (projectile instanceof EntityArrow)
						{
							((EntityArrow) projectile).shootingEntity = living;
						}
						else if (projectile instanceof EntityDartBase)
						{
							((EntityDartBase) projectile).shootingEntity = living;
						}
					}
				}
			}
		}
		else if (event.getSource().getImmediateSource() instanceof EntityPlayer)
		{
			DamageSource source = event.getSource();
			IPlayerAether playerAether = AetherAPI.getInstance().get((EntityPlayer) source.getImmediateSource());
			EntityPlayer player = (EntityPlayer) source.getImmediateSource();

			if (playerAether != null)
			{
				if (playerAether.getAccessoryInventory().wearingAccessory(new ItemStack(ItemsLostAether.power_gloves)) && player.getHeldItemMainhand().isEmpty())
				{
					player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1.0F, 1.0F);
					player.playSound(SoundEvents.ENTITY_FIREWORK_LAUNCH, 0.4F, 1.0F);
					event.getEntityLiving().knockBack(source.getImmediateSource(), 1.5F, source.getImmediateSource().posX - event.getEntityLiving().posX, source.getImmediateSource().posZ - event.getEntityLiving().posZ);
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

				for (int p = 0; p < 5 + world.rand.nextInt(1); ++p)
				{
					treasurechest.setInventorySlotContents(world.rand.nextInt(treasurechest.getSizeInventory()), PlatinumDungeonGenerator.getPlatinumLoot(world.rand));
				}

				guiID.shrink(1);
			}
		}
	}

	protected void damageShield(EntityPlayer player, float damage)
	{
		if (damage >= 3.0F && player.getActiveItemStack().getItem() instanceof ItemAetherShield)
		{
			// living.playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8F, 0.8F +
			// living.world.rand.nextFloat() * 0.4F);

			ItemStack copyBeforeUse = player.getActiveItemStack().copy();
			int i = 1 + MathHelper.floor(damage);
			player.getActiveItemStack().damageItem(i, player);

			if (player.getActiveItemStack().isEmpty())
			{
				EnumHand enumhand = player.getActiveHand();
				net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, enumhand);

				if (enumhand == EnumHand.MAIN_HAND)
				{
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
				}
				else
				{
					player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
				}

				player.resetActiveHand();
				player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + player.world.rand.nextFloat() * 0.4F);
			}

		}
	}

	private boolean canBlockDamageSource(EntityPlayer player, DamageSource damageSourceIn)
	{
		if (!damageSourceIn.isUnblockable() && player.isActiveItemStackBlocking())
		{
			Vec3d vec3d = damageSourceIn.getDamageLocation();

			if (vec3d != null)
			{
				Vec3d vec3d1 = player.getLook(1.0F);
				Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(player.posX, player.posY, player.posZ)).normalize();
				vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

				if (vec3d2.dotProduct(vec3d1) < 0.0D)
				{
					return true;
				}
			}
		}

		return false;
	}

	public void knockBack(Entity knocked, Entity attacker, float strength, double xRatio, double zRatio)
	{
		float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
		knocked.motionX /= 2.0D;
		knocked.motionZ /= 2.0D;
		knocked.motionX -= xRatio / (double) f * (double) strength;
		knocked.motionZ -= zRatio / (double) f * (double) strength;

		if (knocked.onGround && ((EntityLivingBase) attacker).getActiveItemStack().getItem() != ItemsLostAether.gravitite_shield)
		{
			knocked.motionY /= 2.0D;
			knocked.motionY += (double) strength;

			if (knocked.motionY > 0.4000000059604645D)
			{
				knocked.motionY = 0.4000000059604645D;
			}
		}
		else
		{
			knocked.motionY = 1.0D;
		}
	}
}