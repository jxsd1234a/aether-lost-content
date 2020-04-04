package com.legacy.lostaether.client.renders;

import com.legacy.lostaether.client.models.ModelAerwhaleKing;
import com.legacy.lostaether.entities.EntityAerwhaleKing;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAerwhaleKing extends RenderLiving<EntityAerwhaleKing>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("lost_Aether", "textures/entities/king_aerwhale.png");

	public RenderAerwhaleKing(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelAerwhaleKing(0.0F), 0.5F);
		this.addLayer(new LayerAerwhaleGlow<>(this));
	}

	@Override
	protected void preRenderCallback(EntityAerwhaleKing whaleBoss, float partialTickTime)
	{
		GlStateManager.scale(2.5F, 2.5F, 2.5F);
		GlStateManager.translate(0, -0.7D, 0);

		if (whaleBoss.stunTime > 0)
		{
			GlStateManager.color(1.0F, 1.0F, 0.5F);
		}
	}

	@Override
	protected void applyRotations(EntityAerwhaleKing entityLiving, float ageInTicks, float rotationYaw, float partialTicks)
	{
		if (entityLiving.stunTime > 0)
		{
			rotationYaw += (float) (Math.cos((double) entityLiving.ticksExisted * 3.25D) * Math.PI * 0.25D);
		}

		super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAerwhaleKing whaleBoss)
	{
		return TEXTURE;
	}

}