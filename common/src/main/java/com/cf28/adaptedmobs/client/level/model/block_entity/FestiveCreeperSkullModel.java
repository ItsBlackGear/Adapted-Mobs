package com.cf28.adaptedmobs.client.level.model.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class FestiveCreeperSkullModel extends SkullModelBase {
    protected final ModelPart head;
    private final ModelPart root;

    public FestiveCreeperSkullModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
    }

    public static LayerDefinition createMobHeadLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F),
                PartPose.ZERO
        );
        head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(28, 0).addBox(-6.0F, -2.0F, 0.0F, 10.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(1.0F, -8.0F, 4.0F, -0.7854F, 0.0F, 0.0F));
        head.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(28, 0).addBox(-7.0F, -2.0F, 0.0F, 10.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(2.0F, -8.0F, -4.0F, 0.7854F, 0.0F, 0.0F));
        head.addOrReplaceChild("head_r3", CubeListBuilder.create().texOffs(28, -6).addBox(0.0F, -2.0F, -4.0F, 0.0F, 2.0F, 8.0F), PartPose.offsetAndRotation(5.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
        head.addOrReplaceChild("head_r4", CubeListBuilder.create().texOffs(28, -6).addBox(0.0F, -2.0F, -3.0F, 0.0F, 2.0F, 8.0F), PartPose.offsetAndRotation(-5.0F, -8.0F, -1.0F, 0.0F, 0.0F, -0.7854F));
        head.addOrReplaceChild("head_r5", CubeListBuilder.create().texOffs(36, 8).addBox(-2.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(-5.0F, 0.0F, 4.0F, 0.0F, 0.7854F, 0.0F));
        head.addOrReplaceChild("head_r6", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F).mirror(false), PartPose.offsetAndRotation(5.0F, 0.0F, 4.0F, 0.0F, -0.7854F, 0.0F));
        head.addOrReplaceChild("head_r7", CubeListBuilder.create().texOffs(36, 8).addBox(-2.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F), PartPose.offsetAndRotation(-5.0F, 0.0F, -4.0F, 0.0F, -0.7854F, 0.0F));
        head.addOrReplaceChild("head_r8", CubeListBuilder.create().texOffs(36, 8).mirror().addBox(0.0F, -8.0F, 0.0F, 2.0F, 8.0F, 0.0F).mirror(false), PartPose.offsetAndRotation(5.0F, 0.0F, -4.0F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(float animation, float yRot, float xRot) {
        this.head.yRot = yRot * (float) (Math.PI / 180.0);
        this.head.xRot = xRot * (float) (Math.PI / 180.0);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
