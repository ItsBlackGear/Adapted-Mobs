package com.cf28.adaptedmobs.client.level.model.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jetbrains.annotations.NotNull;

public class SupportCreeperSkullModel extends SkullModelBase {
    protected final ModelPart head;
    private final ModelPart root;

    public SupportCreeperSkullModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
    }

    public static LayerDefinition createMobHeadLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
                        .texOffs(32, 48)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                        .texOffs(0, 58)
                        .addBox(-2.0F, -10.0F, -2.0F, 4.0F, 2.0F, 4.0F)
                        .texOffs(32, 0)
                        .addBox(-4.0F, -5.0F, -9.0F, 8.0F, 5.0F, 5.0F),
                PartPose.ZERO
        );

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(float f, float g, float h) {
        this.head.yRot = g * (float) (Math.PI / 180.0);
        this.head.xRot = h * (float) (Math.PI / 180.0);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
