package com.legacy.lostaether.entities;

import java.util.List;

import javax.annotation.Nullable;

import com.gildedgames.the_aether.Aether;
import com.gildedgames.the_aether.api.AetherAPI;
import com.gildedgames.the_aether.api.player.util.IAetherBoss;
import com.gildedgames.the_aether.entities.hostile.EntityWhirlwind;
import com.gildedgames.the_aether.items.ItemsAether;
import com.gildedgames.the_aether.registry.sounds.SoundsAether;
import com.legacy.lostaether.LostLootTables;
import com.legacy.lostaether.blocks.BlocksLostAether;
import com.legacy.lostaether.client.sounds.LostSounds;
import com.legacy.lostaether.entities.util.LostNameGen;
import com.legacy.lostaether.items.tools.ItemAetherShield;

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
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
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

public class EntityAerwhaleKing extends EntityFlying implements IAetherBoss
{
	public static final DataParameter<String> WHALE_NAME = EntityDataManager.<String>createKey(EntityAerwhaleKing.class, DataSerializers.STRING);
	public static final DataParameter<Boolean> WHALE_CHARGING = EntityDataManager.<Boolean>createKey(EntityAerwhaleKing.class, DataSerializers.BOOLEAN);

	private int dungeonX, dungeonY, dungeonZ;
	private int targetX, targetY, targetZ;
	public int chatTime, attackDelay, stunTime, randomAttackChance;
	public float velocity;
	public boolean isTargetted, courseFlipped;

	public EntityAerwhaleKing(World world)
	{
		super(world);
		this.moveHelper = new EntityAerwhaleKing.AIMoveControl(this);
		this.setSize(4F, 4F);
		this.isImmuneToFire = true;
		this.experienceValue = 30;
		this.ignoreFrustumCheck = true;
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
		this.tasks.addTask(0, new EntityAerwhaleKing.AIDoNothing());
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
		if (this.getStunned())
			super.move(type, 0, 0, 0);
		else
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

		if (this.isAIDisabled())
			return;

		AxisAlignedBB radiusCheck = this.world.isRemote ? this.getEntityBoundingBox().grow(20.0D, 12.0D, 20.0D) : new AxisAlignedBB(new BlockPos(this.dungeonX, this.posY, this.dungeonZ)).grow(15, 12, 15);
		List<EntityPlayer> playerList = this.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, radiusCheck);

		// Give all players in the area the boss bar. Mainly for multiplayer.
		for (EntityPlayer nearbyPlayers : this.getPlayerList())
		{
			AetherAPI.getInstance().get(nearbyPlayers).setFocusedBoss(this);
		}

