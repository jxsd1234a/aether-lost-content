// Date: 8/19/2014 2:56:18 AM
package com.legacy.lostaether.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAgilityBoots extends ModelBiped
{

	public ModelRenderer boot_R;

	public ModelRenderer boot_L;

	public ModelRenderer wing1_R;

	public ModelRenderer wing2_R;

	public ModelRenderer wing1_L;

	public ModelRenderer wing2_L;

	public ModelAgilityBoots()
	{
		super(0.5F);
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.boot_L = new ModelRenderer(this, 0, 16);
        this.boot_L.setRotationPoint(1.8F, 12.0F, 0.0F);
        this.boot_L.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 1.0F);
        setRotation(boot_L, 0F, 0F, 0F);
        this.wing1_L = new ModelRenderer(this, 12, 16);
        this.wing1_L.mirror = true;
        this.wing1_L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing1_L.addBox(2.0F, 8.0F, -1.0F, 1, 1, 3, 0.5F);
        this.wing1_R = new ModelRenderer(this, 12, 16);
        this.wing1_R.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing1_R.addBox(-3.0F, 8.0F, -1.0F, 1, 1, 3, 0.5F);
        this.wing2_L = new ModelRenderer(this, 12, 16);
        this.wing2_L.mirror = true;
        this.wing2_L.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing2_L.addBox(2.0F, 7.0F, 0.0F, 1, 1, 3, 0.5F);
        this.boot_R = new ModelRenderer(this, 0, 16);
        this.boot_R.setRotationPoint(-1.8F, 12.0F, 0.0F);
        this.boot_R.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 1.0F);
        setRotation(boot_R, 0F, 0F, 0F);
        this.wing2_R = new ModelRenderer(this, 12, 16);
        this.wing2_R.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing2_R.addBox(-3.0F, 7.0F, 0.0F, 1, 1, 3, 0.5F);
        this.boot_L.addChild(this.wing1_L);
        this.boot_R.addChild(this.wing1_R);
        this.wing1_L.addChild(this.wing2_L);
        this.wing1_R.addChild(this.wing2_R);
	    }

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

		this.boot_L.render(scale);
		this.boot_R.render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		this.boot_R.rotateAngleX = this.bipedRightLeg.rotateAngleX;
		this.boot_R.rotateAngleY = this.bipedRightLeg.rotateAngleY;
		this.boot_R.rotateAngleZ = this.bipedRightLeg.rotateAngleZ;
		
		this.boot_L.rotateAngleX = this.bipedLeftLeg.rotateAngleX;
		this.boot_L.rotateAngleY = this.bipedLeftLeg.rotateAngleY;
		this.boot_L.rotateAngleZ = this.bipedLeftLeg.rotateAngleZ;
		
		this.boot_R.rotationPointZ = this.bipedRightLeg.rotationPointZ;
		
		this.boot_L.rotationPointZ = this.bipedLeftLeg.rotationPointZ;
	}
}
