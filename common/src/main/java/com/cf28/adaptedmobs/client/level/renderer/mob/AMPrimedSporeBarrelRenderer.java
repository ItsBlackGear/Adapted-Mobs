package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.common.level.entity.AMPrimedSporeBarrel;
import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class AMPrimedSporeBarrelRenderer extends EntityRenderer<AMPrimedSporeBarrel> {
    private final BlockRenderDispatcher blockRenderer;

    public AMPrimedSporeBarrelRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(AMPrimedSporeBarrel entity, float entityYaw, float partialTicks, PoseStack matrices, MultiBufferSource buffer, int packedLight) {
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

        matrices.mulPose(Axis.YP.rotationDegrees(-90.0F));
        matrices.translate(-0.5, -0.5, 0.5);
        matrices.mulPose(Axis.YP.rotationDegrees(90.0F));

        var blockState = switch (entity.getSporeType()) {
            case ROCKET -> AMBlocks.ROCKET_SPORE_BARREL.get().defaultBlockState();
            case FESTIVE -> AMBlocks.FESTIVE_SPORE_BARREL.get().defaultBlockState();
            default -> AMBlocks.SUPPORT_SPORE_BARREL.get().defaultBlockState();
        };
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, blockState, matrices, buffer, packedLight, fuseTicks / 5 % 2 == 0);

        matrices.popPose();
        super.render(entity, entityYaw, partialTicks, matrices, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AMPrimedSporeBarrel entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
