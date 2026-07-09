package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.layer.CreeperClothLayer;
import com.cf28.adaptedmobs.client.level.layer.SupportCreeperPowerLayer;
import com.cf28.adaptedmobs.client.level.model.mob.PeeperCreeperModel;
import com.cf28.adaptedmobs.client.level.model.mob.SupportCreeperModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.SupportCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SupportCreeperRenderer extends SwellableCreeperRenderer<SupportCreeper, SupportCreeperModel<SupportCreeper>> {
    private static final ResourceLocation SUPPORT_TEXTURE = AdaptedMobs.resource("textures/entity/creeper/support_creeper.png");
    private static final ResourceLocation PEEPER_TEXTURE = AdaptedMobs.resource("textures/entity/creeper/peeper_creeper.png");

    private final Map<SupportCreeper.Variant, Pair<SupportCreeperModel<SupportCreeper>, ResourceLocation>> mapperByVariant;

    public SupportCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new SupportCreeperModel<>(context.bakeLayer(AMModelLayers.SUPPORT_CREEPER)), 0.5F);
        this.addLayer(new SupportCreeperPowerLayer<>(this, context.getModelSet()));
        this.addLayer(new CreeperClothLayer<>(this, new SupportCreeperModel<>(context.bakeLayer(AMModelLayers.SUPPORT_CREEPER_CLOTH))));

        this.mapperByVariant = Util.make(Maps.newHashMap(), map -> {
            map.put(SupportCreeper.Variant.NORMAL, new Pair<>(new SupportCreeperModel<>(context.bakeLayer(AMModelLayers.SUPPORT_CREEPER)), SUPPORT_TEXTURE));
            map.put(SupportCreeper.Variant.PEEPER, new Pair<>(new PeeperCreeperModel<>(context.bakeLayer(AMModelLayers.PEEPER_CREEPER)), PEEPER_TEXTURE));
        });
    }

    @Override
    public void render(SupportCreeper entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.model = this.mapperByVariant.get(entity.getVariant()).getFirst();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SupportCreeper creeper) {
        return this.mapperByVariant.get(creeper.getVariant()).getSecond();
    }
}
