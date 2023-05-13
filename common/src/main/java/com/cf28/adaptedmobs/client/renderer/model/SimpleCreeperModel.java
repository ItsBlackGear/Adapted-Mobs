package com.cf28.adaptedmobs.client.renderer.model;

import com.cf28.adaptedmobs.client.renderer.animation.CreeperAnimations;
import com.cf28.adaptedmobs.client.renderer.animation.EntityTransformations;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class SimpleCreeperModel<T extends TamableCreeper> extends AgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public SimpleCreeperModel(ModelPart root) {
        super(0.5F, 24.0F);
        this.root = root;
        ModelPart all = root.getChild("all");
        this.head = all.getChild("head");
        this.leftHindLeg = all.getChild("leg1");
        this.rightHindLeg = all.getChild("leg2");
        this.leftFrontLeg = all.getChild("leg3");
        this.rightFrontLeg = all.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition all = root.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation), PartPose.offset(0.0F, -18.0F, 0.0F));
        all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation), PartPose.offset(0.0F, -18.0F, 0.0F));
        all.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offset(-2.0F, -6.0F, 2.0F));
        all.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offset(2.0F, -6.0F, 2.0F));
        all.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offset(-2.0F, -6.0F, -2.0F));
        all.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, deformation), PartPose.offset(2.0F, -6.0F, -2.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        this.head.xRot = headPitch * (float) (Math.PI / 180.0);
        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.animate(entity.babyTransformationState, EntityTransformations.BABY_TRANSFORM, ageInTicks);
        this.animate(entity.sitUpAnimationState, CreeperAnimations.SITUP, ageInTicks);
        this.animate(entity.sitDownAnimationState, CreeperAnimations.SITDOWN, ageInTicks);
    }
}