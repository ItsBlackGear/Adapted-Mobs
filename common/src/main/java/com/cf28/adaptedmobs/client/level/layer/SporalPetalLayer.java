package com.cf28.adaptedmobs.client.level.layer;

import com.cf28.adaptedmobs.client.level.model.mob.SporalModel;
import com.cf28.adaptedmobs.common.level.entity.mob.Sporal;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.Map;

public class SporalPetalLayer extends RenderLayer<Sporal, SporalModel> {
    private static final Map<Sporal.Variant, ResourceLocation> TEXTURES = Util.make(new EnumMap<>(Sporal.Variant.class), map -> {
        for (Sporal.Variant variant : Sporal.Variant.values()) {
            map.put(variant, AdaptedMobs.resource("textures/entity/sporal/sporal_" + variant.getSerializedName() + "_layer.png"));
        }
    });

    public SporalPetalLayer(RenderLayerParent<Sporal, SporalModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Sporal sporal, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!sporal.isInvisible()) {
            renderColoredCutoutModel(this.getParentModel(), TEXTURES.get(sporal.getVariant()), poseStack, buffer, packedLight, sporal, -1);
        }
    }
}
