package com.legacy.lost_aether.event;

import java.util.List;

import com.aether.api.AetherAPI;
import com.legacy.lost_aether.data.LostContentTags;
import com.legacy.lost_aether.item.LostShieldItem;
import com.legacy.lost_aether.registry.LostContentItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LostContentEvents
{
	@SubscribeEvent
	public static void destroyBlock(BlockEvent.BreakEvent event)
	{
		if (!(event.getWorld() instanceof ServerWorld))
			return;

		PlayerEntity player = event.getPlayer();
		ServerWorld world = (ServerWorld) event.getWorld();
		BlockState state = event.getState();
		BlockPos pos = event.getPos();
		Item item = player.getHeldItemMainhand().getItem();

		if (!player.isCreative() && player.func_234569_d_(state) && LostContentTags.Items.PHOENIX_TOOLS.contains(item))
		{
			List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, (TileEntity) world.getTileEntity(pos), player, player.getHeldItemMainhand());
			drops.forEach(itemStack -> Block.spawnAsEntity(world.getWorld(), pos, getSmeltedResult(itemStack, world.getWorld())));
			world.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
			return;
		}
	}

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

	private static ItemStack getSmeltedResult(ItemStack stack, World world)
	{
		ItemStack output = stack;
		IInventory iInventory = new Inventory(stack);
		output = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, iInventory, world).map((furnaceRecipe) ->
		{
			return furnaceRecipe.getCraftingResult(iInventory);
		}).orElse(stack);
		output.setCount(stack.getCount());
		return output;
	}
}