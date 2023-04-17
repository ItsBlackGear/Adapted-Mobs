package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.client.AMModelLayers;
import com.cf28.adaptedmobs.client.renderer.layer.FestiveCreeperPowerLayer;
import com.cf28.adaptedmobs.client.renderer.layer.SupportCreeperPowerLayer;
import com.cf28.adaptedmobs.client.renderer.model.FestiveCreeperModel;
import com.cf28.adaptedmobs.client.renderer.model.SupportCreeperModel;
import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class SupportCreeperRenderer extends MobRenderer<SupportCreeper, SupportCreeperModel<SupportCreeper>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdaptedMobs.MOD_ID, "textures/entity/creeper/support.png");

    public SupportCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new SupportCreeperModel<>(context.bakeLayer(AMModelLayers.SUPPORT_CREEPER)), 0.5F);
        this.addLayer(new SupportCreeperPowerLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(SupportCreeper entity) {
        return TEXTURE;
    }
}