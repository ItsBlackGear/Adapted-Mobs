package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.layer.CreeperClothLayer;
import com.cf28.adaptedmobs.client.renderer.layer.SupportCreeperPowerLayer;
import com.cf28.adaptedmobs.client.renderer.model.SupportCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class SupportCreeperRenderer extends MobRenderer<SupportCreeper, SupportCreeperModel<SupportCreeper>> {
    private static final ResourceLocation SUPPORT_TEXTURE = new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/creeper/support_creeper.png");
    private static final ResourceLocation PEEPER_TEXTURE = new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/creeper/peeper_creeper.png");

    public SupportCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new SupportCreeperModel<>(context.bakeLayer(AMModelLayers.SUPPORT_CREEPER)), 0.5F);
        this.addLayer(new SupportCreeperPowerLayer(this, context.getModelSet()));
        this.addLayer(new CreeperClothLayer<>(this, new SupportCreeperModel<>(context.bakeLayer(AMModelLayers.SUPPORT_CREEPER_CLOTH))));
    }

    @Override
    protected void scale(SupportCreeper creeper, PoseStack matrices, float partialTickTime) {
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
    protected float getWhiteOverlayProgress(SupportCreeper creeper, float partialTicks) {
        float swelling = creeper.getSwelling(partialTicks);
        return (int)(swelling * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(swelling, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(SupportCreeper entity) {
        String string = ChatFormatting.stripFormatting(entity.getName().getString());
        if (string != null &&
                (
                        string.equals("Pee Dog") || string.equals("PeeingDog") || string.equals("Peeper") ||
                        string.equals("pee dog") || string.equals("peeingdog") || string.equals("peeper")
                )
        ) {
            return PEEPER_TEXTURE;
        } else {
            return SUPPORT_TEXTURE;
        }
    }
}