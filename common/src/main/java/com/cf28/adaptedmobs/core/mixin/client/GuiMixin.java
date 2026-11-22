package com.cf28.adaptedmobs.core.mixin.client;

import com.cf28.adaptedmobs.client.level.renderer.mob.mindmould.MindmouldRenderer;
import com.cf28.adaptedmobs.client.level.renderer.rendertype.MindmouldRenderType;
import com.cf28.adaptedmobs.client.registries.AMShadersRegistry;
import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    protected abstract void renderTextureOverlay(GuiGraphics guiGraphics, ResourceLocation shaderLocation, float alpha);

    // TODO move this elsewhere!!!!!!!
    @Inject(method = "renderCameraOverlays", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/entity/player/Inventory;getArmor(I)Lnet/minecraft/world/item/ItemStack;")
    )
    private void adaptedMobs$renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;
        if (Mindmould.getAnyRiding(minecraft.player) == null) return;
        if (minecraft.level == null) return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();

        ShaderInstance previousShader = RenderSystem.getShader();

        RenderSystem.setShaderTexture(0, MindmouldRenderer.getOverlayTexture());
        RenderSystem.setShader(AMShadersRegistry::getMindmouldOverlayShader);

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderGameTime(minecraft.level.getGameTime(),
            minecraft.getTimer().getGameTimeDeltaPartialTick(false)
        );

        // 0, 0, -90, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight()
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(
            VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX
        );
        bufferbuilder.addVertex(matrix4f, 0f, 0f, -90f).setUv(0f, 0f);
        bufferbuilder.addVertex(matrix4f, 0f, guiGraphics.guiHeight(), -90f).setUv(0f, 1f);
        bufferbuilder.addVertex(matrix4f, guiGraphics.guiWidth(), guiGraphics.guiHeight(), -90f).setUv(1f, 1f);
        bufferbuilder.addVertex(matrix4f, guiGraphics.guiWidth(), 0f, -90f).setUv(1f, 0f);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

        RenderSystem.setShader(() -> previousShader);
    }

    @Inject(method = "getVehicleMaxHearts", at = @At("HEAD"), cancellable = true)
    private void adaptedmobs$harpyVehicleMaxHeartsZero(LivingEntity mount, CallbackInfoReturnable<Integer> cir) {
        if (mount instanceof Harpy) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "setOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void adaptedmobs$suppressHarpyMountMessage(Component component, boolean animateColor, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getVehicle() instanceof Harpy) {
            if (component.getContents() instanceof TranslatableContents translatable) {
                if ("mount.onboard".equals(translatable.getKey())) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "renderVehicleHealth", at = @At("HEAD"), cancellable = true)
    private void adaptedmobs$suppressHarpyVehicleHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.getVehicle() instanceof Harpy) {
            ci.cancel();
        }
    }
}
