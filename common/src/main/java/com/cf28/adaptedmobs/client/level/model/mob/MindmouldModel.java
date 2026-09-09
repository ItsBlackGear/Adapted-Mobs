package com.cf28.adaptedmobs.client.level.model.mob;

// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;

public class MindmouldModel<T extends Mindmould> extends HierarchicalModel<T> {
    private final ModelPart all;
    private final ModelPart vessel;
    private final ModelPart brain;

    public MindmouldModel(ModelPart root) {
        this.all = root.getChild("all");
        this.vessel = this.all.getChild("vessel");
        this.brain = this.all.getChild("brain");
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
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
//        vessel.render(poseStack, vertexConsumer, packedLight, packedOverlay, packedColor);
    }

    @Override
    public ModelPart root() {
        return all;
    }
}