package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.layer.CreeperClothLayer;
import com.cf28.adaptedmobs.client.renderer.layer.EntityPowerLayer;
import com.cf28.adaptedmobs.client.renderer.model.SimpleCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class SimpleCreeperRenderer extends SwellableCreeperRenderer<TamableCreeper, SimpleCreeperModel<TamableCreeper>> {
    public SimpleCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new SimpleCreeperModel<>(context.bakeLayer(AMModelLayers.CREEPER)), 0.5F);
        this.addLayer(new EntityPowerLayer<>(this, new SimpleCreeperModel<>(context.bakeLayer(AMModelLayers.CREEPER_ARMOR))));
        this.addLayer(new CreeperClothLayer<>(this, new SimpleCreeperModel<>(context.bakeLayer(AMModelLayers.CREEPER_CLOTH))));
    }

    @Override
    public ResourceLocation getTextureLocation(TamableCreeper entity) {
        return new ResourceLocation("textures/entity/creeper/creeper.png");
    }
}