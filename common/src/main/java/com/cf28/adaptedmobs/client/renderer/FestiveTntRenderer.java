package com.cf28.adaptedmobs.client.renderer;

import com.cf28.adaptedmobs.common.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.registry.AMBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

@Environment(EnvType.CLIENT)
public class FestiveTntRenderer extends EntityRenderer<PrimedFestiveTnt> {
    private final BlockRenderDispatcher blockRenderer;

    public FestiveTntRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    public void render(PrimedFestiveTnt entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        matrixStack.translate(0.0, 0.5, 0.0);
        int i = entity.getFuse();
        if ((float)i - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)i - partialTicks + 1.0F) / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float g = 1.0F + f * 0.3F;
            matrixStack.scale(g, g, g);
        }

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStack.translate(-0.5, -0.5, 0.5);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        if (entity.isCharged()) {
            TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, Blocks.TNT.defaultBlockState(), matrixStack, buffer, packedLight, i / 5 % 2 == 0);
        } else {
            TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, AMBlocks.FESTIVE_TNT.get().defaultBlockState(), matrixStack, buffer, packedLight, i / 5 % 2 == 0);
        }
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PrimedFestiveTnt entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}