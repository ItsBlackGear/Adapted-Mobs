package com.cf28.adaptedmobs.client.renderer.model;

import com.blackgear.platform.client.AnimationHelper;
import com.blackgear.platform.client.model.AgeableHierarchicalModel;
import com.cf28.adaptedmobs.client.renderer.animation.EntityTransformations;
import com.cf28.adaptedmobs.client.renderer.animation.SupportCreeperAnimations;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@Environment(EnvType.CLIENT)
public class SupportCreeperModel<T extends SupportCreeper> extends AgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;

    public SupportCreeperModel(ModelPart root) {
        super(0.5F, 24.0F);
        this.root = root;
        this.head = root.getChild("body").getChild("upper").getChild("head");
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
                .addBox(-3.0F, -8.0F, -2.0F, 6.0F, 8.0F, 4.0F, deformation),
            PartPose.offset(0.0F, -9.0F, 0.0F)
        );
        upper.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation),
            PartPose.offset(0.0F, -8.0F, 0.0F)
        );
        return mesh;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * (float)(Math.PI / 180.0F);
        this.head.xRot = headPitch * (float)(Math.PI / 180.0F);

        AnimationHelper.animateWalk(this, SupportCreeperAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 100.0F);

        AnimationHelper.animate(this, entity.attackAnimationState, SupportCreeperAnimations.BESTOW, ageInTicks);
        AnimationHelper.animate(this, entity.sitDownAnimationState, SupportCreeperAnimations.SIT_DOWN, ageInTicks);
        AnimationHelper.animate(this, entity.sitUpAnimationState, SupportCreeperAnimations.SIT_UP, ageInTicks);

        if (this.young) {
            AnimationHelper.animate(this, entity.babyTransformationState, EntityTransformations.BABY_TRANSFORM, ageInTicks);
        }
    }
}