package com.cf28.adaptedmobs.client.level.model.mob;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class AbandonedModel<T extends AbstractSkeleton> extends SkeletonModel<T> {
    public AbandonedModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 2).addBox(1.0F, -9.0F, -2.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -2.0F, 0.0F));

        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

        root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(18, 18).addBox(-4.0F, -12.0F, -1.0F, 8.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 32).addBox(-4.0F, -12.0F, -1.0F, 8.0F, 12.0F, 2.0F, new CubeDeformation(0.2F))
                .texOffs(0, 32).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 10.0F, 0.0F));

        root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(40, 16).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, -1.0F, 0.0F));

        root.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(40, 16).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, -1.0F, 0.0F));

        root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.0F, 10.0F, 0.0F));

        root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 14.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, 10.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }
    
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        
        this.head.y -= 2.0F;
        this.body.y += 10.0F;
        this.rightArm.y -= 3.0F;
        this.leftArm.y -= 3.0F;
        this.rightLeg.y -= 2.0F;
        this.leftLeg.y -= 2.0F;
    }
}