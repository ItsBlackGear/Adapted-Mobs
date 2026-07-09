package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.common.integrations.tolerablecreepers.SupportCreepieEntity;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class AMCreepieRenderer<T extends Creepie, M extends EntityModel<T>> extends MobRenderer<T, M> {
    private final ResourceLocation texture;

    public AMCreepieRenderer(EntityRendererProvider.Context context, M model, ResourceLocation texture) {
        super(context, model, 0.25F);
        this.texture = texture;
    }

    @Override
    protected void scale(T entity, PoseStack poseStack, float partialTicks) {
        float g = entity.getSwelling(partialTicks) * (30.0F / 15.0F);
        float h = 1.0F + Mth.sin(g * 100.0F) * g * 0.01F;
        g = Mth.clamp(g, 0.0F, 1.0F);
        g *= g * g;
        float i = (1.0F + g * 0.4F) * h;
        float j = (1.0F + g * 0.1F) / h;
        poseStack.scale(i, j, i);
    }

    @Override
    protected float getWhiteOverlayProgress(T entity, float partialTicks) {
        float swell = entity.getSwelling(partialTicks) * (30.0F / 15.0F);
        return (int) (swell * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(swell, 0.5F, 1.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        if (entity instanceof SupportCreepieEntity supportCreepie) {
            return supportCreepie.getVariant().getTexture();
        }
        return this.texture;
    }
}
