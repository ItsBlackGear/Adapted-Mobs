package com.cf28.adaptedmobs.client.level.model.mob;

import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class EntombedModel<T extends Entombed> extends EntityModel<T> {
    public final ModelPart root;
    public final ModelPart left_leg;
    public final ModelPart right_leg;
    public final ModelPart chest;
    public final ModelPart left_arm;
    public final ModelPart right_arm;
    public final ModelPart head;

    public EntombedModel(ModelPart root) {
        this.root = root;
        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
        this.chest = root.getChild("chest");
        this.left_arm = this.chest.getChild("left_arm");
        this.right_arm = this.chest.getChild("right_arm");
        this.head = this.chest.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
                        .texOffs(40, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.25F))
                        .texOffs(32, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(2.0F, 10.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
                        .texOffs(32, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 14.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(-2.0F, 10.0F, 0.0F));

        PartDefinition chest = partdefinition.addOrReplaceChild("chest", CubeListBuilder.create()
                        .texOffs(0, 32).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(32, 0).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(0.0F, 10.0F, 0.0F));

        PartDefinition left_arm = chest.addOrReplaceChild("left_arm", CubeListBuilder.create()
                        .texOffs(48, 16).mirror().addBox(0.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                        .texOffs(44, 52).addBox(0.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(4.0F, -10.0F, 0.0F));

        PartDefinition right_arm = chest.addOrReplaceChild("right_arm", CubeListBuilder.create()
                        .texOffs(16, 52).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(30, 52).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.25F)),
                PartPose.offset(-4.0F, -10.0F, 0.0F));

        PartDefinition head = chest.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);

        this.left_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;
        this.right_leg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.2F * limbSwingAmount;

        if (entity.isStalking()) {
            this.right_arm.xRot = -0.35F + Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.3F * limbSwingAmount;
            this.left_arm.xRot = -0.35F + Mth.cos(limbSwing * 0.6662F) * 0.3F * limbSwingAmount;
            this.right_arm.zRot = 0.1F;
            this.left_arm.zRot = -0.1F;
        } else {
            float armRaise = -((float) Math.PI / 2.2F);
            this.right_arm.xRot = armRaise + this.head.xRot * 0.5F + Mth.sin(ageInTicks * 0.067F) * 0.05F;
            this.left_arm.xRot = armRaise + this.head.xRot * 0.5F - Mth.sin(ageInTicks * 0.067F) * 0.05F;
            this.right_arm.zRot = 0.05F + Mth.cos(ageInTicks * 0.09F) * 0.05F;
            this.left_arm.zRot = -0.05F - Mth.cos(ageInTicks * 0.09F) * 0.05F;
        }

        if (this.attackTime > 0.0F) {
            float progress = this.attackTime;
            this.chest.yRot = Mth.sin(Mth.sqrt(progress) * ((float) Math.PI * 2F)) * 0.2F;
            this.right_arm.xRot -= Mth.sin(progress * (float) Math.PI) * 0.7F;
            this.left_arm.xRot -= Mth.sin(progress * (float) Math.PI) * 0.7F;
        } else {
            this.chest.yRot = 0.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        chest.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
