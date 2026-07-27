package com.cf28.adaptedmobs.client.level.model.mob;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class HarpyChickModel<T extends Harpy> extends EntityModel<T> {
    private final ModelPart bone;
    private final ModelPart wing1;
    private final ModelPart wing2;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart bb_main;

    public HarpyChickModel(ModelPart root) {
        this.bone = root.getChild("bone");
        this.wing1 = root.getChild("wing1");
        this.wing2 = root.getChild("wing2");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(24, 26).addBox(0.0F, -5.0F, -3.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
        .texOffs(16, 25).addBox(-1.0F, 1.0F, -6.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
        .texOffs(0, 13).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, -1.0F));

        PartDefinition wing1 = partdefinition.addOrReplaceChild("wing1", CubeListBuilder.create().texOffs(24, 13).addBox(0.0F, 0.0F, -3.0F, 1.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
        .texOffs(28, 6).addBox(0.0F, 7.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 13.0F, 2.0F));

        PartDefinition wing2 = partdefinition.addOrReplaceChild("wing2", CubeListBuilder.create().texOffs(48, 13).mirror().addBox(-1.0F, 0.0F, -3.0F, 1.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
        .texOffs(28, 6).mirror().addBox(-1.0F, 7.0F, -3.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 13.0F, 2.0F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 25).addBox(-0.5F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 20.0F, 2.0F));

        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 25).mirror().addBox(-3.5F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 20.0F, 2.0F));

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -11.0F, -1.0F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bone.xRot = headPitch * ((float)Math.PI / 180F);
        this.bone.yRot = netHeadYaw * ((float)Math.PI / 180F);

        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;

        if (entity.isFlying()) {
            this.wing1.zRot = -0.0873F - ageInTicks;
            this.wing2.zRot = 0.0873F + ageInTicks;
            this.leg1.xRot += 0.5F;
            this.leg2.xRot += 0.5F;
        } else {
            this.wing1.zRot = 0.0F;
            this.wing2.zRot = 0.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        wing1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        wing2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        leg2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
