package com.cf28.adaptedmobs.client.renderer.model;

import com.blackgear.platform.client.AnimationHelper;
import com.blackgear.platform.client.model.AgeableHierarchicalModel;
import com.cf28.adaptedmobs.client.renderer.animation.EntityTransformations;
import com.cf28.adaptedmobs.client.renderer.animation.RocketCreeperAnimations;
import com.cf28.adaptedmobs.common.entity.creeper.RocketCreeper;
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
public class RocketCreeperModel<T extends RocketCreeper> extends AgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;

    public RocketCreeperModel(ModelPart root) {
        super(0.5F, 24.0F);
        this.root = root;
        this.head = root.getChild("all").getChild("upper").getChild("head");
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
        upper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -11.0F, -4.0F, 8.0F, 11.0F, 8.0F, deformation), PartPose.offset(0.0F, -15.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * (float)(Math.PI / 180F);
        this.head.xRot = headPitch * (float)(Math.PI / 180F);

        AnimationHelper.animateWalk(this, RocketCreeperAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 100.0F);

        AnimationHelper.animate(this, entity.attackAnimationState, RocketCreeperAnimations.ROCKET, ageInTicks);
        AnimationHelper.animate(this, entity.sitDownAnimationState, RocketCreeperAnimations.SIT_DOWN, ageInTicks);
        AnimationHelper.animate(this, entity.sitUpAnimationState, RocketCreeperAnimations.SIT_UP, ageInTicks);

        if (this.young) {
            AnimationHelper.animate(this, entity.babyTransformationState, EntityTransformations.BABY_TRANSFORM, ageInTicks);
        }
    }
}