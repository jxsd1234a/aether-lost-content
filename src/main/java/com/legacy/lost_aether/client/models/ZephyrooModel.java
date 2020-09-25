package com.legacy.lost_aether.client.models;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class ZephyrooModel<T extends LivingEntity> extends SegmentedModel<T>
{
	ModelRenderer LeftHand;
	ModelRenderer LeftArm;
	ModelRenderer LeftFoot;
	ModelRenderer LeftLeg;
	ModelRenderer LeftHip;
	ModelRenderer LeftShoulder;
	ModelRenderer TailBottom;
	ModelRenderer Pouch;
	ModelRenderer Snout;
	ModelRenderer RightHip;
	ModelRenderer RightLeg;
	ModelRenderer RightFoot;
	ModelRenderer RightShoulder;
	ModelRenderer RightArm;
	ModelRenderer RightHand;
	ModelRenderer TailTop;
	ModelRenderer EarLeft;
	ModelRenderer Neck;
	ModelRenderer EarRight;
	ModelRenderer Head;
	ModelRenderer Body;

	public ZephyrooModel()
	{
		this.textureWidth = 128;
		this.textureHeight = 64;

		this.LeftHand = new ModelRenderer(this, 50, 50);
		this.LeftHand.addBox(0F, 0F, 0F, 2, 1, 4);
		this.LeftHand.setRotationPoint(5F, 10.5F, -10.5F);
		this.LeftHand.setTextureSize(128, 64);
		this.LeftHand.mirror = true;
		this.setRotation(this.LeftHand, -0.3665191F, 0F, 0F);
		this.LeftArm = new ModelRenderer(this, 40, 38);
		this.LeftArm.addBox(0F, 0F, 0F, 2, 9, 2);
		this.LeftArm.setRotationPoint(5F, 3F, -7F);
		this.LeftArm.setTextureSize(128, 64);
		this.LeftArm.mirror = true;
		this.setRotation(this.LeftArm, -0.4363323F, 0F, 0F);
		this.LeftFoot = new ModelRenderer(this, 29, 19);
		this.LeftFoot.addBox(0F, 8F, -7F, 3, 2, 6);
		this.LeftFoot.setRotationPoint(3F, 14F, 1F);
		this.LeftFoot.setTextureSize(128, 64);
		this.LeftFoot.mirror = true;
		this.setRotation(this.LeftFoot, 0F, 0F, 0F);
		this.LeftLeg = new ModelRenderer(this, 0, 0);
		this.LeftLeg.addBox(0F, 0F, 0F, 3, 9, 3);
		this.LeftLeg.setRotationPoint(3F, 14F, 1F);
		this.LeftLeg.setTextureSize(128, 64);
		this.LeftLeg.mirror = true;
		this.setRotation(this.LeftLeg, -0.4363323F, 0F, 0F);
		this.LeftHip = new ModelRenderer(this, 0, 41);
		this.LeftHip.addBox(0F, 0F, 0F, 2, 7, 7);
		this.LeftHip.setRotationPoint(4F, 8F, 0.5F);
		this.LeftHip.setTextureSize(128, 64);
		this.LeftHip.mirror = true;
		this.setRotation(this.LeftHip, 0.0349066F, 0F, 0F);
		this.LeftShoulder = new ModelRenderer(this, 40, 49);
		this.LeftShoulder.addBox(0F, 0F, 0F, 2, 3, 3);
		this.LeftShoulder.setRotationPoint(5F, 2F, -7.5F);
		this.LeftShoulder.setTextureSize(128, 64);
		this.LeftShoulder.mirror = true;
		this.setRotation(this.LeftShoulder, 0.0349066F, 0F, 0F);
		this.TailBottom = new ModelRenderer(this, 44, 25);
		this.TailBottom.addBox(0F, 0F, 0F, 3, 9, 3);
		this.TailBottom.setRotationPoint(-1.5F, 21F, 15F);
		this.TailBottom.setTextureSize(128, 64);
		this.TailBottom.mirror = true;
		this.setRotation(this.TailBottom, 1.32645F, 0F, 0F);
		this.Pouch = new ModelRenderer(this, 13, 0);
		this.Pouch.addBox(0F, 0F, 0F, 9, 2, 8);
		this.Pouch.setRotationPoint(-4.5F, 10F, -4F);
		this.Pouch.setTextureSize(128, 64);
		this.Pouch.mirror = true;
		this.setRotation(this.Pouch, -0.7853982F, 0F, 0F);
		this.Snout = new ModelRenderer(this, 0, 22);
		this.Snout.addBox(0F, 0F, 0F, 4, 4, 9);
		this.Snout.setRotationPoint(-2F, -2F, -14.5F);
		this.Snout.setTextureSize(128, 64);
		this.Snout.mirror = true;
		this.setRotation(this.Snout, 0.3490659F, 0F, 0F);
		this.RightHip = new ModelRenderer(this, 0, 41);
		this.RightHip.addBox(0F, 0F, 0F, 2, 7, 7);
		this.RightHip.setRotationPoint(-6F, 8F, 0.5F);
		this.RightHip.setTextureSize(128, 64);
		this.RightHip.mirror = true;
		this.setRotation(this.RightHip, 0.0349066F, 0F, 0F);
		this.RightLeg = new ModelRenderer(this, 0, 0);
		this.RightLeg.addBox(0F, 0F, 0F, 3, 9, 3);
		this.RightLeg.setRotationPoint(-6F, 14F, 1F);
		this.RightLeg.setTextureSize(128, 64);
		this.RightLeg.mirror = true;
		this.setRotation(this.RightLeg, -0.4363323F, 0F, 0F);
		this.RightFoot = new ModelRenderer(this, 29, 19);
		this.RightFoot.addBox(0F, 8F, -7F, 3, 2, 6);
		this.RightFoot.setRotationPoint(-6F, 14F, 1F);
		this.RightFoot.setTextureSize(128, 64);
		this.RightFoot.mirror = true;
		this.setRotation(this.RightFoot, 0F, 0F, 0F);
		this.RightShoulder = new ModelRenderer(this, 40, 49);
		this.RightShoulder.addBox(0F, 0F, 0F, 2, 3, 3);
		this.RightShoulder.setRotationPoint(-7F, 2F, -7.5F);
		this.RightShoulder.setTextureSize(128, 64);
		this.RightShoulder.mirror = true;
		this.setRotation(this.RightShoulder, 0.0349066F, 0F, 0F);
		this.RightArm = new ModelRenderer(this, 40, 38);
		this.RightArm.addBox(0F, 0F, 0F, 2, 9, 2);
		this.RightArm.setRotationPoint(-7F, 3F, -7F);
		this.RightArm.setTextureSize(128, 64);
		this.RightArm.mirror = true;
		this.setRotation(this.RightArm, -0.4363323F, 0F, 0F);
		this.RightHand = new ModelRenderer(this, 50, 50);
		this.RightHand.addBox(0F, 0F, 0F, 2, 1, 4);
		this.RightHand.setRotationPoint(-7F, 10.5F, -10.5F);
		this.RightHand.setTextureSize(128, 64);
		this.RightHand.mirror = true;
		this.setRotation(this.RightHand, -0.3665191F, 0F, 0F);
		this.TailTop = new ModelRenderer(this, 48, 37);
		this.TailTop.addBox(0F, 0F, 0F, 3, 10, 3);
		this.TailTop.setRotationPoint(-1.5F, 14F, 8F);
		this.TailTop.setTextureSize(128, 64);
		this.TailTop.mirror = true;
		this.setRotation(this.TailTop, 0.8028515F, 0F, 0F);
		this.EarLeft = new ModelRenderer(this, 0, 41);
		this.EarLeft.addBox(0F, 0F, 0F, 1, 5, 2);
		this.EarLeft.setRotationPoint(2F, -10.5F, -10F);
		this.EarLeft.setTextureSize(128, 64);
		this.EarLeft.mirror = true;
		this.setRotation(this.EarLeft, 0F, 0F, 0.2443461F);
		this.Neck = new ModelRenderer(this, 0, 14);
		this.Neck.addBox(0F, 0F, 0F, 4, 3, 5);
		this.Neck.setRotationPoint(-2F, 0F, -9.5F);
		this.Neck.setTextureSize(128, 64);
		this.Neck.mirror = true;
		this.setRotation(this.Neck, 0.6108652F, 0F, 0F);
		this.EarRight = new ModelRenderer(this, 0, 41);
		this.EarRight.addBox(0F, 0F, 0F, 1, 5, 2);
		this.EarRight.setRotationPoint(-3F, -10F, -10F);
		this.EarRight.setTextureSize(128, 64);
		this.EarRight.mirror = true;
		this.setRotation(this.EarRight, 0F, 0F, -0.2617994F);
		this.Head = new ModelRenderer(this, 26, 27);
		this.Head.addBox(0F, 0F, 0F, 4, 3, 5);
		this.Head.setRotationPoint(-2F, -6F, -11F);
		this.Head.setTextureSize(128, 64);
		this.Head.mirror = true;
		this.setRotation(this.Head, 0F, 0F, 0F);
		this.Body = new ModelRenderer(this, 0, 35);
		this.Body.addBox(0F, 0F, 0F, 10, 9, 20);
		this.Body.setRotationPoint(-5F, -2F, -4F);
		this.Body.setTextureSize(128, 64);
		this.Body.mirror = true;
		this.setRotation(this.Body, -0.7853982F, 0F, 0F);
	}

	@Override
	public Iterable<ModelRenderer> getParts()
	{
		return ImmutableList.of(LeftHand, LeftArm, LeftFoot, LeftLeg, LeftHip, LeftShoulder, TailBottom, Pouch, Snout, RightHip, RightLeg, RightFoot, RightShoulder, RightArm, RightHand, TailTop, EarLeft, Neck, EarRight, Head, Body);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(T entity, float f, float f1, float f2, float f3, float f4)
	{
		if (!entity.onGround || entity.moveForward != 0)
		{
			this.RightLeg.rotateAngleX = (float) Math.cos(f2 * 0.50F);
			this.LeftLeg.rotateAngleX = (float) Math.cos(f2 * 0.50F);
			this.RightFoot.rotateAngleX = this.RightLeg.rotateAngleX + 0.25F;
			this.LeftFoot.rotateAngleX = this.RightLeg.rotateAngleX + 0.25F;
		}
		else
		{
			this.RightLeg.rotateAngleX = 0;
			this.LeftLeg.rotateAngleX = 0;
			this.RightFoot.rotateAngleX = this.RightLeg.rotateAngleX + 0.25F;
			this.LeftFoot.rotateAngleX = this.RightLeg.rotateAngleX + 0.25F;
		}
	}
}
