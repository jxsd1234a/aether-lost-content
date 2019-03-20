package com.legacy.lostaether.entities;

import javax.annotation.Nullable;

import com.legacy.aether.entities.util.EntityMountable;
import com.legacy.aether.items.ItemsAether;
import com.legacy.lostaether.LostLootTables;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityZephyroo extends EntityMountable
{
	public int maxJumps;

	public int jumpsRemaining;

	public int ticks;

	public EntityZephyroo(World world)
	{
		super(world);

		this.jumpsRemaining = 0;
		this.maxJumps = 0;
		this.stepHeight = 1.0F;

		this.ignoreFrustumCheck = true;
		this.canJumpMidAir = true;
		this.setSize(0.9F, 1.3F);
	}

	@Override
    protected void initEntityAI()
    {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
		this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.25D, ItemsAether.blue_berry, false));
		this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(5, new EntityAILookIdle(this));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
    }

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.9D);
		this.setHealth(40);

	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(this.moveForward != 0.0F && this.onGround)
        {
            this.jump();
        }
		
		if (this.onGround)
		{
			this.jumpsRemaining = this.maxJumps;
		}
		this.ticks++;

		this.fallDistance = 0;
		this.fall();	
		
	}

	@Override
	public double getMountedYOffset()
	{
		return 1.2D;
	}

	@Override
	public float getMountedMoveSpeed()
	{
		return 0.3F;
	}

	@Override
	protected void jump()
	{
		super.jump();
	}

	private void fall()
	{
	}

	@Override
	protected double getMountJumpStrength()
	{
		return 1.0D;
	}
	
	@Override
    public boolean processInteract(EntityPlayer entityplayer, EnumHand hand)
	{
		if (!this.isBeingRidden() && !this.isRiding() && !super.processInteract(entityplayer, hand))
		{
			if (!entityplayer.world.isRemote)
			{
				entityplayer.startRiding(this);
				entityplayer.prevRotationYaw = entityplayer.rotationYaw = this.rotationYaw;
			}

			return true;
		}
		
		return super.processInteract(entityplayer, hand);
	}
	
	@Override
    public void travel(float strafe, float vertical, float forward)
	{
		Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);

		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;

			this.prevRotationYaw = this.rotationYaw = player.rotationYaw;
			this.prevRotationPitch = this.rotationPitch = player.rotationPitch;
			this.rotationYawHead = this.rotationYaw;
			this.renderYawOffset = this.rotationYaw;
			
			strafe = player.moveStrafing;
			vertical = player.moveVertical;
			forward = player.moveForward;

			if (forward <= 0.0F)
			{
				forward *= 0.25F;
			}
			
			if (forward != 0.0F && this.onGround)
			{
				this.jump();
			}

			if (this.jumpPower > 0.0F && !this.isMountJumping() && (this.onGround || this.canJumpMidAir))
			{
				this.motionY = this.getMountJumpStrength() * (double) this.jumpPower;

				if (this.isPotionActive(MobEffects.JUMP_BOOST))
				{
					this.motionY += (double) ((float) (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
				}

				this.setMountJumping(true);
				this.isAirBorne = true;

				this.jumpPower = 0.0F;

				if (!this.world.isRemote)
				{
					this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
				}
			}

			this.motionX *= 0.35F;
			this.motionZ *= 0.35F;

			this.stepHeight = 1.0F;

			if (!this.world.isRemote)
			{
				this.jumpMovementFactor = this.getAIMoveSpeed() * 0.6F;
				super.travel(strafe, vertical, forward);
			}

			if (this.onGround)
			{
				this.jumpPower = 0.0F;
				this.setMountJumping(false);
			}

			this.prevLimbSwingAmount = this.limbSwingAmount;
			double d0 = this.posX - this.prevPosX;
			double d1 = this.posZ - this.prevPosZ;
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
			super.travel(strafe, vertical, forward);
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readEntityFromNBT(nbttagcompound);

		this.maxJumps = nbttagcompound.getShort("Jumps");
		this.jumpsRemaining = nbttagcompound.getShort("Remaining");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeEntityToNBT(nbttagcompound);

		nbttagcompound.setShort("Jumps", (short) this.maxJumps);
		nbttagcompound.setShort("Remaining", (short) this.jumpsRemaining);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entityageable)
	{
		return new EntityZephyroo(this.world);
	}

	public float getTimeTilJump() 
	{
		return 3;
	}
	
	@Nullable
    protected ResourceLocation getLootTable()
    {
        return LostLootTables.zephyroo;
    }

}