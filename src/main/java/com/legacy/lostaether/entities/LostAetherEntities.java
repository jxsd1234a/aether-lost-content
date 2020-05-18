package com.legacy.lostaether.entities;

import com.legacy.aether.world.AetherWorld;
import com.legacy.lostaether.LostAetherContent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class LostAetherEntities
{
	private static int modEntityId;

	public static void initialization()
	{
		register("king_aerwhale", EntityAerwhaleKing.class);
		register("falling_rock", EntityFallingRock.class);
		register("zephyroo", EntityZephyroo.class, 0x88a5c9, 0xf3f868);

		EntityRegistry.addSpawn(EntityZephyroo.class, 10, 2, 2, EnumCreatureType.CREATURE, AetherWorld.aether_biome);
	}

	private static void register(String entityName, Class<? extends Entity> clazz, int primaryEggColor, int secondaryEggColor)
	{
		EntityRegistry.registerModEntity(LostAetherContent.locate(entityName), clazz, entityName, modEntityId, LostAetherContent.instance, 80, 3, false, primaryEggColor, secondaryEggColor);
		modEntityId++;
	}

	private static void register(String entityName, Class<? extends Entity> clazz)
	{
		EntityRegistry.registerModEntity(LostAetherContent.locate(entityName), clazz, entityName, modEntityId, LostAetherContent.instance, 64, 3, false);
		modEntityId++;
	}
}