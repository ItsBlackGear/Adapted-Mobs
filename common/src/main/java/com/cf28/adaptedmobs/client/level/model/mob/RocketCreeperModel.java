package com.cf28.adaptedmobs.client.level.model.mob;

import com.cf28.adaptedmobs.client.level.animation.EntityTransformations;
import com.cf28.adaptedmobs.client.level.animation.RocketCreeperAnimations;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.RocketCreeper;
import net.minecraft.client.model.AgeableHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class RocketCreeperModel<T extends RocketCreeper> extends AgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;

    public RocketCreeperModel(ModelPart root) {
        super(0.5F, 24.0F);
        this.root = root;
        ModelPart all = root.getChild("all");
        this.head = all.getChild("upper").getChild("head");
        this.leftHindLeg = all.getChild("leftlegback");
        this.rightHindLeg = all.getChild("rightlegback");
        this.leftFrontLeg = all.getChild("leftlegfront");
        this.rightFrontLeg = all.getChild("rightlegfront");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition all = root.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.ZERO);
        all.addOrReplaceChild("rightlegfront", CubeListBuilder.create().texOffs(17, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation), PartPose.offset(2.0F, 15.0F, -2.0F));
        all.addOrReplaceChild("leftlegfront", CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation), PartPose.offset(-2.0F, 15.0F, -2.0F));
        all.addOrReplaceChild("leftlegback", CubeListBuilder.create().texOffs(17, 51).addBox(-6.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation), PartPose.offset(2.0F, 15.0F, 2.0F));
        all.addOrReplaceChild("rightlegback", CubeListBuilder.create().texOffs(0, 51).addBox(2.0F, 0.0F, -2.0F, 4.0F, 9.0F, 4.0F, deformation), PartPose.offset(-2.0F, 15.0F, 2.0F));
        PartDefinition upper = all.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 0.0F));
        upper.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-3.0F, -15.0F, -2.0F, 6.0F, 15.0F, 4.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition head = upper.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -11.0F, -4.0F, 8.0F, 11.0F, 8.0F, deformation)
                .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation), PartPose.offset(0.0F, -15.0F, 0.0F));
        head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(34, 19).addBox(-1.0F, -7.0F, 0.0F, 12.0F, 19.0F, 0.0F, deformation), PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.0F, -1.2217F, 0.0F));
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

        this.animate(entity.attackAnimationState, RocketCreeperAnimations.ROCKET, ageInTicks);
        this.animate(entity.sitDownAnimationState, RocketCreeperAnimations.SIT_DOWN, ageInTicks);
        this.animate(entity.sitUpAnimationState, RocketCreeperAnimations.SIT_UP, ageInTicks);

        if (this.young) {
            this.animate(entity.babyTransformationState, EntityTransformations.BABY_TRANSFORM, ageInTicks);
        }
    }
}
