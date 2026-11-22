package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.layer.SporalPetalLayer;
import com.cf28.adaptedmobs.client.level.model.mob.SporalModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Sporal;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SporalRenderer extends MobRenderer<Sporal, SporalModel> {
    private static final ResourceLocation TEXTURE = AdaptedMobs.resource("textures/entity/sporal/sporal.png");

    public SporalRenderer(EntityRendererProvider.Context context) {
        super(context, new SporalModel(context.bakeLayer(AMModelLayers.SPORAL)), 0.4F);
        this.addLayer(new SporalPetalLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Sporal entity) {
        return TEXTURE;
    }
}
