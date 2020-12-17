package com.legacy.lost_aether.client.render.layer;

import com.legacy.lost_aether.LostContentMod;
import com.legacy.lost_aether.client.models.AerwhaleKingModel;
import com.legacy.lost_aether.entity.AerwhaleKingEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AerwhaleKingGlowLayer<T extends AerwhaleKingEntity, M extends AerwhaleKingModel<T>> extends AbstractEyesLayer<T, M>
{
	private static final ResourceLocation TEXTURE = LostContentMod.locate("textures/entity/aerwhale_king_glow.png");
	private static final RenderType RENDER_TYPE = RenderType.getEyes(TEXTURE);

	public AerwhaleKingGlowLayer(IEntityRenderer<T, M> rendererIn)
	{
		super(rendererIn);
	}

	public RenderType getRenderType()
	{
		return RENDER_TYPE;
	}
}