package com.legacy.lost_aether.entity;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import com.aether.player.IAetherBoss;
import com.aether.util.AetherSoundEvents;
import com.legacy.lost_aether.entity.util.LostNameGen;
import com.legacy.lost_aether.registry.LostContentBlocks;
import com.legacy.lost_aether.registry.LostContentSounds;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AerwhaleKingEntity extends FlyingEntity implements IAetherBoss
{
	public static final DataParameter<String> WHALE_NAME = EntityDataManager.<String>createKey(AerwhaleKingEntity.class, DataSerializers.STRING);
	public static final DataParameter<Boolean> WHALE_CHARGING = EntityDataManager.<Boolean>createKey(AerwhaleKingEntity.class, DataSerializers.BOOLEAN);

	private int dungeonX, dungeonY, dungeonZ;
	private int targetX, targetY, targetZ;
	public int chatTime, attackDelay, stunTime, randomAttackChance;
	public float velocity;
	public boolean isTargetted, courseFlipped;

	public AerwhaleKingEntity(EntityType<? extends AerwhaleKingEntity> type, World world)
	{
		super(type, world);
		this.moveController = new AerwhaleKingEntity.WhaleMovementController(this);
		/*this.setSize(4F, 4F);
		this.isImmuneToFire = true;*/
		this.experienceValue = 30;
		this.ignoreFrustumCheck = true;
	}

	@Override
	protected void registerAttributes()
	{
		super.registerAttributes();

		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(500.0D);
		this.setHealth(500.0F);
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(0, new AerwhaleKingEntity.DoNothingGoal());
	}

	@Override
	protected void registerData()
	{
		super.registerData();
		this.dataManager.register(WHALE_CHARGING, false);
		this.dataManager.register(WHALE_NAME, String.valueOf(LostNameGen.whaleGen()));
	}

	@Override
	public void move(MoverType type, Vec3d vec)
	{
		if (this.getStunned())
			super.move(type, Vec3d.ZERO);
		else
			super.move(type, vec);
	}

	@Override
	public void writeAdditional(CompoundNBT nbttagcompound)
	{
		super.writeAdditional(nbttagcompound);
		nbttagcompound.putInt("DungeonX", this.dungeonX);
		nbttagcompound.putInt("DungeonY", this.dungeonY);
		nbttagcompound.putInt("DungeonZ", this.dungeonZ);
		nbttagcompound.putString("BossName", this.getBossName());
	}

	public void readAdditional(CompoundNBT nbttagcompound)
	{
		super.readAdditional(nbttagcompound);
		this.dungeonX = nbttagcompound.getInt("DungeonX");
		this.dungeonY = nbttagcompound.getInt("DungeonY");
		this.dungeonZ = nbttagcompound.getInt("DungeonZ");
		this.setBossName(nbttagcompound.getString("BossName"));
	}

	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return entityIn.getBoundingBox();
	}

	@Override
	public void tick()
	{
		super.tick();

		if (this.world.isRemote)
			this.setMotion(this.getMotion());

		if (this.isAIDisabled())
			return;

		AxisAlignedBB radiusCheck = this.world.isRemote ? this.getBoundingBox().grow(20.0D, 12.0D, 20.0D) : new AxisAlignedBB(new BlockPos(this.dungeonX, this.getPosY(), this.dungeonZ)).grow(15, 12, 15);
		List<PlayerEntity> playerList = this.world.<PlayerEntity>getEntitiesWithinAABB(PlayerEntity.class, radiusCheck);

		// TODO: re-add when boss api is done
		// Give all players in the area the boss bar. Mainly for multiplayer.
		/*for (PlayerEntity nearbyPlayers : this.getPlayerList())
		{
			AetherAPI.getInstance().get(nearbyPlayers).setFocusedBoss(this);
		}*/

		if (this.getAttackTarget() != null)
		{
			if (!world.isRemote)
				this.chargeTarget();

			if (!this.world.isRemote && playerList.isEmpty() && (this.getAttackTarget() != null && getDistanceToPos(this.getAttackTarget().getPosition(), new BlockPos(this.dungeonX, this.dungeonY, this.dungeonZ)) >= 20.0F || (this.getAttackTarget().getHealth() <= 0 || !this.getAttackTarget().isAlive()))) //
			{
				this.reset();
				this.unlockDoor();
				this.world.setBlockState(new BlockPos(this.dungeonX, this.dungeonY, this.dungeonZ - 8), LostContentBlocks.songstone.getDefaultState());
			}

			if (!this.getStunned())
			{
				++AerwhaleKingEntity.this.attackDelay;
			}

			if (!playerList.isEmpty() && (!this.getAttackTarget().isAlive() || this.getAttackTarget().getHealth() <= 0))
			{
				PlayerEntity randomPlayer = playerList.get(rand.nextInt(playerList.size()));
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
			if (this.stunTime == 60 && !this.world.isRemote)
			{
				this.spawnExplosionParticle();
				this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 2, Explosion.Mode.NONE);

				// TODO: re-add when whirlys are in
				if (!this.world.isRemote)
				{
					/*for (int w = 0; w < 4; ++w)
					{
						BlockPos blockpos = (new BlockPos(this.dungeonX + -6 + this.rand.nextInt(12), this.dungeonY, this.dungeonZ + -6 + this.rand.nextInt(12)));
						EntityWhirlwind whirly = new EntityWhirlwind(this.world);
						whirly.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
						whirly.actionTimer = -Integer.MAX_VALUE;
						whirly.capturedDrops.clear();
						whirly.onInitialSpawn(this.world.getDifficultyForLocation(blockpos), (IEntityLivingData) null);
						this.world.spawnEntity(whirly);
					}*/

					for (PlayerEntity players : this.getPlayerList())
					{
						for (int w = 0; w < 6; ++w)
						{
							BlockPos blockpos = (new BlockPos(players.getPosX() + -7 + this.rand.nextInt(14), players.getPosY() + 10 + this.rand.nextInt(5), players.getPosZ() + -7 + this.rand.nextInt(14)));
							FallingRockEntity projectile = new FallingRockEntity(this.world, this);
							projectile.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);

							this.world.addEntity(projectile);
						}

						BlockPos blockpos = (new BlockPos(players.getPosX(), players.getPosY() + 10 + this.rand.nextInt(5), players.getPosZ()));
						FallingRockEntity projectile = new FallingRockEntity(this.world, this);
						projectile.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);

						this.world.addEntity(projectile);
					}

				}
			}

			for (int i = 0; i < 2; ++i)
			{
				double d0 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.world.addParticle(ParticleTypes.EFFECT, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
			}

			this.attackDelay = 0;
			--this.stunTime;
		}

		if (this.getPosY() > this.dungeonY + 2)
		{
			this.noClip = true;
		}
		else
		{
			this.noClip = false;
		}

		// List<EntityWhirlwind> whirlysWithinBox =
		// this.world.<EntityWhirlwind>getEntitiesWithinAABB(EntityWhirlwind.class,
		// this.getBoundingBox().grow(2.5D, 2.5D, 2.5D));

		// TODO
		/*for (int l = 0; l < whirlysWithinBox.size(); l++)
		{
			EntityWhirlwind whirlys = (EntityWhirlwind) whirlysWithinBox.get(l);
			whirlys.setDead();
		}*/

		if (!this.isAlive())
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
		LivingEntity entitylivingbase = this.getAttackTarget();
		BlockPos destPos = new BlockPos(this.targetX, this.targetY, this.targetZ);
		boolean hitTarget = this.getBoundingBox().intersects(entitylivingbase.getBoundingBox());

		if (!this.world.isRemote && this.attackDelay <= 3)
		{
			randomAttackChance = rand.nextInt(150);
		}
		else if (this.attackDelay == 45 + randomAttackChance)
		{
			this.isTargetted = true;
			this.setTargetLockPos(this.getAttackTarget().getPosX(), this.getAttackTarget().getPosY(), this.getAttackTarget().getPosZ());
		}
		else if (this.attackDelay >= 50 + randomAttackChance && !this.isCharging() && !this.getStunned())
		{
			if (!world.isRemote)
			{
				this.moveController.setMoveTo(destPos.getX(), destPos.getY(), destPos.getZ(), 10.0D);
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

		if (!this.getStunned() && this.getDistanceSq(destPos.getX(), destPos.getY(), destPos.getZ()) <= 5.0F)
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
			AerwhaleKingEntity.this.attackDelay = 0;
			AerwhaleKingEntity.this.isTargetted = false;
			AerwhaleKingEntity.this.setCharging(false);
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
		if (!this.world.isRemote)
			this.setMotion(this.getMotion().mul(0.5D, 0.5D, 0.5D));
	}

	private void sendMessage(PlayerEntity player, ITextComponent s)
	{
		player.sendStatusMessage(s, false);
		/*Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (side.isClient())
		{
			Aether.proxy.sendMessage(player, s);
		}*/
	}

	@Override
	public boolean attackEntityFrom(DamageSource ds, float var2)
	{
		if ((ds.isProjectile() || ds.isProjectile()) && ds.getTrueSource() instanceof PlayerEntity)
		{
			this.sendMessage((PlayerEntity) ds.getTrueSource(), new TranslationTextComponent("gui.lost_aether.projectile_miss"));
			this.spawnExplosionParticle();
			return false;
		}

		if (ds.getImmediateSource() == null || !(ds.getImmediateSource() instanceof PlayerEntity))
		{
			return false;
		}

		PlayerEntity player = (PlayerEntity) ds.getImmediateSource();
		ItemStack stack = player.inventory.getCurrentItem();

		if (stack.getItem() == Items.STICK && player.isCreative()) // TODO
		{
			this.reset();
			return false;
		}
		/*else if (!this.getStunned())
		{
			return false;
		}
		*/
		// AetherAPI.getInstance().get(player).setFocusedBoss(this);
		this.setAttackTarget(player);

		return super.attackEntityFrom(ds, Math.max(0, Math.min(var2, 20)));
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean flag = super.attackEntityAsMob(entityIn);

		if (entityIn != null)
		{
			if (this.attackDelay > 10 && entityIn instanceof LivingEntity && !entityIn.isInvulnerable())
			{
				if (entityIn instanceof PlayerEntity)
				{
					PlayerEntity playerentity = (PlayerEntity) entityIn;
					ItemStack playerItem = playerentity.isHandActive() ? playerentity.getActiveItemStack() : ItemStack.EMPTY;

					if (!playerItem.isEmpty() && (playerItem.isShield(playerentity)))
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
						LivingEntity collidedEntity = (LivingEntity) entityIn;
						collidedEntity.addVelocity(collidedEntity.getMotion().getY() * 2, 0.35D, collidedEntity.getMotion().getZ() * 2);
						this.world.playSound(null, getPosX(), getPosY(), getPosZ(), AetherSoundEvents.ENTITY_ZEPHYR_SHOOT, SoundCategory.HOSTILE, 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
						return true;
					}
				}
				else
				{
					entityIn.attackEntityFrom(DamageSource.causeMobDamage(this).setDifficultyScaled(), 15.0F);
					LivingEntity collidedEntity = (LivingEntity) entityIn;
					collidedEntity.addVelocity(collidedEntity.getMotion().getY() * 2, 0.35D, collidedEntity.getMotion().getZ() * 2);
					this.world.playSound(null, getPosX(), getPosY(), getPosZ(), AetherSoundEvents.ENTITY_ZEPHYR_SHOOT, SoundCategory.HOSTILE, 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
					return true;
				}
			}

			return flag;
		}

		return false;
	}

	@Override
	protected void collideWithNearbyEntities()
	{
		List<?> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
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
		double distanceToFirstTarget = this.getDistanceSq(this.dungeonX - 14, this.dungeonY + 12, this.dungeonZ);
		double distanceToSecondTarget = this.getDistanceSq(this.dungeonX, this.dungeonY + 12, this.dungeonZ - 14);
		double distanceToThirdTarget = this.getDistanceSq(this.dungeonX + 14, this.dungeonY + 12, this.dungeonZ);
		double distanceToFourthTarget = this.getDistanceSq(this.dungeonX, this.dungeonY + 12, this.dungeonZ + 14);

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
	public boolean canDespawn(double distance)
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
		return this.isAlive();
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
	@OnlyIn(Dist.CLIENT)
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
				PlayerEntity randomPlayer = this.getPlayerList().get(rand.nextInt(this.getPlayerList().size()));

				if (randomPlayer != null)
					this.setAttackTarget(randomPlayer);

				if (this.getAttackTarget() != null)
				{
					int height = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) this.getPosX(), (int) this.getPosZ());
					this.setPositionAndUpdate(this.getPosX(), height, this.getPosZ());
				}
			}

			this.stunTime = 60;
			this.courseFlipped = rand.nextBoolean();
		}
	}

	public List<PlayerEntity> getPlayerList()
	{
		AxisAlignedBB radiusCheck = this.world.isRemote ? this.getBoundingBox().grow(20.0D, 12.0D, 20.0D) : new AxisAlignedBB(new BlockPos(this.dungeonX, this.getPosY(), this.dungeonZ)).grow(15, 12, 15);
		List<PlayerEntity> playerList = this.world.<PlayerEntity>getEntitiesWithinAABB(PlayerEntity.class, radiusCheck);

		return playerList;
	}

	@OnlyIn(Dist.CLIENT)
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
		return this.getBossName() + ", " + new TranslationTextComponent("title.lost_aether.king_aerwhale.name", new Object[0]).getFormattedText();
	}

	@Override
	public boolean canBeLeashedTo(final PlayerEntity player)
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
		this.playSound(LostContentSounds.ENTITY_AERWHALE_KING_IDLE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F);
		//this.world.playMovingSound(null, this, LostContentSounds.ENTITY_AERWHALE_KING_IDLE, SoundCategory.HOSTILE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F);
		return null; //LostContentSounds.ENTITY_AERWHALE_KING_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return LostContentSounds.ENTITY_AERWHALE_KING_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return LostContentSounds.ENTITY_AERWHALE_KING_DEATH;
	}

	@Override
	protected float getSoundVolume()
	{
		return 3.0F;
	}

	@Override
	protected float getSoundPitch()
	{
		return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn)
	{
		return 1.8F * 0.85F;
	}

	public static void setDoor(AerwhaleKingEntity aerwhale)
	{
		int x, y, z;

		for (x = aerwhale.dungeonX - 1; x < aerwhale.dungeonX + 2; ++x)
		{
			for (y = aerwhale.dungeonY - 1; y < aerwhale.dungeonY; ++y)
			{
				for (z = aerwhale.dungeonZ + 1; z < aerwhale.dungeonZ + 6; ++z)
				{
					BlockPos newPos = new BlockPos(x, y, z);
					aerwhale.world.setBlockState(newPos, LostContentBlocks.locked_gale_stone.getDefaultState());
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
					this.world.destroyBlock(newPos, false);
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
					this.world.setBlockState(newPos, LostContentBlocks.gale_stone_stairs.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH));
				}
			}
		}

		// Unlocks the dungeon blocks
		if (!this.isAlive() || this.getHealth() <= 0)
		{
			for (x = this.dungeonX - 15; x < this.dungeonX + 30; ++x)
			{
				for (y = this.dungeonY - 32; y < this.dungeonY + 32; ++y)
				{
					for (z = this.dungeonZ - 15; z < this.dungeonZ + 30; ++z)
					{
						BlockPos newPos = new BlockPos(x, y, z);
						BlockState unlock_block = this.world.getBlockState(newPos);

						if (unlock_block == LostContentBlocks.locked_gale_stone.getDefaultState())
						{
							this.world.setBlockState(newPos, LostContentBlocks.gale_stone.getDefaultState());
						}
						else if (unlock_block == LostContentBlocks.locked_light_gale_stone.getDefaultState())
						{
							this.world.setBlockState(newPos, LostContentBlocks.light_gale_stone.getDefaultState());
						}
					}
				}
			}
		}
	}

	class WhaleMovementController extends MovementController
	{
		private AerwhaleKingEntity boss = AerwhaleKingEntity.this;

		public WhaleMovementController(AerwhaleKingEntity whaleBoss)
		{
			super(whaleBoss);
		}

		@Override
		public void tick()
		{
			if (this.boss.world.isRemote)
				return;

			if (this.action == Action.MOVE_TO)
			{
				double d0 = this.posX - this.boss.getPosX();
				double d1 = this.posY - this.boss.getPosY();
				double d2 = this.posZ - this.boss.getPosZ();
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				d3 = (double) MathHelper.sqrt(d3);

				if (d3 < this.boss.getBoundingBox().getAverageEdgeLength())
				{
					this.action = Action.WAIT;
					this.boss.setMotion(this.boss.getMotion().mul(0.5D, 0.5D, 0.5D));
				}
				else
				{
					this.boss.setMotion(this.boss.getMotion().add(d0 / d3 * 0.05D * this.speed, d1 / d3 * 0.05D * this.speed, d2 / d3 * 0.05D * this.speed));

					if (!this.boss.isCharging())
					{
						this.boss.rotationYaw = -((float) MathHelper.atan2(this.boss.getMotion().getX(), this.boss.getMotion().getZ())) * (180F / (float) Math.PI);
						this.boss.renderYawOffset = this.boss.rotationYaw;
					}
				}
			}
			if (!this.boss.getStunned() && this.boss.isCharging())
			{
				this.boss.getLookController().setLookPosition(this.boss.targetX, this.boss.targetY, this.boss.targetZ, this.boss.getHorizontalFaceSpeed() * 4, this.boss.getVerticalFaceSpeed() * 4);
			}
		}
	}

	class DoNothingGoal extends Goal
	{
		public DoNothingGoal()
		{
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
			// this.setMutexBits(7);
		}

		@Override
		public boolean shouldExecute()
		{
			return AerwhaleKingEntity.this.getStunned();
		}
	}

	public static float getDistanceToPos(Vec3i startingPos, Vec3i farPos)
	{
		float f = (float) (startingPos.getX() - farPos.getX());
		float f1 = (float) (startingPos.getY() - farPos.getY());
		float f2 = (float) (startingPos.getZ() - farPos.getZ());
		return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
	}
}