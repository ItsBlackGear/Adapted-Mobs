package com.cf28.adaptedmobs.client.level.model.mob;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class HarpyModel<T extends Harpy> extends EntityModel<T> {
    private static final float TUCKED_LEG = -0.6F;
    private static final float TALON_LEG = -1.2F;
    private static final float KICK_SPEED = 0.6F;
    private static final float KICK_AMOUNT = 0.5F;
    private static final float SITTING_LEG = -0.9F;
    private static final float SITTING_DROP = 3.0F;

    private final ModelPart head;
    private final ModelPart leg_right;
    private final ModelPart wing_left;
    private final ModelPart wing_right;
    private final ModelPart tail;
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart leg_left;

    public HarpyModel(ModelPart root) {
        this.head = root.getChild("head");
        this.leg_right = root.getChild("leg_right");
        this.wing_left = root.getChild("wing_left");
        this.wing_right = root.getChild("wing_right");
        this.tail = root.getChild("tail");
        this.root = root.getChild("root");
        this.body = root.getChild("body");
        this.leg_left = root.getChild("leg_left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(40, 1).addBox(0.0F, -11.0F, -8.0F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 11).addBox(-1.0F, -1.0F, -10.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, -4.0F));

        PartDefinition leg_right = partdefinition.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(0, 49).addBox(-3.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 18.0F, -1.0F));

        PartDefinition leg_right_overlay = leg_right.addOrReplaceChild("leg_right_overlay", CubeListBuilder.create().texOffs(0, 38).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition wing_left = partdefinition.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(44, 38).addBox(0.0F, 0.0F, -4.0F, 1.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 5.0F, -1.0F));

        PartDefinition wing_right = partdefinition.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(44, 16).addBox(-1.0F, 0.0F, -1.0F, 1.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 5.0F, -4.0F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -0.5F, 0.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 21).addBox(-4.0F, 7.5F, 0.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.5F, 3.0F));

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -6.0F, -3.0F, 10.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 10.0F, -2.0F));

        PartDefinition leg_left = partdefinition.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(16, 49).addBox(-1.0F, 0.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 18.0F, -1.0F));

        PartDefinition leg_left_overlay = leg_left.addOrReplaceChild("leg_left_overlay", CubeListBuilder.create().texOffs(20, 38).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 96, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);

        float time = entity.tickCount;
        float bodyDrop = entity.isInSittingPose() ? SITTING_DROP : 0.0F;
        this.body.y = 10.0F + bodyDrop;
        this.head.y = 7.0F + bodyDrop;
        this.tail.y = 17.5F + bodyDrop;
        this.wing_left.y = 5.0F + bodyDrop;
        this.wing_right.y = 5.0F + bodyDrop;

        if (entity.isPerched()) {
            this.leg_left.xRot = SITTING_LEG;
            this.leg_right.xRot = SITTING_LEG;
            this.wing_left.zRot = 0.0F;
            this.wing_right.zRot = 0.0F;
            return;
        }

        if (entity.isPreparingSwoop()) {
            this.leg_left.xRot = TUCKED_LEG + Mth.cos(time * KICK_SPEED) * KICK_AMOUNT;
            this.leg_right.xRot = TUCKED_LEG + Mth.cos(time * KICK_SPEED + (float) Math.PI) * KICK_AMOUNT;
        } else if (entity.isSwooping()) {
            this.leg_left.xRot = TALON_LEG;
            this.leg_right.xRot = TALON_LEG;
        } else if (entity.isFlying()) {
            float sway = Mth.cos(time * 0.1F) * 0.05F;
            this.leg_left.xRot = TUCKED_LEG + sway;
            this.leg_right.xRot = TUCKED_LEG - sway;
        } else {
            this.leg_left.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.leg_right.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        }

        if (entity.isFlying()) {
            this.wing_left.zRot = -0.0873F - ageInTicks;
            this.wing_right.zRot = 0.0873F + ageInTicks;
        } else {
            this.wing_left.zRot = 0.0F;
            this.wing_right.zRot = 0.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        wing_left.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        wing_right.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        tail.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg_left.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
