package com.cf28.adaptedmobs.client.renderer.model;

import com.cf28.adaptedmobs.common.entity.errant.Errant;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ErrantModel extends HierarchicalModel<Errant> {
    private final ModelPart root;
    private final ModelPart cloak;

    public ErrantModel(ModelPart root) {
        this.root = root.getChild("Errantwhole");
        this.cloak = this.root.getChild("cloak");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition Errantwhole = root.addOrReplaceChild("Errantwhole", CubeListBuilder.create(), PartPose.offset(-43.0F, -15.0F, 0.0F));

        PartDefinition Body = Errantwhole.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(36, 50).addBox(-5.0F, -17.0F, -2.0F, 10.0F, 17.0F, 5.0F, new CubeDeformation(0.21F)), PartPose.offset(43.0F, 21.0F, 0.0F));

        PartDefinition Arm1 = Body.addOrReplaceChild("Arm1", CubeListBuilder.create().texOffs(59, 68).addBox(-7.0F, -3.0F, -3.0F, 7.0F, 6.0F, 7.0F, new CubeDeformation(-0.01F))
            .texOffs(81, 37).addBox(-5.0F, 3.0F, -2.0F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.5F))
            .texOffs(0, 42).addBox(-9.0F, -5.0F, -4.0F, 9.0F, 8.0F, 9.0F)
            .texOffs(0, 78).addBox(-5.0F, 3.0F, -2.0F, 5.0F, 13.0F, 5.0F), PartPose.offset(-5.0F, -14.0F, 0.0F));

        PartDefinition Arm2 = Body.addOrReplaceChild("Arm2", CubeListBuilder.create().texOffs(63, 24).addBox(0.0F, -3.0F, -3.0F, 7.0F, 6.0F, 7.0F, new CubeDeformation(-0.01F))
            .texOffs(40, 0).addBox(0.0F, 3.0F, -2.0F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.5F))
            .texOffs(31, 33).addBox(0.0F, -5.0F, -4.0F, 9.0F, 8.0F, 9.0F)
            .texOffs(30, 72).addBox(0.0F, 3.0F, -2.0F, 5.0F, 13.0F, 5.0F), PartPose.offset(5.0F, -14.0F, 0.0F));

        PartDefinition Crownedhelm = Body.addOrReplaceChild("Crownedhelm", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 14.0F, 10.0F, new CubeDeformation(-0.01F))
            .texOffs(0, 24).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.3F))
            .texOffs(30, 14).addBox(-5.0F, -18.0F, -5.0F, 10.0F, 7.0F, 10.0F, new CubeDeformation(0.29F)), PartPose.offset(0.0F, -16.0F, 0.0F));

        PartDefinition Leg1 = Errantwhole.addOrReplaceChild("Leg1", CubeListBuilder.create().texOffs(66, 45).addBox(-2.0F, 1.0F, -2.0F, 5.0F, 18.0F, 5.0F), PartPose.offset(40.0F, 20.0F, 0.0F));

        PartDefinition Tasset1 = Leg1.addOrReplaceChild("Tasset1", CubeListBuilder.create(), PartPose.offset(3.0F, 18.0F, 0.0F));

        PartDefinition cube_r1 = Tasset1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(80, 0).addBox(-1.25F, 0.25F, -2.5F, 3.0F, 10.0F, 6.0F), PartPose.offsetAndRotation(-4.0F, -19.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition Leg2 = Errantwhole.addOrReplaceChild("Leg2", CubeListBuilder.create().texOffs(60, 0).addBox(-3.0F, 1.0F, -2.0F, 5.0F, 18.0F, 5.0F), PartPose.offset(46.0F, 20.0F, 0.0F));

        PartDefinition Tasset2 = Leg2.addOrReplaceChild("Tasset2", CubeListBuilder.create(), PartPose.offset(-3.0F, 18.0F, 0.0F));

        PartDefinition cube_r2 = Tasset2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(50, 81).addBox(-1.75F, 0.25F, -2.5F, 3.0F, 10.0F, 6.0F), PartPose.offsetAndRotation(4.0F, -19.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition Cape = Errantwhole.addOrReplaceChild(
            "cloak",
            CubeListBuilder.create(),
//                .texOffs(0, 97)
//                .addBox(-7.0F, 0.0F, -1.0F, 14.0F, 23.0F, 1.0F, new CubeDeformation(-0.02F), 1.0F, 0.5F),
            PartPose.ZERO
        );

        return LayerDefinition.create(mesh, 128, 128);
    }
    
    @Override
    public void setupAnim(Errant entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void renderCloak(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        this.cloak.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}