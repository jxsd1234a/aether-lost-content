package com.legacy.lostaether.client.renders;

import com.legacy.lostaether.LostAetherContent;
import com.legacy.lostaether.client.models.ModelZephyroo;
import com.legacy.lostaether.entities.EntityZephyroo;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderZephyroo extends RenderLiving<EntityZephyroo>
{
	private static final ResourceLocation TEXTURE = LostAetherContent.locate("textures/entities/zephyroo.png");

	public RenderZephyroo(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelZephyroo(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityZephyroo zephyr)
	{
		return TEXTURE;
	}
}