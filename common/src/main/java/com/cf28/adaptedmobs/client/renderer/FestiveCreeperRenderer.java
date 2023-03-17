package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.model.FestiveCreeperModel;
import com.cf28.adaptedmobs.common.entity.FestiveCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class FestiveCreeperRenderer extends MobRenderer<FestiveCreeper, FestiveCreeperModel<FestiveCreeper>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/creeper/festive.png");

    public FestiveCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new FestiveCreeperModel<>(context.bakeLayer(AMModelLayers.FESTIVE_CREEPER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(FestiveCreeper entity) {
        return TEXTURE;
    }
}