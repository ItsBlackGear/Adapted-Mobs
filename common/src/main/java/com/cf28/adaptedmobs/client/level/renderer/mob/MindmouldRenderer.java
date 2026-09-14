package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.client.level.model.mob.HarpyModel;
import com.cf28.adaptedmobs.client.level.model.mob.MindmouldModel;
import com.cf28.adaptedmobs.client.level.renderer.rendertype.MindmouldRenderType;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class MindmouldRenderer extends MobRenderer<Mindmould, EntityModel<Mindmould>> {
    static final ResourceLocation MINDMOULD_TEXTURE = AdaptedMobs.resource("textures/entity/slime/mindmould.png");
    static final ResourceLocation MINDMOULD_SMALL_TEXTURE = AdaptedMobs.resource("textures/entity/slime/mindmould_small.png");

    private final MindmouldModel<Mindmould> smallModel;
    private final MindmouldModel<Mindmould> defaultModel;

    public MindmouldRenderer(EntityRendererProvider.Context context) {
        super(context, new MindmouldModel<Mindmould>(context.bakeLayer(AMModelLayers.MINDMOULD)), .5f);
        this.smallModel = new MindmouldModel<Mindmould>(context.bakeLayer(AMModelLayers.MINDMOULD_SMALL));
        this.defaultModel = (MindmouldModel<Mindmould>) this.model;
    }

    @Override
    public void render(Mindmould mindmould, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (mindmould.isBaby()) this.model = smallModel;
        else this.model = defaultModel;

        if (!mindmould.getDisguiseBlock().is(Blocks.AIR)) {
            BlockState blockState = mindmould.getDisguiseBlock();

            poseStack.pushPose();
//            float f = ((float) mindmould.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
//            f = Mth.sqrt(f);
//            if (f > 1.0F) f = 1.0F;

//            poseStack.mulPose(Axis.ZP.rotationDegrees(f * this.getFlipDegrees(mindmould)));
//            this.scale(mindmould, poseStack, partialTicks);

            poseStack.translate(-0.5f, 0f, -0.5f);

            BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
            dispatcher.renderBatched(blockState, mindmould.blockPosition(),
                mindmould.level(), poseStack,
                buffer.getBuffer(MindmouldRenderType.mindmould()),
                false, mindmould.getRandom()
            );
            poseStack.popPose();
            return;
        }

        super.render(mindmould, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected @Nullable RenderType getRenderType(@NotNull Mindmould livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (bodyVisible) return RenderType.entityTranslucent(this.getTextureLocation(livingEntity));
        return super.getRenderType(livingEntity, false, translucent, glowing);
    }

    @Override public @NotNull ResourceLocation getTextureLocation(@NotNull Mindmould mindmould) {
        if (mindmould.isBaby()) return MINDMOULD_SMALL_TEXTURE;
        return MINDMOULD_TEXTURE;
    }
}
