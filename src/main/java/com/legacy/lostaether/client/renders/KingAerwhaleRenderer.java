package com.legacy.lostaether.client.renders;

import com.legacy.lostaether.client.models.ModelKingAerwhale;
import com.legacy.lostaether.entities.EntityKingAerwhale;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class KingAerwhaleRenderer extends RenderLiving<EntityKingAerwhale> 
{

    private static final ResourceLocation TEXTURE = new ResourceLocation("lost_Aether", "textures/entities/king_aerwhale.png");

	public KingAerwhaleRenderer(RenderManager rendermanagerIn)
	{
		super(rendermanagerIn, new ModelKingAerwhale(), 0.5F);
	}

	protected void preRenderCallback(EntityKingAerwhale whaleBoss, float partialTickTime)
    {
		GlStateManager.scale(2.5F, 2.5F, 2.5F);
		GlStateManager.translate(0, -0.7D, 0);

		
		if (whaleBoss.stunTime > 0)
		{
			GlStateManager.color(1.0F, 1.0F, 0.5F);
			//GlStateManager.rotate(whaleBoss.ticksExisted * 30, 0, 0, 360);
		}
    }
	
	@Override
	protected ResourceLocation getEntityTexture(EntityKingAerwhale whaleBoss) 
	{
		return TEXTURE;
	}

}