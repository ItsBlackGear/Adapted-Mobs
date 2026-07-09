package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.layer.CreeperClothLayer;
import com.cf28.adaptedmobs.client.level.layer.EntityPowerLayer;
import com.cf28.adaptedmobs.client.level.model.mob.FestiveCreeperModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FestiveCreeperRenderer extends SwellableCreeperRenderer<FestiveCreeper, FestiveCreeperModel<FestiveCreeper>> {
    public FestiveCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new FestiveCreeperModel<>(context.bakeLayer(AMModelLayers.FESTIVE_CREEPER)), 0.5F);
        this.addLayer(new EntityPowerLayer<>(this, new FestiveCreeperModel<>(context.bakeLayer(AMModelLayers.FESTIVE_CREEPER_ARMOR))));
        this.addLayer(new CreeperClothLayer<>(this, new FestiveCreeperModel<>(context.bakeLayer(AMModelLayers.FESTIVE_CREEPER_CLOTH))));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FestiveCreeper entity) {
        return AdaptedMobs.resource("textures/entity/creeper/festive_creeper.png");
    }
}
