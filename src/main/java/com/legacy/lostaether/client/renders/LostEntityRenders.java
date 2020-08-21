package com.legacy.lostaether.client.renders;

import com.legacy.lostaether.entities.EntityAerwhaleKing;
import com.legacy.lostaether.entities.EntityFallingRock;
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
		register(EntityZephyroo.class, RenderZephyroo.class);
		register(EntityAerwhaleKing.class, RenderAerwhaleKing.class);
		register(EntityFallingRock.class, RenderFallingRock.class);
	}

	public static <T extends Entity> void register(Class<T> classes, final Class<? extends Render<T>> render)
	{
		RenderingRegistry.registerEntityRenderingHandler(classes, new IRenderFactory<T>()
		{

			@Override
			public Render<T> createRenderFor(RenderManager manager)
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