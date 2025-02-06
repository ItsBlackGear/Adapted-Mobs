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

    @Override
    public void render(PrimedFestiveTnt entity, float entityYaw, float partialTicks, PoseStack matrices, MultiBufferSource buffer, int packedLight) {
        matrices.pushPose();
        matrices.translate(0.0, 0.5, 0.0);
        int fuseTicks = entity.getFuse();
        if ((float) fuseTicks - partialTicks + 1.0F < 10.0F) {
            float progress = 1.0F - ((float) fuseTicks - partialTicks + 1.0F) / 10.0F;
            progress = Mth.clamp(progress, 0.0F, 1.0F);
            progress *= progress;
            progress *= progress;
            float scale = 1.0F + progress * 0.3F;
            matrices.scale(scale, scale, scale);
        }

        matrices.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrices.translate(-0.5, -0.5, 0.5);
        matrices.mulPose(Vector3f.YP.rotationDegrees(90.0F));

        if (entity.isCharged()) {
            TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, Blocks.TNT.defaultBlockState(), matrices, buffer, packedLight, fuseTicks / 5 % 2 == 0);
        } else {
            TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, AMBlocks.FESTIVE_TNT.get().defaultBlockState(), matrices, buffer, packedLight, fuseTicks / 5 % 2 == 0);
        }

        matrices.popPose();
        super.render(entity, entityYaw, partialTicks, matrices, buffer, packedLight);
    }

    @Override @SuppressWarnings("deprecation")
    public ResourceLocation getTextureLocation(PrimedFestiveTnt entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}