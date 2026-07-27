package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.model.mob.HarpyChickModel;
import com.cf28.adaptedmobs.client.level.model.mob.HarpyModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HarpyRenderer extends MobRenderer<Harpy, EntityModel<Harpy>> {
    private static final ResourceLocation HARPY_LOCATION = ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "textures/entity/harpy/harpy.png");
    private static final ResourceLocation HARPY_CHICK_LOCATION = ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "textures/entity/harpy/harpy_chick.png");
    
    private final HarpyModel<Harpy> adultModel;
    private final HarpyChickModel<Harpy> chickModel;

    public HarpyRenderer(EntityRendererProvider.Context context) {
        super(context, new HarpyModel<>(context.bakeLayer(AMModelLayers.HARPY)), 0.5F);
        this.adultModel = (HarpyModel<Harpy>) this.model;
        this.chickModel = new HarpyChickModel<>(context.bakeLayer(AMModelLayers.HARPY_CHICK));
    }

    @Override
    public void render(Harpy harpy, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (harpy.isBaby()) {
            this.model = this.chickModel;
            this.shadowRadius = 0.25F;
        } else {
            this.model = this.adultModel;
            this.shadowRadius = 0.5F;
        }
        super.render(harpy, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Harpy harpy) {
        if (harpy.isBaby()) {
            return HARPY_CHICK_LOCATION;
        } else {
            return HARPY_LOCATION;
        }
    }

    @Override
    public float getBob(Harpy livingBase, float partialTicks) {
        float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
        float f1 = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
        return (Mth.sin(f) + 1.0F) * f1;
    }
}
