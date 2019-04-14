package com.legacy.lostaether.entities;

import java.util.List;

import javax.annotation.Nullable;

import com.legacy.aether.Aether;
import com.legacy.aether.api.AetherAPI;
import com.legacy.aether.api.player.util.IAetherBoss;
import com.legacy.aether.entities.hostile.EntityWhirlwind;
import com.legacy.aether.items.ItemsAether;
import com.legacy.aether.registry.sounds.SoundsAether;
import com.legacy.lostaether.BlocksLostAether;
import com.legacy.lostaether.LostLootTables;
import com.legacy.lostaether.entities.util.LostNameGen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityKingAerwhale extends EntityFlying implements IAetherBoss
{

	public static final DataParameter<String> WHALE_NAME = EntityDataManager.<String> createKey(EntityKingAerwhale.class, DataSerializers.STRING);

	public static final DataParameter<Boolean> WHALE_CHARGING = EntityDataManager.<Boolean> createKey(EntityKingAerwhale.class, DataSerializers.BOOLEAN);

	private int dungeonX, dungeonY, dungeonZ;

	private int targetX, targetY, targetZ;

	public int chatTime, attackDelay, stunTime, randomAttackChance;

	public float velocity;

	public boolean isTargetted, flipped;

	public EntityKingAerwhale(World world)
	{
		super(world);
		this.moveHelper = new EntityKingAerwhale.AIMoveControl(this);
		this.setSize(4F, 4F);
		this.isImmuneToFire = true;
		this.experienceValue = 30;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(500.0D);
		this.setHealth(500.0F);
	}

	protected void initEntityAI()
	{
		super.initEntityAI();
		this.tasks.addTask(0, new EntityKingAerwhale.AIDoNothing());
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.posX = Math.floor(this.posX + 0.5D);
		this.posY = Math.floor(this.posY + 0.5D);
		this.posZ = Math.floor(this.posZ + 0.5D);
		this.dataManager.register(WHALE_CHARGING, false);
		this.dataManager.register(WHALE_NAME, String.valueOf(LostNameGen.whaleGen()));
	}

	@Override
	public void move(MoverType type, double x, double y, double z)
	{
		super.move(type, x, y, z);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("dungeonX", this.dungeonX);
		nbttagcompound.setInteger("dungeonY", this.dungeonY);
		nbttagcompound.setInteger("dungeonZ", this.dungeonZ);
		nbttagcompound.setString("bossName", this.getBossName());
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readEntityFromNBT(nbttagcompound);
		this.dungeonX = nbttagcompound.getInteger("dungeonX");
		this.dungeonY = nbttagcompound.getInteger("dungeonY");
		this.dungeonZ = nbttagcompound.getInteger("dungeonZ");
		this.setBossName(nbttagcompound.getString("bossName"));
	}
	
	@Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return entityIn.getEntityBoundingBox();
    }

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		AxisAlignedBB radiusCheck = this.world.isRemote ? this.getEntityBoundingBox().grow(20.0D, 12.0D, 20.0D) : new AxisAlignedBB(new BlockPos(this.dungeonX, this.posY, this.dungeonZ)).grow(15, 12, 15);
		List<EntityPlayer> list = this.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, radiusCheck);
        for (EntityPlayer nearbyPlayers : list)
        {
            AetherAPI.getInstance().get(nearbyPlayers).setFocusedBoss(this);

            Block blockUnder = this.world.getBlockState(nearbyPlayers.getPosition().down()).getBlock();
            if (blockUnder == BlocksLostAether.songstone)
            {
            	this.setAttackTarget(nearbyPlayers);
            	
            	this.setDoor();
            	if (!world.isRemote)
            	{
            		this.world.destroyBlock(nearbyPlayers.getPosition().down(), false);
            	}
            	
            }
        }
        
		if (this.getAttackTarget() != null)
		{
			this.chargeTarget();
			
			if (!this.world.isRemote && list.isEmpty() && (this.getAttackTarget() != null && this.getAttackTarget().getDistance(this.dungeonX, this.dungeonY, this.dungeonZ) >= 20.0F || (this.getAttackTarget().getHealth() <= 0 || this.getAttackTarget().isDead))) //
			{
				this.reset();
				this.unlockDoor();
				this.world.setBlockState(new BlockPos(this.dungeonX, this.dungeonY, this.dungeonZ - 8), BlocksLostAether.songstone.getDefaultState());
			}
			
			if (!this.getStunned())
			{
				++EntityKingAerwhale.this.attackDelay;
			}
		}
		else if (!this.getMoveHelper().isUpdating())
		{
			this.reset();
		}

		if (this.getStunned())
		{	
			if (this.stunTime == 60)
			{
				this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2.0F, 1.0F);
				this.spawnExplosionParticle();
				
				if (!this.world.isRemote && rand.nextInt(2) == 0)
				{
					BlockPos blockpos = (new BlockPos(this.dungeonX + -6 + this.rand.nextInt(12), this.dungeonY, this.dungeonZ + -6 + this.rand.nextInt(12)));
					EntityWhirlwind whirly = new EntityWhirlwind(this.world);
					whirly.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
					whirly.actionTimer = -999999;
					whirly.onInitialSpawn(this.world.getDifficultyForLocation(blockpos), (IEntityLivingData)null);
		            this.world.spawnEntity(whirly);
				}
			}
			
			for (int i = 0; i < 2; ++i)
	        {
	            double d0 = this.rand.nextGaussian() * 0.02D;
	            double d1 = this.rand.nextGaussian() * 0.02D;
	            double d2 = this.rand.nextGaussian() * 0.02D;
	            this.world.spawnParticle(EnumParticleTypes.SPELL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
	        }
			
			this.attackDelay = 0;
			--this.stunTime;
		}
		
		if (this.posY > this.dungeonY + 2)
		{
			this.noClip = true;	
		}
		else
		{
			this.noClip = false;
		}
        
        List<EntityWhirlwind> whirlysWithinBox = this.world.<EntityWhirlwind>getEntitiesWithinAABB(EntityWhirlwind.class, this.getEntityBoundingBox().grow(2.5D, 2.5D, 2.5D));

        for(int l = 0; l < whirlysWithinBox.size(); l++)
        {
        	EntityWhirlwind whirlys = (EntityWhirlwind)whirlysWithinBox.get(l);
        	whirlys.setDead();
        }
		
		if (this.isDead)
		{
			this.unlockDoor();
		}
		
		if (!world.isRemote)
		{
			this.doCourse();
		}
		
	}
	
	public void chargeTarget()
	{
		EntityLivingBase entitylivingbase = this.getAttackTarget();
		BlockPos destPos = new BlockPos(this.targetX, this.targetY, this.targetZ);
        boolean hitTarget = this.getEntityBoundingBox().intersects(entitylivingbase.getEntityBoundingBox());
        
        if (!this.world.isRemote && this.attackDelay <= 3)
        {
        	randomAttackChance = rand.nextInt(150);
        }
        else if (this.attackDelay == 45 + randomAttackChance)
        {
        	this.isTargetted = true;
        	this.setTargetLockPos(EntityKingAerwhale.this.getAttackTarget().posX, EntityKingAerwhale.this.getAttackTarget().posY, EntityKingAerwhale.this.getAttackTarget().posZ);
        }
        else if (this.attackDelay >= 50 + randomAttackChance && !this.isCharging() && !this.getStunned())
        {
        	if (!world.isRemote)
    		{
        		EntityKingAerwhale.this.moveHelper.setMoveTo(destPos.getX(), destPos.getY(), destPos.getZ(), 10.0D);
    		}

        	this.setCharging(true);
        }

        if (hitTarget && !this.getStunned())
        {
        	this.attackEntityAsMob(entitylivingbase);
        	this.setCharging(false);
        	this.isTargetted = false;
        	this.setStunned(false);
        	this.setTargetLockPos(0, 0, 0);
        	return;
        }
        if (!this.getStunned() && this.getDistanceSq(destPos) <= 5.0F)
        {
        	this.stop();
        	this.setStunned(true);
        	this.setCharging(false);
        	this.isTargetted = false;
        	this.setTargetLockPos(0, 0, 0);
        	return;
        }
        else if (this.stunTime == 1)
        {
        	this.getMoveHelper().setMoveTo(this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ, 2.0F);
        }
        else if (this.attackDelay > 250)
        {
        	EntityKingAerwhale.this.attackDelay = 0;
        	EntityKingAerwhale.this.isTargetted = false;
            EntityKingAerwhale.this.setCharging(false);
            this.getMoveHelper().setMoveTo(this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ, 2.0F);
        }
        else
        {
        	return;
        }
	}

	@Override
	public void applyEntityCollision(Entity entity)
	{
		if (this.getAttackTarget() != null && this.isCharging() && entity != this.getAttackTarget())
		{
			this.attackEntityAsMob(entity); // TODO
		}
	}

	public void stop()
	{
		EntityKingAerwhale.this.motionX *= 0.5D;
		EntityKingAerwhale.this.motionY *= 0.5D;
		EntityKingAerwhale.this.motionZ *= 0.5D;
	}

	private void sendMessage(EntityPlayer player, ITextComponent s)
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (this.chatTime <= 0)
		{
			if (side.isClient())
			{
				Aether.proxy.sendMessage(player, s);
			}
			this.chatTime = 60;
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource ds, float var2)
	{
		if (ds.getImmediateSource() == null || !(ds.getImmediateSource() instanceof EntityPlayer))
		{
			return false;
		}
		
		EntityPlayer player = (EntityPlayer) ds.getImmediateSource();
		ItemStack stack = player.inventory.getCurrentItem();
		
		if (stack.getItem() == ItemsAether.developer_stick)
		{
			this.reset();
			return false;
		}
		
		else if (this.getAttackTarget() == null)
		{
			this.setDoor();
			this.setAttackTarget(player);
			return super.attackEntityFrom(ds, 0.0F);
		}

		
		
		else if (!this.getStunned())
		{
			return false;
		}
		
		AetherAPI.getInstance().get(player).setFocusedBoss(this);
		this.setAttackTarget(player);

		return super.attackEntityFrom(ds, Math.max(0, Math.min(var2, 20)));
	}

	public boolean attackEntityAsMob(Entity entityIn)
	{
		if (this.attackDelay > 10  && entityIn instanceof EntityLivingBase && !entityIn.getIsInvulnerable())
		{
			entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
			EntityLivingBase collidedEntity = (EntityLivingBase) entityIn;
			collidedEntity.addVelocity(collidedEntity.motionY * 2, 0.35D, collidedEntity.motionZ * 2);
			this.world.playSound(null, posX, posY, posZ, SoundsAether.zephyr_shoot, SoundCategory.HOSTILE, 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
			return true;
		}
		return super.attackEntityAsMob(entityIn);
	}

	@Override
	protected void collideWithNearbyEntities()
	{
		List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
		if (list != null && !list.isEmpty() && this.isCharging())
		{
			for (int i = 0; i < list.size(); ++i)
			{
				Entity entity = (Entity) list.get(i);
				this.applyEntityCollision(entity);
			}
		}
		super.collideWithNearbyEntities();
	}

	private void doCourse()
	{
		double flySpeed = this.getAttackTarget() != null ? 1.0F : 0.7F;
		double distanceToFirstTarget = EntityKingAerwhale.this.getDistanceSq(new BlockPos(this.dungeonX - 14, this.dungeonY + 12, this.dungeonZ));
		double distanceToSecondTarget = EntityKingAerwhale.this.getDistanceSq(new BlockPos(this.dungeonX, this.dungeonY + 12, this.dungeonZ - 14));
		double distanceToThirdTarget = EntityKingAerwhale.this.getDistanceSq(new BlockPos(this.dungeonX + 14, this.dungeonY + 12, this.dungeonZ));
		double distanceToFourthTarget = EntityKingAerwhale.this.getDistanceSq(new BlockPos(this.dungeonX, this.dungeonY + 12, this.dungeonZ + 14));

		if (!this.getStunned() && !this.isCharging())
		{
			
			if (!this.flipped)
			{
				if (distanceToFirstTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX, this.dungeonY + 12, this.dungeonZ - 16, flySpeed);
				}
				else if (distanceToSecondTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX + 16, this.dungeonY + 12, this.dungeonZ, flySpeed);
				}
				else if (distanceToThirdTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX, this.dungeonY + 12, this.dungeonZ + 16, flySpeed);
				}
				else if (distanceToFourthTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ, flySpeed);
				}	
			}
			else
			{
				if (distanceToFirstTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX, this.dungeonY + 12, this.dungeonZ + 16, flySpeed);
					//
				}
				else if (distanceToSecondTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ, flySpeed);
					//
				}
				else if (distanceToThirdTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX, this.dungeonY + 12, this.dungeonZ - 16, flySpeed);
					//this.getMoveHelper().setMoveTo(this.dungeonX, this.dungeonY + 12, this.dungeonZ + 16, flySpeed);
				}
				else if (distanceToFourthTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX + 16, this.dungeonY + 12, this.dungeonZ, flySpeed);
					//this.getMoveHelper().setMoveTo(this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ, flySpeed);
				}	
			}
			
		}
	}

	@Override
	public boolean canDespawn()
	{
		return false;
	}

	public boolean isNonBoss()
	{
		return false;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	@Override
	public void knockBack(Entity par1Entity, float par2, double par3, double par5)
	{
	}

	@Override
    public void addVelocity(double d, double d1, double d2)
    {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
    }

	public void reset()
	{
		this.stop();
		this.setAttackTarget(null);
		this.setHealth(this.getMaxHealth());
		this.isTargetted = false;
		this.setCharging(false);
		this.setStunned(false);
		this.setPositionAndUpdate(this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ);
	}

	public void setDungeon(double posX, double posY, double posZ)
	{
		this.dungeonX = (int) posX;
		this.dungeonY = (int) posY;
		this.dungeonZ = (int) posZ;
	}

	public void setTargetLockPos(double posX, double posY, double posZ)
	{
		this.targetX = (int) posX;
		this.targetY = (int) posY;
		this.targetZ = (int) posZ;
	}

	public void setBossName(String name)
	{
		this.dataManager.set(WHALE_NAME, name);
	}

	public String getBossName()
	{
		return this.dataManager.get(WHALE_NAME);
	}

	public boolean isCharging()
	{
		return this.dataManager.get(WHALE_CHARGING).booleanValue();
	}

	public void setCharging(boolean charging)
	{
		this.dataManager.set(WHALE_CHARGING, charging);
	}

	public boolean getStunned()
	{
		return this.stunTime > 0;
	}

	public void setStunned(boolean stunned)
	{
		if (stunned == false)
		{
			this.stunTime = 0;
			this.attackDelay = 0;
			this.getMoveHelper().setMoveTo(this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ, 2.0F);
		}
		else
		{
			this.stunTime = 60;
			this.flipped = rand.nextBoolean();
			System.out.println(this.flipped);
		}
	}

	@Override
	public String getBossTitle()
	{
		return this.getBossName() + ", " + new TextComponentTranslation("title.lost_aether.king_aerwhale.name", new Object[0]).getFormattedText();
	}

	@Override
	public boolean canBeLeashedTo(final EntityPlayer player)
	{
		return false;
	}

	@Override
	public float getBossHealth()
	{
		return this.getHealth();
	}

	@Override
	public float getMaxBossHealth()
	{
		return this.getMaxHealth();
	}

	@Override
	public SoundEvent getAmbientSound()
	{
		return SoundsAether.aerwhale_call;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundsAether.aerwhale_death;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return SoundsAether.aerwhale_death;
	}

	@Override
	protected float getSoundVolume()
	{
		return 3F;
	}

	@Override
	protected float getSoundPitch()
	{
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F;
	}

	@Override
	public float getEyeHeight()
	{
		return 1.8F * 0.85F;
	}

	@Override
	protected ResourceLocation getLootTable()
	{
		return LostLootTables.king_aerwhale;
	}

	private void setDoor()
    {
		int x, y, z;
		
		for (x = this.dungeonX - 1; x < this.dungeonX + 2; ++x)
        {
            for (y = this.dungeonY - 1; y < this.dungeonY; ++y)
            {
                for (z = this.dungeonZ + 1; z < this.dungeonZ + 6; ++z)
                {
                    BlockPos newPos = new BlockPos(x, y, z);
                    this.world.setBlockState(newPos, BlocksLostAether.locked_gale_stone.getDefaultState());
                }
            }
        }
    }
	
	private void unlockDoor()
    {
		int x, y, z;
		
		for (x = this.dungeonX - 1; x < this.dungeonX + 2; ++x)
        {
            for (y = this.dungeonY - 1; y < this.dungeonY; ++y)
            {
                for (z = this.dungeonZ + 1; z < this.dungeonZ + 5; ++z) //6
                {
                    BlockPos newPos = new BlockPos(x, y, z);
                    this.world.setBlockToAir(newPos);
                }
            }
        }
		
		for (x = this.dungeonX - 1; x < this.dungeonX + 2; ++x)
        {
            for (y = this.dungeonY - 1; y < this.dungeonY; ++y)
            {
                for (z = this.dungeonZ + 5; z < this.dungeonZ + 6; ++z)
                {
                    BlockPos newPos = new BlockPos(x, y, z);
                    this.world.setBlockState(newPos, BlocksLostAether.gale_stairs.getDefaultState().withRotation(Rotation.CLOCKWISE_180));
                }
            }
        }
		
		// Unlocks the dungeon blocks
		if (this.isDead || this.getHealth() <= 0)
		{
			for (x = this.dungeonX - 15; x < this.dungeonX + 30; ++x)
	        {
	            for (y = this.dungeonY  - 32; y < this.dungeonY + 32; ++y)
	            {
	                for (z = this.dungeonZ - 15; z < this.dungeonZ + 30; ++z)
	                {
	                    BlockPos newPos = new BlockPos(x, y, z);
	                    IBlockState unlock_block = this.world.getBlockState(newPos);
	
	                    if (unlock_block == BlocksLostAether.locked_gale_stone.getDefaultState())
	                    {
	                        this.world.setBlockState(newPos, BlocksLostAether.gale_stone.getDefaultState());
	                    }
	                    else if (unlock_block == BlocksLostAether.locked_light_gale_stone.getDefaultState())
	                    {
	                        this.world.setBlockState(newPos, BlocksLostAether.light_gale_stone.getDefaultState());
	                    }
	                }
	            }
	        }
		}
    }
	class AIMoveControl extends EntityMoveHelper
	{

		public AIMoveControl(EntityKingAerwhale whaleBoss)
		{
			super(whaleBoss);
		}

		public void onUpdateMoveHelper()
		{
			if (this.action == EntityMoveHelper.Action.MOVE_TO)
			{
				double d0 = this.posX - EntityKingAerwhale.this.posX;
				double d1 = this.posY - EntityKingAerwhale.this.posY;
				double d2 = this.posZ - EntityKingAerwhale.this.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				d3 = (double) MathHelper.sqrt(d3);
				if (d3 < EntityKingAerwhale.this.getEntityBoundingBox().getAverageEdgeLength())
				{
					this.action = EntityMoveHelper.Action.WAIT;
					EntityKingAerwhale.this.motionX *= 0.5D;
					EntityKingAerwhale.this.motionY *= 0.5D;
					EntityKingAerwhale.this.motionZ *= 0.5D;
				}
				else
				{
					EntityKingAerwhale.this.motionX += d0 / d3 * 0.05D * this.speed;
					EntityKingAerwhale.this.motionY += d1 / d3 * 0.05D * this.speed;
					EntityKingAerwhale.this.motionZ += d2 / d3 * 0.05D * this.speed;
					if (!EntityKingAerwhale.this.isCharging())
					{
						EntityKingAerwhale.this.rotationYaw = -((float) MathHelper.atan2(EntityKingAerwhale.this.motionX, EntityKingAerwhale.this.motionZ)) * (180F / (float) Math.PI);
						EntityKingAerwhale.this.renderYawOffset = EntityKingAerwhale.this.rotationYaw;
					}
				}
			}
			if (!EntityKingAerwhale.this.getStunned() && EntityKingAerwhale.this.isCharging())
			{
				EntityKingAerwhale.this.getLookHelper().setLookPosition(EntityKingAerwhale.this.targetX, EntityKingAerwhale.this.targetY, EntityKingAerwhale.this.targetZ, EntityKingAerwhale.this.getHorizontalFaceSpeed() * 4, EntityKingAerwhale.this.getVerticalFaceSpeed() * 4);
			}
		}
	}

	class AIDoNothing extends EntityAIBase
	{

		public AIDoNothing()
		{
			this.setMutexBits(7);
		}

		public boolean shouldExecute()
		{
			return EntityKingAerwhale.this.getStunned();
		}
	}
}