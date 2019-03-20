package com.legacy.lostaether.client.renders;

import com.legacy.lostaether.client.models.ModelZephyroo;
import com.legacy.lostaether.entities.EntityZephyroo;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class ZephyrooRenderer extends RenderLiving<EntityZephyroo> 
{

    private static final ResourceLocation TEXTURE = new ResourceLocation("lost_Aether", "textures/entities/zephyroo.png");

	public ZephyrooRenderer(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelZephyroo(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityZephyroo zephyr) 
	{
		return TEXTURE;
	}

}