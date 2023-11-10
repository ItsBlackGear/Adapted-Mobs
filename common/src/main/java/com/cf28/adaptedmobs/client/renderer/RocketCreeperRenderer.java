package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.layer.CreeperClothLayer;
import com.cf28.adaptedmobs.client.renderer.layer.RocketCreeperPowerLayer;
import com.cf28.adaptedmobs.client.renderer.model.RocketCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.RocketCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class RocketCreeperRenderer extends MobRenderer<RocketCreeper, RocketCreeperModel<RocketCreeper>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/creeper/rocket_creeper.png");

    public RocketCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new RocketCreeperModel<>(context.bakeLayer(AMModelLayers.ROCKET_CREEPER)), 0.5F);
        this.addLayer(new RocketCreeperPowerLayer(this, context.getModelSet()));
        this.addLayer(new CreeperClothLayer<>(this, new RocketCreeperModel<>(context.bakeLayer(AMModelLayers.ROCKET_CREEPER_CLOTH))));
    }

    @Override
    protected void scale(RocketCreeper creeper, PoseStack matrices, float partialTickTime) {
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
    protected float getWhiteOverlayProgress(RocketCreeper creeper, float partialTicks) {
        float swelling = creeper.getSwelling(partialTicks);
        return (int)(swelling * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(swelling, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(RocketCreeper entity) {
        return TEXTURE;
    }
}