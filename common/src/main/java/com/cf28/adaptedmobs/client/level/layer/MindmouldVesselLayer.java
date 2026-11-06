package com.cf28.adaptedmobs.client.level.layer;

import com.cf28.adaptedmobs.client.level.model.mob.MindmouldModel;
import com.cf28.adaptedmobs.client.level.renderer.mob.mindmould.FacadeVertexConsumer;
import com.cf28.adaptedmobs.client.level.renderer.mob.mindmould.MindmouldFacadeModel;
import com.cf28.adaptedmobs.client.level.renderer.mob.mindmould.MindmouldFacadeModel.FacadeCorner;
import com.cf28.adaptedmobs.client.level.renderer.mob.mindmould.MindmouldRenderer;
import com.cf28.adaptedmobs.client.level.renderer.rendertype.MindmouldRenderType;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class MindmouldVesselLayer extends RenderLayer<Mindmould, MindmouldModel<Mindmould>> {
    private final BlockRenderDispatcher blockRenderer;

    public MindmouldVesselLayer(
        RenderLayerParent<Mindmould, MindmouldModel<Mindmould>> renderer,
        BlockRenderDispatcher blockRenderer
    ) {
        super(renderer);
        this.blockRenderer = blockRenderer;
    }

    @Override
    public void render(
        @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer,
        int packedLight, Mindmould mindmould, float limbSwing, float limbSwingAmount,
        float partialTick, float ageInTicks, float netHeadYaw, float headPitch
    ) {
        if (mindmould.isInvisible()) return;

//        Minecraft minecraft = Minecraft.getInstance();
//        Player mainPlayer = minecraft.player;
//        if (mainPlayer != null && mindmould.getVehicle() != null
//        && Objects.equals(mindmould.getAttachedPlayer(), mainPlayer)
//        && minecraft.options.getCameraType().isFirstPerson()) return;

        mindmould.updateClientDisguise(partialTick);
        BlockState blockState = mindmould.getClientBlock();

        boolean baby = mindmould.isBaby();
        MindmouldModel<Mindmould> model = this.getParentModel();
        int packedOverlay = LivingEntityRenderer.getOverlayCoords(mindmould, 0.0F);

        BlockPos pos = mindmould.blockPosition();
        long seed = mindmould.getLootTableSeed(); // blockState.getSeed(pos);
        BakedModel blockModel = blockRenderer.getBlockModel(blockState);
        BakedModel facadeModel = MindmouldFacadeModel.getFacadeModel(blockModel, blockState, seed, null);
        if (baby) facadeModel = MindmouldFacadeModel.getFacadeModel(blockModel, blockState, seed, this.getFacadeCorner(mindmould));

        poseStack.pushPose();

        Player player = mindmould.getAttachedPlayer();
        if (player != null) {
            Matrix4f pose = poseStack.last().pose();
            poseStack.mulPose(Axis.YP.rotation((float) Mth.atan2(pose.m20(), pose.m22())));

            float headYaw = Mth.rotLerp(partialTick, player.yHeadRotO, player.yHeadRot);
            float playerPitch = Mth.lerp(partialTick, player.xRotO, player.getXRot());

            Vec3 playerEye = player.getEyePosition(partialTick).subtract(0, 0.12 * player.getScale(), 0)
                .subtract(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition());
            Vector3f rotationPivot = new Matrix4f(pose).invert().transformPosition(
                (float) playerEye.x, (float) playerEye.y, (float) playerEye.z, new Vector3f()
            );

            poseStack.translate(rotationPivot.x(), rotationPivot.y(), rotationPivot.z());
            poseStack.mulPose(Axis.YP.rotationDegrees(headYaw - 180.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(playerPitch));
            poseStack.translate(-rotationPivot.x(), -rotationPivot.y(), -rotationPivot.z());
        }

        model.transformFacade(poseStack);

//        float bodyYaw = Mth.rotLerp(partialTick, mindmould.yBodyRotO, mindmould.yBodyRot);
//        poseStack.translate(0.5F, 0.5F, 0.5F);
//        poseStack.mulPose(Axis.YP.rotationDegrees(bodyYaw - 180.0F));
        // reapply but modulod
//        poseStack.mulPose(Axis.YP.rotationDegrees(-(Mth.wrapDegrees(bodyYaw) % 90)));
//        poseStack.translate(-0.5F, -0.5F, -0.5F);

        // Messing around with some variance to make the vertex warping unique per Mindmould
        // but I wasn't able to get that working currently
//        poseStack.pushPose();
//        Vec3 positionVariance = mindmould.getPositionVariance();
//        poseStack.translate(positionVariance.x, positionVariance.y, positionVariance.z);
        Matrix3f inverseTransform = new Matrix3f(poseStack.last().pose()).invert();
//        poseStack.popPose();

        Map<TextureAtlasSprite, RenderType> renderTypes = new HashMap<>();
        Vector3f origin = poseStack.last().pose().transformPosition(0.0F, 0.0F, 0.0F, new Vector3f());
        // a bit useless now that theres like 30 more parameters being used in the rendertype
        FacadeVertexConsumer consumer = new FacadeVertexConsumer(
            sprite -> buffer.getBuffer(renderTypes.computeIfAbsent(sprite, key
                -> MindmouldRenderType.mindmould(MindmouldRenderer.getVesselTexture(mindmould), mindmould.getTransformTime(), key,
                    origin.x(), origin.y(), origin.z(), inverseTransform))
            ), model.vesselCube(), mindmould.level(), blockState, Minecraft.useAmbientOcclusion()
            && facadeModel.useAmbientOcclusion() && blockState.getLightEmission() == 0,
            Minecraft.getInstance().gameRenderer.getMainCamera().getPosition()
        );

        Vec3 offset = blockState.getOffset(mindmould.level(), pos);
        poseStack.translate(-offset.x, -offset.y, -offset.z);
        blockRenderer.getModelRenderer().tesselateBlock(
            mindmould.level(), facadeModel, blockState, pos, poseStack, consumer,
            false, RandomSource.create(seed), seed, packedOverlay
        );
        poseStack.popPose();
    }

    private FacadeCorner getFacadeCorner(Mindmould mindmould) {
        int cornerHash = Mth.murmurHash3Mixer(mindmould.getUUID().hashCode());
        return FacadeCorner.getFromHash(cornerHash);
    }
}
