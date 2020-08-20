package com.legacy.lostaether.entities;

import java.util.List;

import com.gildedgames.the_aether.blocks.BlocksAether;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityFallingRock extends EntityLiving
{
	public EntityLivingBase shootingEntity;

	public float sinage[];

	public EntityFallingRock(World worldIn)
	{
		super(worldIn);

		this.setSize(0.9F, 0.9F);
		this.sinage = new float[3];

		for (int i = 0; i < 3; i++)
		{
			this.sinage[i] = this.rand.nextFloat() * 6F;
		}
	}

	public EntityFallingRock(World worldIn, EntityLivingBase shooter)
	{
		this(worldIn);

		this.shootingEntity = shooter;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		BlockPos blockpos = this.getPosition();

		if (this.onGround || this.ticksExisted > 200 || this.getPosition().getY() <= 0)
		{
			this.world.playEvent(2001, blockpos, Block.getStateId(BlocksAether.holystone.getDefaultState()));
			this.setDead();
		}

		if (this.motionY != 0)
		{
			List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
			for (Entity entity : list)
			{
				this.damage(entity, 0.0F);
			}
		}

		float f1 = 0.9F;

		if (this.isInWater())
		{
			for (int i = 0; i < 4; ++i)
			{
				this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
			}

			f1 = 0.6F;
		}

		if (this.isWet())
		{
			this.extinguish();
		}

		this.motionX *= (double) f1;
		this.motionY *= (double) f1;
		this.motionZ *= (double) f1;
	}

	@Override
	public void fall(float distance, float damageMultiplier)
	{
		int i = MathHelper.ceil(distance - 1.0F);

		if (i > 0)
		{
			List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
			for (Entity entity : list)
			{
				this.damage(entity, 1.0F);
			}
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	private void damage(Entity hitEntityIn, float amp)
	{
		EntityLivingBase spawner = this.shootingEntity;

		if (hitEntityIn.isEntityAlive() && !hitEntityIn.getIsInvulnerable() && hitEntityIn != spawner)
		{
			if (spawner == null)
			{
				hitEntityIn.attackEntityFrom(new DamageSource("rock_fall").setDamageBypassesArmor(), 6.0F + amp);
			}

			else
			{
				if (spawner.isOnSameTeam(hitEntityIn))
				{
					return;
				}

				hitEntityIn.attackEntityFrom(new EntityDamageSource("rock_fall_mob", spawner).setDamageBypassesArmor(), 6.0F + amp);
			}
		}
	}
}
