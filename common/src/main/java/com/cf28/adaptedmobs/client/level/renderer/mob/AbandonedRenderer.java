package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.layer.AbandonedBackItemLayer;
import com.cf28.adaptedmobs.client.level.model.mob.AbandonedModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Abandoned;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AbandonedRenderer extends HumanoidMobRenderer<Abandoned, AbandonedModel<Abandoned>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "textures/entity/skeleton/abandoned.png");

    public AbandonedRenderer(EntityRendererProvider.Context context) {
        super(context, new AbandonedModel<>(context.bakeLayer(AMModelLayers.ABANDONED)), 0.5F);
        this.addLayer(new AbandonedBackItemLayer(this, context.getItemRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(Abandoned entity) {
        return TEXTURE;
    }
}