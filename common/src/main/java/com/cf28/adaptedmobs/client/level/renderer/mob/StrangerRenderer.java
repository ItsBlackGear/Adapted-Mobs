package com.cf28.adaptedmobs.client.level.renderer.mob;

import com.cf28.adaptedmobs.common.level.entity.mob.Stranger;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class StrangerRenderer extends MobRenderer<Stranger, PlayerModel<Stranger>> {
    private static final RenderType EYES = RenderType.eyes(AdaptedMobs.resource("textures/entity/player/eyes.png"));

    public StrangerRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        this.addLayer(new EyesLayer<>(this) {
            @Override
            public @NotNull RenderType renderType() {
                return EYES;
            }
        });
    }

    @Override
    public boolean shouldRender(@NotNull Stranger stranger, @NotNull Frustum frustum, double camX, double camY, double camZ) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return false;

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        if (stranger.updateGaze(camera.getPosition(), new Vec3(camera.getLookVector()), player.hasLineOfSight(stranger)))
            return false;

        return super.shouldRender(stranger, frustum, camX, camY, camZ);
    }

    @Override
    protected void scale(@NotNull Stranger stranger, PoseStack poseStack, float partialTick) {
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Stranger stranger) {
        return DefaultPlayerSkin.getDefaultTexture();
    }
}
