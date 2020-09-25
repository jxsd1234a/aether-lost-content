package com.legacy.lost_aether.client.models;

import com.google.common.collect.ImmutableList;
import com.legacy.lost_aether.entity.AerwhaleKingEntity;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class AerwhaleKingModel<T extends AerwhaleKingEntity> extends SegmentedModel<T>
{
	public ModelRenderer Head;
	public ModelRenderer BottomPartHead;
	public ModelRenderer LeftFin;
	public ModelRenderer RightFin;
	public ModelRenderer Middlebody;
	public ModelRenderer Crown1;
	public ModelRenderer BottomPartMiddlebody;
	public ModelRenderer BackBody;
	public ModelRenderer MiddleFin;
	public ModelRenderer FrontBody;
	public ModelRenderer BackfinRight;
	public ModelRenderer BackfinLeft;

	public AerwhaleKingModel()
	{
		float scale = 0.0F;

		this.textureWidth = 228;
		this.textureHeight = 200;
		this.Head = new ModelRenderer(this, 0, 0);
		this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.Head.addBox(-12.0F, -9.0F, -14.0F, 24, 18, 28, scale);
		this.BottomPartMiddlebody = new ModelRenderer(this, 0, 98);
		this.BottomPartMiddlebody.setRotationPoint(0.0F, -1.0F, 14.0F);
		this.BottomPartMiddlebody.addBox(-12.0F, 5.0F, -15.0F, 24, 6, 26, scale);
		this.FrontBody = new ModelRenderer(this, 0, 141);
		this.FrontBody.setRotationPoint(0.0F, 0.9F, 0.0F);
		this.FrontBody.addBox(-11.5F, -1.0F, -0.5F, 19, 5, 21, scale);
		this.BottomPartHead = new ModelRenderer(this, 0, 51);
		this.BottomPartHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.BottomPartHead.addBox(-13.0F, 4.0F, -15.0F, 26, 6, 30, scale);
		this.Middlebody = new ModelRenderer(this, 134, 0);
		this.Middlebody.setRotationPoint(0.0F, -1.0F, 14.0F);
		this.Middlebody.addBox(-11.0F, -5.0F, -1.0F, 22, 14, 25, scale);
		this.setRotateAngle(Middlebody, -0.06981317007977318F, 0.0F, 0.0F);
		this.Crown1 = new ModelRenderer(this, 100, 58);
		this.Crown1.setRotationPoint(0.0F, -9.0F, 3.0F);
		this.Crown1.addBox(-4.0F, -5.0F, -4.0F, 8, 5, 8, scale);
		this.RightFin = new ModelRenderer(this, 76, 0);
		this.RightFin.setRotationPoint(-10.0F, 4.0F, 10.0F);
		this.RightFin.addBox(-20.0F, -2.0F, -6.0F, 19, 3, 14, scale);
		this.setRotateAngle(RightFin, -0.148352986419518F, 0.20943951023931953F, 0.0F);
		this.BackBody = new ModelRenderer(this, 149, 96);
		this.BackBody.setRotationPoint(2.0F, 4.4F, 24.8F);
		this.BackBody.addBox(-10.5F, -9.0F, -2.0F, 17, 10, 22, scale);
		this.setRotateAngle(BackBody, -0.10471975511965977F, 0.0F, 0.0F);
		this.MiddleFin = new ModelRenderer(this, 110, 24);
		this.MiddleFin.setRotationPoint(0.0F, -1.0F, 0.0F);
		this.MiddleFin.addBox(-1.0F, -11.0F, 7.0F, 2, 7, 8, scale);
		this.setRotateAngle(MiddleFin, -0.14416419621473162F, 0.0F, 0.0F);
		this.BackfinLeft = new ModelRenderer(this, 152, 53);
		this.BackfinLeft.setRotationPoint(4.0F, -1.0F, 20.0F);
		this.BackfinLeft.addBox(-4.0F, 0.0F, -6.0F, 13, 3, 24, scale);
		this.setRotateAngle(BackfinLeft, -0.10471975511965977F, 0.7330382858376184F, 0.0F);
		this.LeftFin = new ModelRenderer(this, 76, 0);
		this.LeftFin.setRotationPoint(10.0F, 4.0F, 10.0F);
		this.LeftFin.addBox(1.0F, -2.0F, -6.0F, 19, 3, 14, scale);
		this.setRotateAngle(LeftFin, -0.148352986419518F, -0.20943951023931953F, 0.0F);
		this.BackfinRight = new ModelRenderer(this, 150, 53);
		this.BackfinRight.setRotationPoint(-6.0F, -1.0F, 20.0F);
		this.BackfinRight.addBox(-11.0F, 0.0F, -6.0F, 15, 3, 24, scale);
		this.setRotateAngle(BackfinRight, -0.10471975511965977F, -0.7330382858376184F, 0.0F);
		this.Middlebody.addChild(this.BottomPartMiddlebody);
		this.BackBody.addChild(this.FrontBody);
		this.Head.addChild(this.BottomPartHead);
		this.Head.addChild(this.Middlebody);
		this.Head.addChild(this.Crown1);
		this.Head.addChild(this.RightFin);
		this.Middlebody.addChild(this.BackBody);
		this.Middlebody.addChild(this.MiddleFin);
		this.BackBody.addChild(this.BackfinLeft);
		this.Head.addChild(this.LeftFin);
		this.BackBody.addChild(this.BackfinRight);
	}

	@Override
	public Iterable<ModelRenderer> getParts()
	{
		return ImmutableList.of(this.Head);
	}

	/*@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.Head.offsetY = 1.5F;
		this.Head.render(f5);
	}*/

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setRotationAngles(T whaleBoss, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.Head.rotateAngleX = headPitch / 57.29578F;
		this.Head.rotateAngleY = netHeadYaw / 57.29578F;

		float speed = whaleBoss.getStunned() ? 0.4F : 0.1F;
		this.LeftFin.rotateAngleZ = (MathHelper.cos(ageInTicks * speed) * 0.3F);
		this.RightFin.rotateAngleZ = (MathHelper.cos(ageInTicks * speed) * -0.3F);

		this.BackfinLeft.rotateAngleZ = (MathHelper.cos(ageInTicks * 0.05F) * 0.1F);
		this.BackfinRight.rotateAngleZ = (MathHelper.cos(ageInTicks * 0.05F) * -0.1F);

		if (whaleBoss.getMotion().getX() >= 0.8F || whaleBoss.getMotion().getZ() >= 0.8F || whaleBoss.getMotion().getX() <= -0.8F || whaleBoss.getMotion().getZ() <= -0.8F)
		{
			this.LeftFin.rotateAngleY = -0.20943951023931953F - 0.7F;
			this.RightFin.rotateAngleY = 0.20943951023931953F + 0.7F;

			this.BackfinLeft.rotateAngleY = 0.7330382858376184F - 0.2F;
			this.BackfinRight.rotateAngleY = -0.7330382858376184F + 0.2F;
		}
		else
		{
			this.LeftFin.rotateAngleY = -0.20943951023931953F;
			this.RightFin.rotateAngleY = 0.20943951023931953F;

			this.BackfinLeft.rotateAngleY = 0.7330382858376184F;
			this.BackfinRight.rotateAngleY = -0.7330382858376184F;
		}

		float moveAmount = !whaleBoss.getStunned() ? MathHelper.cos(ageInTicks * speed) * 0.20F : 0;
		this.Head.rotateAngleX = moveAmount / 5;
		this.Middlebody.rotateAngleX = moveAmount / 3;
		this.BackBody.rotateAngleX = moveAmount / 2;

		this.BackfinLeft.rotateAngleX = moveAmount / 7;
		this.BackfinRight.rotateAngleX = moveAmount / 7;
	}
}
