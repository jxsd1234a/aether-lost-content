package com.legacy.lostaether.client.renders;

import com.gildedgames.the_aether.client.models.entities.CrystalModel;
import com.legacy.lostaether.LostAetherContent;
import com.legacy.lostaether.entities.EntityFallingRock;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFallingRock extends RenderLiving<EntityFallingRock>
{
	private CrystalModel model;
	private static final ResourceLocation TEXTURE = LostAetherContent.locate("textures/entities/falling_rock.png");

	public RenderFallingRock(RenderManager renderManager)
	{
		super(renderManager, new CrystalModel(), 0.25F);
		this.model = (CrystalModel) this.mainModel;
	}

	public void preRenderCallback(EntityFallingRock hs, float f)
	{
		for (int i = 0; i < 3; i++)
		{
			model.sinage[i] = hs.sinage[i];
		}

		GlStateManager.translate(0, 0.3D, 0);
		GlStateManager.scale(1.4F, 1.4F, 1.4F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFallingRock entity)
	{
		return TEXTURE;
	}

}