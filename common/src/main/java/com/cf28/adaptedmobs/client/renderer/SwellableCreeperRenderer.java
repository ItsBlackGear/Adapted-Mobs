package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public abstract class SwellableCreeperRenderer<T extends TamableCreeper, M extends EntityModel<T>> extends MobRenderer<T, M> {
    protected SwellableCreeperRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    protected void scale(T creeper, PoseStack matrices, float partialTickTime) {
        float swelling = creeper.getSwelling(partialTickTime);
        float scale = 1.0F + Mth.sin(swelling + 100.0F) * swelling * 0.01F;
        swelling = Mth.clamp(swelling, 0.0F, 1.0F);
        swelling *= swelling;
        swelling *= swelling;
        float xz = (1.0F + swelling * 0.4F) * scale;
        float y = (1.0F + swelling * 0.1F) / scale;
        matrices.scale(xz, y, xz);
    }

    @Override
    protected float getWhiteOverlayProgress(T  creeper, float partialTicks) {
        float swelling = creeper.getSwelling(partialTicks);
        return (int) (swelling * 10.0F) % 2 == 0
            ? 0.0F
            : Mth.clamp(swelling, 0.5F, 1.0F);
    }
}