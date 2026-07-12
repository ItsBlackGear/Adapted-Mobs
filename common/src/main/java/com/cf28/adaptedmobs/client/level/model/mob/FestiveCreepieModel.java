package com.cf28.adaptedmobs.client.level.model.mob;

import com.cf28.adaptedmobs.client.level.model.ModelUtil;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FestiveCreepieModel<T extends Creepie> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart bb_main;

    public FestiveCreepieModel(ModelPart root) {
        this.root = root;
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        bb_main.addOrReplaceChild("cube_r1", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(28, 24), -2.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, true), PartPose.offsetAndRotation(-4.0F, -4.0F, -4.0F, 0.0F, -0.7854F, 0.0F));
        bb_main.addOrReplaceChild("cube_r2", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(24, 24), -2.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, true), PartPose.offsetAndRotation(-4.0F, -4.0F, 4.0F, 0.0F, 0.7854F, 0.0F));
        bb_main.addOrReplaceChild("cube_r3", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(28, 24), 0.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, false), PartPose.offsetAndRotation(4.0F, -4.0F, 4.0F, 0.0F, -0.7854F, 0.0F));
        bb_main.addOrReplaceChild("cube_r4", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(24, 24), 0.0F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, false), PartPose.offsetAndRotation(4.0F, -4.0F, -4.0F, 0.0F, 0.7854F, 0.0F));
        bb_main.addOrReplaceChild("cube_r5", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(0, 16), -4.0F, 0.0F, 0.0F, 8.0F, 2.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.7854F, 0.0F, 0.0F));
        bb_main.addOrReplaceChild("cube_r6", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(0, 16), -4.0F, 0.0F, 0.0F, 8.0F, 2.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.7854F, 0.0F, 0.0F));
        bb_main.addOrReplaceChild("cube_r7", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(0, 8), 0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 8.0F, true), PartPose.offsetAndRotation(-4.0F, 0.0F, -4.0F, 0.0F, 0.0F, 0.7854F));
        bb_main.addOrReplaceChild("cube_r8", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(0, 10), 0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 8.0F, true), PartPose.offsetAndRotation(4.0F, 0.0F, -4.0F, 0.0F, 0.0F, -0.7854F));
        bb_main.addOrReplaceChild("cube_r9", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(0, 16), -4.0F, -2.0F, 0.0F, 8.0F, 2.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, -8.0F, 4.0F, -0.7854F, 0.0F, 0.0F));
        bb_main.addOrReplaceChild("cube_r10", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(0, 16), -4.0F, -2.0F, 0.0F, 8.0F, 2.0F, 0.0F, false), PartPose.offsetAndRotation(0.0F, -8.0F, -4.0F, 0.7854F, 0.0F, 0.0F));
        bb_main.addOrReplaceChild("cube_r11", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(0, 10), 0.0F, -2.0F, -3.0F, 0.0F, 2.0F, 8.0F, false), PartPose.offsetAndRotation(4.0F, -8.0F, -1.0F, 0.0F, 0.0F, 0.7854F));
        bb_main.addOrReplaceChild("cube_r12", ModelUtil.addFringeBox(CubeListBuilder.create().texOffs(0, 8), 0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 8.0F, false), PartPose.offsetAndRotation(-4.0F, -8.0F, -3.0F, 0.0F, 0.0F, -0.7854F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float swell = entity.getSwelling(0.0F);
        float scale = 1.0F + Mth.sin(swell * 100.0F) * swell * 0.01F;
        this.bb_main.xScale = scale;
        this.bb_main.yScale = scale;
        this.bb_main.zScale = scale;
    }
}
