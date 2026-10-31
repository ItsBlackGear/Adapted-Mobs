package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.model.mob.SporalModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Sporal;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class SporalRenderer extends MobRenderer<Sporal, SporalModel> {
    private static final Map<Sporal.Variant, ResourceLocation> TEXTURES = Util.make(new EnumMap<>(Sporal.Variant.class), map -> {
        for (Sporal.Variant variant : Sporal.Variant.values()) {
            map.put(variant, AdaptedMobs.resource("textures/entity/sporal/sporal_" + variant.getSerializedName() + ".png"));
        }
    });

    public SporalRenderer(EntityRendererProvider.Context context) {
        super(context, new SporalModel(context.bakeLayer(AMModelLayers.SPORAL)), 0.4F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Sporal entity) {
        return TEXTURES.get(entity.getVariant());
    }
}
