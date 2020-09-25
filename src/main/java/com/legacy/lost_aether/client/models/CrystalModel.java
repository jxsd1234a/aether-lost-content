package com.legacy.lost_aether.client.models;

import com.legacy.lost_aether.entity.FallingRockEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class CrystalModel<T extends FallingRockEntity> extends EntityModel<T>
{
	public ModelRenderer head[];
	public float sinage[];
	private static final float sponge = (180F / 3.141593F);

	public CrystalModel()
	{
		this(0.0F);
	}

	public CrystalModel(float deltaIn)
	{
		this(deltaIn, 0.0F);
	}

	public CrystalModel(float deltaIn, float yOffsetIn)
	{
		sinage = new float[3];
		head = new ModelRenderer[3];
		head[0] = new ModelRenderer(this, 0, 0);
		head[1] = new ModelRenderer(this, 32, 0);
		head[2] = new ModelRenderer(this, 0, 16);

		for (int i = 0; i < 3; i++)
		{
			head[i].addBox(-4F, -4F, -4F, 8, 8, 8, deltaIn);
			head[i].setRotationPoint(0.0F, 0.0F + yOffsetIn, 0.0F);
		}
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		for (int i = 0; i < 3; i++)
		{
			head[i].rotateAngleY = netHeadYaw / 57.29578F;
			head[i].rotateAngleX = headPitch / 57.29578F;
		}
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		matrixStackIn.push();
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(this.sinage[0] * sponge));
		this.head[0].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		matrixStackIn.pop();

		matrixStackIn.push();
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(this.sinage[1] * sponge));
		this.head[1].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		matrixStackIn.pop();

		matrixStackIn.push();
		matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(this.sinage[2] * sponge));
		this.head[2].render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		matrixStackIn.pop();
	}

}