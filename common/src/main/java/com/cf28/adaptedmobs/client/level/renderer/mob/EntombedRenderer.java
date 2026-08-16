package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.layer.EntombedMaskLayer;
import com.cf28.adaptedmobs.client.level.model.mob.EntombedModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EntombedRenderer extends MobRenderer<Entombed, EntombedModel<Entombed>> {
    private static final ResourceLocation ENTOMBED_LOCATION = ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "textures/entity/entombed/entombed.png");

    public EntombedRenderer(EntityRendererProvider.Context context) {
        super(context, new EntombedModel<>(context.bakeLayer(AMModelLayers.ENTOMBED)), 0.5F);
        this.addLayer(new EntombedMaskLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Entombed entity) {
        return ENTOMBED_LOCATION;
    }
}
