package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.layer.CreeperClothLayer;
import com.cf28.adaptedmobs.client.renderer.layer.EntityPowerLayer;
import com.cf28.adaptedmobs.client.renderer.model.FestiveCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class FestiveCreeperRenderer extends MobRenderer<FestiveCreeper, FestiveCreeperModel<FestiveCreeper>> {
    public FestiveCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new FestiveCreeperModel<>(context.bakeLayer(AMModelLayers.FESTIVE_CREEPER)), 0.5F);
        this.addLayer(new EntityPowerLayer<>(this, new FestiveCreeperModel<>(context.bakeLayer(AMModelLayers.FESTIVE_CREEPER_ARMOR))));
        this.addLayer(new CreeperClothLayer<>(this, new FestiveCreeperModel<>(context.bakeLayer(AMModelLayers.FESTIVE_CREEPER_CLOTH))));
    }

    @Override
    public ResourceLocation getTextureLocation(FestiveCreeper entity) {
        return AdaptedMobs.resource("textures/entity/creeper/festive_creeper.png");
    }
}