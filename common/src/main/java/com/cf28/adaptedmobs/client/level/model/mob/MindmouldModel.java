package com.cf28.adaptedmobs.client.level.model.mob;

import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class MindmouldModel<T extends Mindmould> extends HierarchicalModel<T> {
    private final ModelPart all;
    private final ModelPart vessel;
    private final ModelPart.Cube vesselCube;
    private final ModelPart brain;

    public MindmouldModel(ModelPart root) {
        this.all = root.getChild("all");
        this.vessel = this.all.getChild("vessel");
        this.vesselCube = this.vessel.getRandomCube(RandomSource.create());
        this.brain = this.all.getChild("brain");
    }

    public static LayerDefinition createSmallBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.0F));
        PartDefinition vessel = all.addOrReplaceChild("vessel", CubeListBuilder.create().texOffs(0, 0)
            .addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition brain = all.addOrReplaceChild("brain", CubeListBuilder.create().texOffs(0, 16)
            .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition stem_r1 = brain.addOrReplaceChild("stem_r1", CubeListBuilder.create().texOffs(41, 0)
            .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0f, 0.0F));
        PartDefinition vessel = all.addOrReplaceChild("vessel", CubeListBuilder.create().texOffs(0, 0)
            .addBox(-7.0F, 8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 0.0F));
        PartDefinition brain = all.addOrReplaceChild("brain", CubeListBuilder.create().texOffs(0, 32)
            .addBox(-6.0F, 11.0F, -6.0F, 12.0F, 8.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition stem_r1 = brain.addOrReplaceChild("stem_r1", CubeListBuilder.create().texOffs(2, 52)
            .addBox(0.0F, 16.0F, -2.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override public void setupAnim(@NotNull Mindmould entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}
    @Override public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int packedColor) {
        this.renderPart(poseStack, brain, vertexConsumer, packedLight, packedOverlay, packedColor);
    }

    public ModelPart.Cube vesselCube() { return vesselCube; }

    public void transformFacade(PoseStack poseStack) {
        all.translateAndRotate(poseStack);
        vessel.translateAndRotate(poseStack);
        poseStack.translate(vesselCube.maxX / 16.0F, vesselCube.maxY / 16.0F, vesselCube.minZ / 16.0F);
        poseStack.scale(
            (vesselCube.minX - vesselCube.maxX) / 16.0F,
            (vesselCube.minY - vesselCube.maxY) / 16.0F,
            (vesselCube.maxZ - vesselCube.minZ) / 16.0F
        );
    }

    private void renderPart(
        PoseStack poseStack, ModelPart part, VertexConsumer vertexConsumer,
        int packedLight, int packedOverlay, int packedColor
    ) {
        poseStack.pushPose();
        all.translateAndRotate(poseStack);
        part.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
        poseStack.popPose();
    }

    @Override
    public ModelPart root() { return all; }
}
