package com.legacy.lost_aether.client.render;

import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.client.models.CrystalModel;
import com.legacy.lost_aether.entity.FallingRockEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class FallingRockRenderer<T extends FallingRockEntity> extends MobRenderer<T, CrystalModel<T>>
{
	private CrystalModel<T> model;
	private static final ResourceLocation TEXTURE = LostContentMod.locate("textures/entity/falling_rock.png");

	public FallingRockRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new CrystalModel<>(), 0.25F);
		this.model = (CrystalModel<T>) this.entityModel;
	}

	@Override
	protected void preRenderCallback(T entityIn, MatrixStack matrixStackIn, float partialTicks)
	{
		matrixStackIn.translate(0, 1.6D, 0);
		matrixStackIn.scale(1.4F, 1.4F, 1.4F);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity)
	{
		return TEXTURE;
	}

}