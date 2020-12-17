package com.legacy.lost_aether.event;

import com.aether.api.AetherAPI;
import com.legacy.lost_aether.item.LostShieldItem;
import com.legacy.lost_aether.registry.LostContentItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LostContentEvents
{
	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event)
	{
		if (event.getEntityLiving() instanceof PlayerEntity)
		{
			// IPlayerAether playerAether = AetherAPI.getInstance().get((PlayerEntity)
			// event.getEntityLiving());
			LivingEntity living = event.getEntityLiving();
			DamageSource source = event.getSource();

			/*if (playerAether != null)
			{
				boolean isShielding = (living.getActiveItemStack().getItem() instanceof ItemAetherShield) && this.canBlockDamageSource((PlayerEntity) event.getEntityLiving(), source);
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
			}*/

			if (living.getActiveItemStack().getItem() instanceof LostShieldItem && event.getAmount() > 0.0F && canBlockDamageSource((PlayerEntity) event.getEntityLiving(), source))
			{
				source.getImmediateSource().playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.4F, 0.8F + living.world.rand.nextFloat() * 0.4F);

				if (living.getActiveItemStack().getItem() == LostContentItems.gravitite_shield && source.getImmediateSource() instanceof LivingEntity)
				{
					((LivingEntity) source.getImmediateSource()).applyKnockback(1.5F, living.getPosX() - source.getImmediateSource().getPosX(), living.getPosZ() - source.getImmediateSource().getPosZ());
					source.getImmediateSource().setPosition(source.getImmediateSource().getPosX(), source.getImmediateSource().getPosY() + 1D, source.getImmediateSource().getPosZ());
					source.getImmediateSource().isAirBorne = true;

				}

				if (living.getActiveItemStack().getItem() == LostContentItems.jeb_shield)
				{
					Entity projectile = source.getImmediateSource();

					if (source.getImmediateSource() instanceof ProjectileEntity)
					{
						Vector3d vec3d = living.getLook(1.0F);
						double x = -(living.getPosX() - (living.getPosX() + vec3d.x * 16.0D));
						double y = vec3d.y * 8;
						double z = -(living.getPosZ() - (living.getPosZ() + vec3d.z * 16.0D));
						((ProjectileEntity) source.getImmediateSource()).shoot(x, y, z, -15F, 0.0F);
						((ProjectileEntity) projectile).setShooter(living);
					}
				}
			}
		}
		else if (event.getSource().getImmediateSource() instanceof PlayerEntity)
		{
			DamageSource source = event.getSource();
			PlayerEntity player = (PlayerEntity) source.getImmediateSource();

			AetherAPI.get((PlayerEntity) source.getImmediateSource()).ifPresent((playerAether) ->
			{
				if (playerAether.getAccessoryInventory().isWearingAccessory(LostContentItems.power_gloves) && player.getHeldItemMainhand().isEmpty() && event.getEntityLiving().hurtTime <= 0)
				{
					player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1.0F, 1.0F);
					player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.4F, 1.0F);
					event.getEntityLiving().applyKnockback(1.5F, source.getImmediateSource().getPosX() - event.getEntityLiving().getPosX(), source.getImmediateSource().getPosZ() - event.getEntityLiving().getPosZ());
					// playerAether.getAccessoryInventory().damageWornStack(1, new
					// ItemStack(ItemsLostAether.power_gloves));
				}
			});
		}

	}

	public static boolean canBlockDamageSource(PlayerEntity player, DamageSource damageSourceIn)
	{
		Entity entity = damageSourceIn.getImmediateSource();
		boolean flag = false;
		if (entity instanceof AbstractArrowEntity)
		{
			AbstractArrowEntity abstractarrowentity = (AbstractArrowEntity) entity;
			if (abstractarrowentity.getPierceLevel() > 0)
			{
				flag = true;
			}
		}

		if (!damageSourceIn.isUnblockable() && player.isActiveItemStackBlocking() && !flag)
		{
			Vector3d vector3d2 = damageSourceIn.getDamageLocation();
			if (vector3d2 != null)
			{
				Vector3d vector3d = player.getLook(1.0F);
				Vector3d vector3d1 = vector3d2.subtractReverse(player.getPositionVec()).normalize();
				vector3d1 = new Vector3d(vector3d1.x, 0.0D, vector3d1.z);
				if (vector3d1.dotProduct(vector3d) < 0.0D)
				{
					return true;
				}
			}
		}

		return false;
	}

	public static void knockBack(Entity knocked, Entity attacker, float strength, double xRatio, double zRatio)
	{
		/*float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
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
		}*/
	}
}
