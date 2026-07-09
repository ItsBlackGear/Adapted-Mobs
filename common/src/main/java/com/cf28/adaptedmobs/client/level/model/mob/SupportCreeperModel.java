package com.cf28.adaptedmobs.client.level.model.mob;

import com.cf28.adaptedmobs.client.level.animation.EntityTransformations;
import com.cf28.adaptedmobs.client.level.animation.SupportCreeperAnimations;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.SupportCreeper;
import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class SupportCreeperModel<T extends SupportCreeper> extends AgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;

    public SupportCreeperModel(ModelPart root) {
        super(0.5F, 24.0F);
        this.root = root;
        ModelPart body = root.getChild("body");
        this.head = body.getChild("upper").getChild("head");
        this.leftHindLeg = body.getChild("leg0");
        this.rightHindLeg = body.getChild("leg1");
        this.leftFrontLeg = body.getChild("leg2");
        this.rightFrontLeg = body.getChild("leg3");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        return LayerDefinition.create(createBaseCreeperModel(deformation), 64, 64);
    }

    protected static MeshDefinition createBaseCreeperModel(CubeDeformation deformation) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        body.addOrReplaceChild(
                "leg0",
                CubeListBuilder.create()
                        .texOffs(0, 41)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation),
                PartPose.offset(-2.0F, -9.0F, 2.0F)
        );
        body.addOrReplaceChild(
                "leg1",
                CubeListBuilder.create()
                        .texOffs(17, 41)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation),
                PartPose.offset(2.0F, -9.0F, 2.0F)
        );
        body.addOrReplaceChild(
                "leg2",
                CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation),
                PartPose.offset(-2.0F, -9.0F, -2.0F)
        );
        body.addOrReplaceChild(
                "leg3",
                CubeListBuilder.create()
                        .texOffs(17, 28)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation),
                PartPose.offset(2.0F, -9.0F, -2.0F)
        );
        PartDefinition upper = body.addOrReplaceChild(
                "upper",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-3.0F, -8.0F, -2.0F, 6.0F, 8.0F, 4.0F, deformation)
                        .texOffs(32, 5)
                        .addBox(0.0F, -6.0F, 2.0F, 0.0F, 6.0F, 5.0F, deformation),
                PartPose.offset(0.0F, -9.0F, 0.0F)
        );
        upper.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation)
                        .texOffs(32, 48)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                        .texOffs(0, 58)
                        .addBox(-2.0F, -10.0F, -2.0F, 4.0F, 2.0F, 4.0F, deformation)
                        .texOffs(32, 0)
                        .addBox(-4.0F, -5.0F, -9.0F, 8.0F, 5.0F, 5.0F, deformation),
                PartPose.offset(0.0F, -8.0F, 0.0F)
        );
        return mesh;
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
//        this.head.yRot = netHeadYaw * (float)(Math.PI / 180.0F);
//        this.head.xRot = headPitch * (float)(Math.PI / 180.0F);
//
//        this.animateWalk(SupportCreeperAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 100.0F);

        this.animate(entity.attackAnimationState, SupportCreeperAnimations.BESTOW, ageInTicks);
        this.animate(entity.sitDownAnimationState, SupportCreeperAnimations.SIT_DOWN, ageInTicks);
        this.animate(entity.sitUpAnimationState, SupportCreeperAnimations.SIT_UP, ageInTicks);

        if (this.young) {
            this.animate(entity.babyTransformationState, EntityTransformations.BABY_TRANSFORM, ageInTicks);
        }
    }
}
