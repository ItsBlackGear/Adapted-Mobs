package com.cf28.adaptedmobs.client.renderer.model;

import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PeeperCreeperModel<T extends SupportCreeper> extends SupportCreeperModel<T> {
    public PeeperCreeperModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition mesh = createBaseCreeperModel(deformation);
        PartDefinition body = mesh.getRoot().getChild("body");
        PartDefinition upper = body.addOrReplaceChild(
            "upper",
            CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-3.0F, -8.0F, -2.0F, 6.0F, 8.0F, 4.0F, deformation)
                .texOffs(32, 5)
                .addBox(0.0F, -6.0F, 2.0F, 0.0F, 6.0F, 5.0F),
            PartPose.offset(0.0F, -9.0F, 0.0F)
        );
        upper.addOrReplaceChild(
            "head",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation)
                .texOffs(32, 0)
                .addBox(-4.0F, -5.0F, -9.0F, 8.0F, 5.0F, 5.0F, deformation),
            PartPose.offset(0.0F, -8.0F, 0.0F)
        );
        return LayerDefinition.create(mesh, 64, 64);
    }
}