package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.layer.CreeperClothLayer;
import com.cf28.adaptedmobs.client.level.layer.EntityPowerLayer;
import com.cf28.adaptedmobs.client.level.model.mob.SimpleCreeperModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SimpleCreeperRenderer extends SwellableCreeperRenderer<TamableCreeper, SimpleCreeperModel<TamableCreeper>> {
    public SimpleCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new SimpleCreeperModel<>(context.bakeLayer(AMModelLayers.CREEPER)), 0.5F);
        this.addLayer(new EntityPowerLayer<>(this, new SimpleCreeperModel<>(context.bakeLayer(AMModelLayers.CREEPER_ARMOR))));
        this.addLayer(new CreeperClothLayer<>(this, new SimpleCreeperModel<>(context.bakeLayer(AMModelLayers.CREEPER_CLOTH))));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TamableCreeper entity) {
        return ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper.png");
    }
}
