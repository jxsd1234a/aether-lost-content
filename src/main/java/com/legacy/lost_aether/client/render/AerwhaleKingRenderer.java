package com.legacy.lost_aether.client.render;

import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.client.models.AerwhaleKingModel;
import com.legacy.lost_aether.client.render.layer.AerwhaleKingGlowLayer;
import com.legacy.lost_aether.entity.AerwhaleKingEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AerwhaleKingRenderer<T extends AerwhaleKingEntity> extends MobRenderer<T, AerwhaleKingModel<T>>
{
	private static final ResourceLocation TEXTURE = LostContentMod.locate("textures/entity/king_aerwhale.png");

	public AerwhaleKingRenderer(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new AerwhaleKingModel<>(), 1.5F);
		this.addLayer(new AerwhaleKingGlowLayer<>(this));
	}

	@Override
	protected void preRenderCallback(T entityIn, MatrixStack matrixStackIn, float partialTicks)
	{
		matrixStackIn.scale(2.5F, 2.5F, 2.5F);
		matrixStackIn.translate(0, 0.7D, 0);
	}

	@Override
	public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		if (entityIn.getStunned())
			RenderSystem.color3f(1.0F, 1.0F, 0.5F);

		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected void applyRotations(T entityLiving, MatrixStack stack, float ageInTicks, float rotationYaw, float partialTicks)
	{
		if (entityLiving.stunTime > 0)
			rotationYaw += (float) (Math.cos((double) entityLiving.ticksExisted * 3.25D) * Math.PI * 0.25D);

		super.applyRotations(entityLiving, stack, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	public ResourceLocation getEntityTexture(T entity)
	{
		return TEXTURE;
	}
}