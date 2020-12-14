package com.legacy.lost_aether.entity;

import com.aether.entity.MountableEntity;
import com.aether.item.AetherItems;
import com.legacy.lost_aether.registry.LostContentEntityTypes;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ZephyrooEntity extends MountableEntity
{
	public int maxJumps;
	public int jumpsRemaining;
	public int ticks;

	public ZephyrooEntity(EntityType<? extends ZephyrooEntity> type, World world)
	{
		super(type, world);

		this.jumpsRemaining = 0;
		this.maxJumps = 0;
		this.stepHeight = 1.0F;

		this.ignoreFrustumCheck = true;
		this.canJumpMidAir = true;

		// this.setSize(0.9F, 1.7F);
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
		// this.goalSelector.addGoal(2, new MateGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.fromItems(AetherItems.BLUEBERRY), false));
		this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes()
	{
		return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 40.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4D);
	}

	@Override
	public void tick()
	{
		super.tick();

		if (this.moveForward != 0.0F && this.onGround)
		{
			this.jump();
		}

		if (this.onGround)
		{
			this.jumpsRemaining = this.maxJumps;
		}
		this.ticks++;

		this.fallDistance = 0;
	}

	@Override
	public double getMountedYOffset()
	{
		return 1.2D;
	}

	/*@Override
	public float getMountedMoveSpeed()
	{
		return 0.3F;
	}*/

	@Override
	protected void jump()
	{
		super.jump();
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier)
	{
		return false;
	}

	@Override
	protected double getMountJumpStrength()
	{
		return 1.0D;
	}

	@Override
	public void travel(Vector3d vec)
	{
		Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);

		if (entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) entity;

			this.prevRotationYaw = this.rotationYaw = player.rotationYaw;
			this.prevRotationPitch = this.rotationPitch = player.rotationPitch;
			this.rotationYawHead = this.rotationYaw;
			this.renderYawOffset = this.rotationYaw;

			float strafe = player.moveStrafing;
			float vertical = player.moveVertical;
			float forward = player.moveForward;

			if (forward <= 0.0F)
			{
				forward *= 0.25F;
			}

			if (forward != 0.0F && this.onGround)
			{
				this.jump();
			}

			this.setMotion(this.getMotion().mul(0.35F, 1, 0.35F));

			this.stepHeight = 1.0F;

			if (!this.world.isRemote)
			{
				this.jumpMovementFactor = this.getAIMoveSpeed() * 0.6F;
				super.travel(new Vector3d(strafe, vertical, forward));
			}

			if (this.onGround)
			{
				this.jumpPower = 0.0F;
				this.setMountJumping(false);
			}

			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d0 = this.getPosX() - this.prevPosX;
			double d1 = this.getPosZ() - this.prevPosZ;
			float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

			if (f4 > 1.0F)
			{
				f4 = 1.0F;
			}

			this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
			this.limbSwing += this.limbSwingAmount;
		}
		else
		{
			this.stepHeight = 0.5F;
			this.jumpMovementFactor = 0.02F;
			super.travel(vec);
		}
	}

	@Override
	public boolean isBreedingItem(ItemStack stack)
	{
		Item item = stack.getItem();
		return item == AetherItems.BLUEBERRY || item == AetherItems.ENCHANTED_BLUEBERRY;
	}

	@Override
	public ActionResultType func_230254_b_(PlayerEntity player, Hand hand)
	{
		ItemStack itemstack = player.getHeldItem(hand);

		if (!super.func_230254_b_(player, hand).isSuccessOrConsume() && itemstack.getItem() != Items.NAME_TAG && !this.isBreedingItem(itemstack) && !this.isChild())
		{
			player.startRiding(this);
			return ActionResultType.SUCCESS;
		}

		return super.func_230254_b_(player, hand);
	}

	@Override
	public AgeableEntity func_241840_a(ServerWorld world, AgeableEntity entityageable)
	{
		return LostContentEntityTypes.ZEPHYROO.create(world);
	}

	@Override
	public boolean canJump()
	{
		return false;
	}
}