package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.layer.ErrantCapeLayer;
import com.cf28.adaptedmobs.client.renderer.model.ErrantModel;
import com.cf28.adaptedmobs.common.entity.errant.Errant;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ErrantRenderer extends MobRenderer<Errant, ErrantModel> {
    private static final ResourceLocation ERRANT_LOCATION = new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/errant/errant.png");

    public ErrantRenderer(EntityRendererProvider.Context context) {
        super(context, new ErrantModel(context.bakeLayer(AMModelLayers.ERRANT)), 0.5F);
        this.addLayer(new ErrantCapeLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Errant entity) {
        return ERRANT_LOCATION;
    }
}