package com.legacy.lost_aether.client;

import com.legacy.lost_aether.client.render.AerwhaleKingRenderer;
import com.legacy.lost_aether.client.render.FallingRockRenderer;
import com.legacy.lost_aether.client.render.ZephyrooRenderer;
import com.legacy.lost_aether.registry.LostContentEntityTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class LostContentEntityRendering
{
	public static void init()
	{
		register(LostContentEntityTypes.AERWHALE_KING, AerwhaleKingRenderer::new);
		register(LostContentEntityTypes.ZEPHYROO, ZephyrooRenderer::new);
		register(LostContentEntityTypes.FALLING_ROCK, FallingRockRenderer::new);
	}

	private static <T extends Entity> void register(EntityType<T> entityClass, IRenderFactory<? super T> renderFactory)
	{
		RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
	}
}
