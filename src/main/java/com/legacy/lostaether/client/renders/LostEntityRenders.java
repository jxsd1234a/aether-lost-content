package com.legacy.lostaether.client.renders;

import com.legacy.lostaether.entities.EntityKingAerwhale;
import com.legacy.lostaether.entities.EntityZephyroo;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class LostEntityRenders
{

	public static void initialize()
	{
		register(EntityZephyroo.class, ZephyrooRenderer.class);
		register(EntityKingAerwhale.class, KingAerwhaleRenderer.class);
	}

	public static <ENTITY extends Entity> void register(Class<ENTITY> classes, final Class<? extends Render<ENTITY>> render)
	{
		RenderingRegistry.registerEntityRenderingHandler(classes, new IRenderFactory<ENTITY>()
		{

			@Override
			public Render<ENTITY> createRenderFor(RenderManager manager)
			{
				try
				{
					return render.getConstructor(RenderManager.class).newInstance(manager);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				return null;
			}
		});
	}
}