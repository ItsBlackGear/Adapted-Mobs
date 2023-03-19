package com.cf28.adaptedmobs.client.renderer.model;

import com.cf28.adaptedmobs.client.renderer.animation.FestiveCreeperAnimations;
import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@Environment(EnvType.CLIENT)
public class FestiveCreeperModel<T extends FestiveCreeper> extends AgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head;

    public FestiveCreeperModel(ModelPart root) {
        super(0.5F, 24.0F);
        this.root = root;
        ModelPart upper = root.getChild("upper");
        this.head = upper.getChild("head");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("backleftleg", CubeListBuilder.create().texOffs(0, 40).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 5.0F, 5.0F, deformation), PartPose.offset(-4.5F, 19.0F, 4.5F));
        root.addOrReplaceChild("backrightleg", CubeListBuilder.create().texOffs(31, 35).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 5.0F, 5.0F, deformation), PartPose.offset(4.5F, 19.0F, 4.5F));
        root.addOrReplaceChild("frontrightleg", CubeListBuilder.create().texOffs(31, 35).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 5.0F, 5.0F, deformation), PartPose.offset(4.5F, 19.0F, -4.5F));
        root.addOrReplaceChild("frontleftleg", CubeListBuilder.create().texOffs(0, 40).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 5.0F, 5.0F, deformation), PartPose.offset(-4.5F, 19.0F, -4.5F));
        PartDefinition upper = root.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));
        upper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 24).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, deformation), PartPose.offset(0.0F, -10.0F, 0.0F));
        upper.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -10.0F, -7.0F, 14.0F, 10.0F, 14.0F, deformation), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * (float)(Math.PI / 180.0F);
        this.head.xRot = headPitch * (float)(Math.PI / 180.0F);
        float speed = Math.min((float)entity.getDeltaMovement().lengthSqr() * 70.0F, 8.0F);
        this.animate(entity.walkingAnimationState, FestiveCreeperAnimations.WALK, ageInTicks, speed);
        this.animate(entity.firingAnimationState, FestiveCreeperAnimations.FIRE, ageInTicks);
    }
}