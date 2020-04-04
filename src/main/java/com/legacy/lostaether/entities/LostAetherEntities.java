package com.legacy.lostaether.entities;

import com.legacy.lostaether.LostAetherContent;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class LostAetherEntities
{
	private static int modEntityId;

	public static void initialization()
	{
		register("king_aerwhale", EntityAerwhaleKing.class);
	}

	@SuppressWarnings("unused")
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