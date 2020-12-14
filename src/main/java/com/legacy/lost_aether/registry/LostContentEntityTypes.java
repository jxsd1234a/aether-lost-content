package com.legacy.lost_aether.registry;

import java.util.Random;

import com.aether.block.AetherBlocks;
import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.LostContentRegistry;
import com.legacy.lost_aether.entity.AerwhaleKingEntity;
import com.legacy.lost_aether.entity.FallingRockEntity;
import com.legacy.lost_aether.entity.ZephyrooEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent.Register;

public class LostContentEntityTypes
{
	public static final EntityType<AerwhaleKingEntity> AERWHALE_KING = buildEntity("aerwhale_king", EntityType.Builder.create(AerwhaleKingEntity::new, EntityClassification.MONSTER).setShouldReceiveVelocityUpdates(true).size(4.0F, 4.0F));
	public static final EntityType<ZephyrooEntity> ZEPHYROO = buildEntity("zephyroo", EntityType.Builder.create(ZephyrooEntity::new, EntityClassification.CREATURE).size(0.9F, 1.7F));
	public static final EntityType<FallingRockEntity> FALLING_ROCK = buildEntity("falling_rock", EntityType.Builder.<FallingRockEntity>create(FallingRockEntity::new, EntityClassification.MISC).size(0.9F, 0.9F));

	public static void init(Register<EntityType<?>> event)
	{
		LostContentRegistry.register(event.getRegistry(), "aerwhale_king", AERWHALE_KING);
		LostContentRegistry.register(event.getRegistry(), "zephyroo", ZEPHYROO);
		LostContentRegistry.register(event.getRegistry(), "falling_rock", FALLING_ROCK);

		registerSpawnConditions();

		GlobalEntityTypeAttributes.put(AERWHALE_KING, AerwhaleKingEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(ZEPHYROO, ZephyrooEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(FALLING_ROCK, MobEntity.func_233666_p_().create());
	}

	private static <T extends Entity> EntityType<T> buildEntity(String key, EntityType.Builder<T> builder)
	{
		return builder.build(LostContentMod.find(key));
	}

	public static boolean mobSpawnConditions(EntityType<? extends MobEntity> type, IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn)
	{
		return MobEntity.canSpawnOn(type, worldIn, reason, pos, randomIn);
	}

	private static void registerSpawnConditions()
	{
		EntitySpawnPlacementRegistry.register(LostContentEntityTypes.AERWHALE_KING, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LostContentEntityTypes::mobSpawnConditions);
		EntitySpawnPlacementRegistry.register(LostContentEntityTypes.ZEPHYROO, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, LostContentEntityTypes::canAnimalSpawn);

	}

	public static boolean canAnimalSpawn(EntityType<? extends AnimalEntity> animal, IWorld worldIn, SpawnReason reason, BlockPos pos, Random random)
	{
		return worldIn.getBlockState(pos.down()).getBlock() == AetherBlocks.AETHER_GRASS_BLOCK && worldIn.getLightSubtracted(pos, 0) > 8;
	}
}
