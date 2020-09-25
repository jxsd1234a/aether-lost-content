package com.legacy.lost_aether.entity;

import java.util.List;

import com.aether.block.AetherBlocks;
import com.google.common.collect.Lists;
import com.legacy.lost_aether.registry.LostContentEntityTypes;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FallingRockEntity extends MobEntity
{
	public LivingEntity shootingEntity;

	public float sinage[];

	public FallingRockEntity(EntityType<? extends FallingRockEntity> type, World worldIn)
	{
		super(type, worldIn);

		// this.setSize(0.9F, 0.9F);
		this.sinage = new float[3];

		for (int i = 0; i < 3; i++)
		{
			this.sinage[i] = this.rand.nextFloat() * 6F;
		}
	}

	public FallingRockEntity(World worldIn, LivingEntity shooter)
	{
		this(LostContentEntityTypes.FALLING_ROCK, worldIn);

		this.shootingEntity = shooter;
	}

	@Override
	public void tick()
	{
		super.tick();
		BlockPos blockpos = this.getPosition();

		if (this.onGround || this.ticksExisted > 200 || this.getPosition().getY() <= 0)
		{
			this.world.playEvent(2001, blockpos, Block.getStateId(AetherBlocks.HOLYSTONE.getDefaultState()));
			this.remove();
		}

		if (this.getMotion().getY() != 0)
		{
			List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox()));
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
				this.world.addParticle(ParticleTypes.BUBBLE, this.getPosX() - this.getMotion().getX() * 0.25D, this.getPosY() - this.getMotion().getY() * 0.25D, this.getPosZ() - this.getMotion().getZ() * 0.25D, this.getMotion().getX(), this.getMotion().getY(), this.getMotion().getZ());
			}

			f1 = 0.6F;
		}

		if (this.isWet())
		{
			this.extinguish();
		}

		this.setMotion(this.getMotion().mul(f1, f1, f1));
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier)
	{
		int i = MathHelper.ceil(distance - 1.0F);

		if (i > 0)
		{
			List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox()));
			for (Entity entity : list)
			{
				this.damage(entity, 1.0F);
			}
		}
		
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	private void damage(Entity hitEntityIn, float amp)
	{
		LivingEntity spawner = this.shootingEntity;

		if (hitEntityIn.isAlive() && !hitEntityIn.isInvulnerable() && hitEntityIn != spawner)
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
