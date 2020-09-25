package com.legacy.lost_aether.client.render;

import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.client.models.ZephyrooModel;
import com.legacy.lost_aether.entity.ZephyrooEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ZephyrooRenderer<T extends ZephyrooEntity> extends MobRenderer<T, ZephyrooModel<T>>
{
	private static final ResourceLocation TEXTURE = LostContentMod.locate("textures/entity/zephyroo.png");

	public ZephyrooRenderer(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new ZephyrooModel<>(), 0.5F);
	}

	@Override
	protected void preRenderCallback(T entityIn, MatrixStack matrixStackIn, float partialTicks)
	{
		if (entityIn.isChild())
			matrixStackIn.scale(0.5F, 0.5F, 0.5F);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity)
	{
		return TEXTURE;
	}
}