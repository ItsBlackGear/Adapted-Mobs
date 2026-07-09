package com.cf28.adaptedmobs.client.level.model.mob;

import com.cf28.adaptedmobs.client.level.animation.EntityTransformations;
import com.cf28.adaptedmobs.client.level.animation.FestiveCreeperAnimations;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.FestiveCreeper;
import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FestiveCreeperModel<T extends FestiveCreeper> extends AgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;

    public FestiveCreeperModel(ModelPart root) {
        super(0.5F, 24.0F);
        this.root = root;
        ModelPart all = root.getChild("all");
        this.head = all.getChild("upper").getChild("head");
        this.leftHindLeg = all.getChild("backleftleg");
        this.rightHindLeg = all.getChild("backrightleg");
        this.leftFrontLeg = all.getChild("frontleftleg");
        this.rightFrontLeg = all.getChild("frontrightleg");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition all = root.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.ZERO);
        all.addOrReplaceChild("backleftleg", CubeListBuilder.create().texOffs(0, 50).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 5.0F, 5.0F, deformation), PartPose.offset(-4.5F, 19.0F, 4.5F));
        all.addOrReplaceChild("backrightleg", CubeListBuilder.create().texOffs(21, 50).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 5.0F, 5.0F, deformation), PartPose.offset(4.5F, 19.0F, 4.5F));
        all.addOrReplaceChild("frontrightleg", CubeListBuilder.create().texOffs(21, 40).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 5.0F, 5.0F, deformation), PartPose.offset(4.5F, 19.0F, -4.5F));
        all.addOrReplaceChild("frontleftleg", CubeListBuilder.create().texOffs(0, 40).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 5.0F, 5.0F, deformation), PartPose.offset(-4.5F, 19.0F, -4.5F));
        PartDefinition upper = all.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, deformation), PartPose.offset(0.0F, -10.0F, 0.0F));
        head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-6.0F, -2.0F, 0.0F, 10.0F, 2.0F, 0.0F, deformation), PartPose.offsetAndRotation(1.0F, -8.0F, 4.0F, -0.7854F, 0.0F, 0.0F));
        head.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(28, 0).addBox(-7.0F, -2.0F, 0.0F, 10.0F, 2.0F, 0.0F, deformation), PartPose.offsetAndRotation(2.0F, -8.0F, -4.0F, 0.7854F, 0.0F, 0.0F));
        head.addOrReplaceChild("head_r3", CubeListBuilder.create().texOffs(28, -6).addBox(0.0F, -2.0F, -4.0F, 0.0F, 2.0F, 8.0F, deformation), PartPose.offsetAndRotation(5.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
        head.addOrReplaceChild("head_r4", CubeListBuilder.create().texOffs(28, -6).addBox(0.0F, -2.0F, -3.0F, 0.0F, 2.0F, 8.0F, deformation), PartPose.offsetAndRotation(-5.0F, -8.0F, -1.0F, 0.0F, 0.0F, -0.7854F));
        head.addOrReplaceChild("head_r5", CubeListBuilder.create().texOffs(36, 8).addBox(-2.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F, deformation), PartPose.offsetAndRotation(-5.0F, 0.0F, 4.0F, 0.0F, 0.7854F, 0.0F));
        head.addOrReplaceChild("head_r6", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F, deformation).mirror(false), PartPose.offsetAndRotation(5.0F, 0.0F, 4.0F, 0.0F, -0.7854F, 0.0F));
        head.addOrReplaceChild("head_r7", CubeListBuilder.create().texOffs(36, 8).addBox(-2.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F, deformation), PartPose.offsetAndRotation(-5.0F, 0.0F, -4.0F, 0.0F, -0.7854F, 0.0F));
        head.addOrReplaceChild("head_r8", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F, deformation).mirror(false), PartPose.offsetAndRotation(5.0F, 0.0F, -4.0F, 0.0F, 0.7854F, 0.0F));
        PartDefinition belly = upper.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(0, 16).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 14.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        belly.addOrReplaceChild("belly_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 10.0F, 0.0F, deformation), PartPose.offsetAndRotation(-7.0F, -5.0F, -7.0F, 0.0F, -0.7854F, 0.0F));
        belly.addOrReplaceChild("belly_r2", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, -10.0F, 0.0F, 2.0F, 10.0F, 0.0F, deformation).mirror(false), PartPose.offsetAndRotation(7.0F, 0.0F, 7.0F, 0.0F, -0.7854F, 0.0F));
        belly.addOrReplaceChild("belly_r3", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, -10.0F, 0.0F, 2.0F, 10.0F, 0.0F, deformation).mirror(false), PartPose.offsetAndRotation(7.0F, 0.0F, -7.0F, 0.0F, 0.7854F, 0.0F));
        belly.addOrReplaceChild("belly_r4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -10.0F, 0.0F, 2.0F, 10.0F, 0.0F, deformation), PartPose.offsetAndRotation(-7.0F, 0.0F, 7.0F, 0.0F, 0.7854F, 0.0F));
        belly.addOrReplaceChild("belly_r5", CubeListBuilder.create().texOffs(28, 4).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 2.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, 0.7854F, 0.0F, 0.0F));
        belly.addOrReplaceChild("belly_r6", CubeListBuilder.create().texOffs(28, 6).addBox(-7.0F, -2.0F, 0.0F, 14.0F, 2.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, -10.0F, 7.0F, -0.7854F, 0.0F, 0.0F));
        belly.addOrReplaceChild("belly_r7", CubeListBuilder.create().texOffs(28, 4).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 2.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, -0.7854F, 0.0F, 0.0F));
        belly.addOrReplaceChild("belly_r8", CubeListBuilder.create().texOffs(28, 6).addBox(-7.0F, -2.0F, 0.0F, 14.0F, 2.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, -10.0F, -7.0F, 0.7854F, 0.0F, 0.0F));
        belly.addOrReplaceChild("belly_r9", CubeListBuilder.create().texOffs(28, -8).addBox(0.0F, -2.0F, 0.0F, 0.0F, 2.0F, 14.0F, deformation), PartPose.offsetAndRotation(-7.0F, -10.0F, -7.0F, 0.0F, 0.0F, -0.7854F));
        belly.addOrReplaceChild("belly_r10", CubeListBuilder.create().texOffs(28, -10).addBox(0.0F, 0.0F, -7.0F, 0.0F, 2.0F, 14.0F, deformation), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
        belly.addOrReplaceChild("belly_r11", CubeListBuilder.create().texOffs(28, -10).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 14.0F, deformation), PartPose.offsetAndRotation(7.0F, 0.0F, -7.0F, 0.0F, 0.0F, -0.7854F));
        belly.addOrReplaceChild("belly_r12", CubeListBuilder.create().texOffs(28, -8).addBox(0.0F, -2.0F, -7.0F, 0.0F, 2.0F, 14.0F, deformation), PartPose.offsetAndRotation(7.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;

        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.4F * limbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        this.animate(entity.attackAnimationState, FestiveCreeperAnimations.FIRE, ageInTicks);
        this.animate(entity.sitDownAnimationState, FestiveCreeperAnimations.SIT_DOWN, ageInTicks);
        this.animate(entity.sitUpAnimationState, FestiveCreeperAnimations.SITUP, ageInTicks);

        if (this.young) {
            this.animate(entity.babyTransformationState, EntityTransformations.BABY_TRANSFORM, ageInTicks);
        }
    }
}