		if (this.getAttackTarget() != null)
		{
			if (!world.isRemote)
				this.chargeTarget();

			if (!this.world.isRemote && playerList.isEmpty() && (this.getAttackTarget() != null && this.getAttackTarget().getDistance(this.dungeonX, this.dungeonY, this.dungeonZ) >= 20.0F || (this.getAttackTarget().getHealth() <= 0 || this.getAttackTarget().isDead))) //
			{
				this.reset();
				this.unlockDoor();
				this.world.setBlockState(new BlockPos(this.dungeonX, this.dungeonY, this.dungeonZ - 8), BlocksLostAether.songstone.getDefaultState());
			}

			if (!this.getStunned())
			{
				++EntityAerwhaleKing.this.attackDelay;
			}

			if (!playerList.isEmpty() && (this.getAttackTarget().isDead || this.getAttackTarget().getHealth() <= 0))
			{
				EntityPlayer randomPlayer = playerList.get(rand.nextInt(playerList.size()));
				if (randomPlayer != null)
					this.setAttackTarget(randomPlayer);
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
				this.spawnExplosionParticle();
				this.world.createExplosion(this, this.posX, this.posY, this.posZ, 2, false);

				if (!this.world.isRemote)
				{
					for (int w = 0; w < 4; ++w)
					{
						BlockPos blockpos = (new BlockPos(this.dungeonX + -6 + this.rand.nextInt(12), this.dungeonY, this.dungeonZ + -6 + this.rand.nextInt(12)));
						EntityWhirlwind whirly = new EntityWhirlwind(this.world);
						whirly.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
						whirly.actionTimer = -Integer.MAX_VALUE;
						whirly.capturedDrops.clear();
						whirly.onInitialSpawn(this.world.getDifficultyForLocation(blockpos), (IEntityLivingData) null);
						this.world.spawnEntity(whirly);
					}

					for (EntityPlayer players : this.getPlayerList())
					{
						for (int w = 0; w < 6; ++w)
						{
							BlockPos blockpos = (new BlockPos(players.posX + -7 + this.rand.nextInt(14), players.posY + 10 + this.rand.nextInt(5), players.posZ + -7 + this.rand.nextInt(14)));
							EntityFallingRock projectile = new EntityFallingRock(this.world, this);
							projectile.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
							// projectile.shoot(this, pitch, yaw, p_184547_4_, w, inaccuracy);

							this.world.spawnEntity(projectile);
						}
						
						BlockPos blockpos = (new BlockPos(players.posX, players.posY + 10 + this.rand.nextInt(5), players.posZ));
						EntityFallingRock projectile = new EntityFallingRock(this.world, this);
						projectile.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
						// projectile.shoot(this, pitch, yaw, p_184547_4_, w, inaccuracy);

						this.world.spawnEntity(projectile);
					}

				}
			}

			for (int i = 0; i < 2; ++i)
			{
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.spawnParticle(EnumParticleTypes.SPELL, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
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

		for (int l = 0; l < whirlysWithinBox.size(); l++)
		{
			EntityWhirlwind whirlys = (EntityWhirlwind) whirlysWithinBox.get(l);
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
			this.setTargetLockPos(EntityAerwhaleKing.this.getAttackTarget().posX, EntityAerwhaleKing.this.getAttackTarget().posY, EntityAerwhaleKing.this.getAttackTarget().posZ);
		}
		else if (this.attackDelay >= 50 + randomAttackChance && !this.isCharging() && !this.getStunned())
		{
			if (!world.isRemote)
			{
				EntityAerwhaleKing.this.moveHelper.setMoveTo(destPos.getX(), destPos.getY(), destPos.getZ(), 10.0D);
			}

			this.setCharging(true);
		}

		if (hitTarget && !this.getStunned() && this.attackEntityAsMob(entitylivingbase))
		{

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
			this.world.setEntityState(this, (byte) 4);
			// System.out.println("stunning from crash");

			return;
		}
		else if (this.stunTime == 1)
		{
			this.getMoveHelper().setMoveTo(this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ, 2.0F);
		}
		else if (this.attackDelay > 250)
		{
			EntityAerwhaleKing.this.attackDelay = 0;
			EntityAerwhaleKing.this.isTargetted = false;
			EntityAerwhaleKing.this.setCharging(false);
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
			this.attackEntityAsMob(entity);
		}
	}

	public void stop()
	{
		EntityAerwhaleKing.this.motionX *= 0.5D;
		EntityAerwhaleKing.this.motionY *= 0.5D;
		EntityAerwhaleKing.this.motionZ *= 0.5D;
	}

	private void sendMessage(EntityPlayer player, ITextComponent s)
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side.isClient())
		{
			Aether.proxy.sendMessage(player, s);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource ds, float var2)
	{
		if ((ds instanceof EntityDamageSourceIndirect || ds.isProjectile()) && ds.getTrueSource() instanceof EntityPlayer)
		{
			this.sendMessage((EntityPlayer) ds.getTrueSource(), new TextComponentTranslation("gui.lost_aether.projectile_miss"));
			this.spawnExplosionParticle();
			return false;
		}

		if (ds.getImmediateSource() == null || !(ds.getImmediateSource() instanceof EntityPlayer))
		{
			return false;
		}

		EntityPlayer player = (EntityPlayer) ds.getImmediateSource();
		ItemStack stack = player.inventory.getCurrentItem();

		if (stack.getItem() == ItemsAether.developer_stick && player.isCreative())
		{
			this.reset();
			return false;
		}
		else if (!this.getStunned())
		{
			return false;
		}

		AetherAPI.getInstance().get(player).setFocusedBoss(this);
		this.setAttackTarget(player);

		return super.attackEntityFrom(ds, Math.max(0, Math.min(var2, 20)));
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean flag = super.attackEntityAsMob(entityIn);

		if (this.attackDelay > 10 && entityIn instanceof EntityLivingBase && !entityIn.getIsInvulnerable())
		{
			if (entityIn instanceof EntityPlayer)
			{
				EntityPlayer playerentity = (EntityPlayer) entityIn;
				ItemStack playerItem = playerentity.isHandActive() ? playerentity.getActiveItemStack() : ItemStack.EMPTY;

				if (!playerItem.isEmpty() && (playerItem.getItem() instanceof ItemShield || playerItem.getItem() instanceof ItemAetherShield))
				{
					// System.out.println("stunning from shield");
					playerentity.disableShield(true);
					playerentity.getCooldownTracker().setCooldown(playerItem.getItem(), 600);
					this.world.setEntityState(playerentity, (byte) 30);

					this.stop();
					this.setStunned(true);
					this.setCharging(false);
					this.isTargetted = false;
					this.setTargetLockPos(0, 0, 0);
					this.world.setEntityState(this, (byte) 4);

					return false;

				}
				else
				{
					entityIn.attackEntityFrom(DamageSource.causeMobDamage(this).setDifficultyScaled(), 15.0F);
					EntityLivingBase collidedEntity = (EntityLivingBase) entityIn;
					collidedEntity.addVelocity(collidedEntity.motionY * 2, 0.35D, collidedEntity.motionZ * 2);
					this.world.playSound(null, posX, posY, posZ, SoundsAether.zephyr_shoot, SoundCategory.HOSTILE, 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
					return true;
				}
			}
			else
			{
				entityIn.attackEntityFrom(DamageSource.causeMobDamage(this).setDifficultyScaled(), 15.0F);
				EntityLivingBase collidedEntity = (EntityLivingBase) entityIn;
				collidedEntity.addVelocity(collidedEntity.motionY * 2, 0.35D, collidedEntity.motionZ * 2);
				this.world.playSound(null, posX, posY, posZ, SoundsAether.zephyr_shoot, SoundCategory.HOSTILE, 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
				return true;
			}
		}

		return flag;
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
		double distanceToFirstTarget = EntityAerwhaleKing.this.getDistanceSq(new BlockPos(this.dungeonX - 14, this.dungeonY + 12, this.dungeonZ));
		double distanceToSecondTarget = EntityAerwhaleKing.this.getDistanceSq(new BlockPos(this.dungeonX, this.dungeonY + 12, this.dungeonZ - 14));
		double distanceToThirdTarget = EntityAerwhaleKing.this.getDistanceSq(new BlockPos(this.dungeonX + 14, this.dungeonY + 12, this.dungeonZ));
		double distanceToFourthTarget = EntityAerwhaleKing.this.getDistanceSq(new BlockPos(this.dungeonX, this.dungeonY + 12, this.dungeonZ + 14));

		if (!this.getStunned() && !this.isCharging())
		{
			if (!this.courseFlipped)
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
				}
				else if (distanceToSecondTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX - 16, this.dungeonY + 12, this.dungeonZ, flySpeed);
				}
				else if (distanceToThirdTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX, this.dungeonY + 12, this.dungeonZ - 16, flySpeed);
				}
				else if (distanceToFourthTarget <= 10)
				{
					this.getMoveHelper().setMoveTo(this.dungeonX + 16, this.dungeonY + 12, this.dungeonZ, flySpeed);
				}
			}
		}
	}

	@Override
	public boolean canDespawn()
	{
		return false;
	}

	@Override
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
		if (this.world.isRemote)
			return;

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

			if (!this.getPlayerList().isEmpty())
			{
				EntityPlayer randomPlayer = this.getPlayerList().get(rand.nextInt(this.getPlayerList().size()));
				if (randomPlayer != null)
					this.setAttackTarget(randomPlayer);
			}

			this.stunTime = 60;
			this.courseFlipped = rand.nextBoolean();
		}
	}

	public List<EntityPlayer> getPlayerList()
	{
		AxisAlignedBB radiusCheck = this.world.isRemote ? this.getEntityBoundingBox().grow(20.0D, 12.0D, 20.0D) : new AxisAlignedBB(new BlockPos(this.dungeonX, this.posY, this.dungeonZ)).grow(15, 12, 15);
		List<EntityPlayer> playerList = this.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, radiusCheck);

		return playerList;
	}

	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte id)
	{
		if (id == 4)
		{
			this.setStunned(true);
		}
		else
		{
			super.handleStatusUpdate(id);
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
		return LostSounds.ENTITY_AERWHALE_KING_IDLE;
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

	public static void setDoor(EntityAerwhaleKing aerwhale)
	{
		int x, y, z;

		for (x = aerwhale.dungeonX - 1; x < aerwhale.dungeonX + 2; ++x)
		{
			for (y = aerwhale.dungeonY - 1; y < aerwhale.dungeonY; ++y)
			{
				for (z = aerwhale.dungeonZ + 1; z < aerwhale.dungeonZ + 6; ++z)
				{
					BlockPos newPos = new BlockPos(x, y, z);
					aerwhale.world.setBlockState(newPos, BlocksLostAether.locked_gale_stone.getDefaultState());
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
				for (z = this.dungeonZ + 1; z < this.dungeonZ + 5; ++z) // 6
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
				for (y = this.dungeonY - 32; y < this.dungeonY + 32; ++y)
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

		public AIMoveControl(EntityAerwhaleKing whaleBoss)
		{
			super(whaleBoss);
		}

		public void onUpdateMoveHelper()
		{
			if (this.action == EntityMoveHelper.Action.MOVE_TO)
			{
				double d0 = this.posX - EntityAerwhaleKing.this.posX;
				double d1 = this.posY - EntityAerwhaleKing.this.posY;
				double d2 = this.posZ - EntityAerwhaleKing.this.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				d3 = (double) MathHelper.sqrt(d3);
				if (d3 < EntityAerwhaleKing.this.getEntityBoundingBox().getAverageEdgeLength())
				{
					this.action = EntityMoveHelper.Action.WAIT;
					EntityAerwhaleKing.this.motionX *= 0.5D;
					EntityAerwhaleKing.this.motionY *= 0.5D;
					EntityAerwhaleKing.this.motionZ *= 0.5D;
				}
				else
				{
					EntityAerwhaleKing.this.motionX += d0 / d3 * 0.05D * this.speed;
					EntityAerwhaleKing.this.motionY += d1 / d3 * 0.05D * this.speed;
					EntityAerwhaleKing.this.motionZ += d2 / d3 * 0.05D * this.speed;
					if (!EntityAerwhaleKing.this.isCharging())
					{
						EntityAerwhaleKing.this.rotationYaw = -((float) MathHelper.atan2(EntityAerwhaleKing.this.motionX, EntityAerwhaleKing.this.motionZ)) * (180F / (float) Math.PI);
						EntityAerwhaleKing.this.renderYawOffset = EntityAerwhaleKing.this.rotationYaw;
					}
				}
			}
			if (!EntityAerwhaleKing.this.getStunned() && EntityAerwhaleKing.this.isCharging())
			{
				EntityAerwhaleKing.this.getLookHelper().setLookPosition(EntityAerwhaleKing.this.targetX, EntityAerwhaleKing.this.targetY, EntityAerwhaleKing.this.targetZ, EntityAerwhaleKing.this.getHorizontalFaceSpeed() * 4, EntityAerwhaleKing.this.getVerticalFaceSpeed() * 4);
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
			return EntityAerwhaleKing.this.getStunned();
		}
	}
}