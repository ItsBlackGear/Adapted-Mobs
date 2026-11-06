package com.cf28.adaptedmobs.core.mixin.client;

import com.cf28.adaptedmobs.client.level.renderer.mob.mindmould.MindmouldRenderer;
import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
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

    @Inject(method = "renderCameraOverlays", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/entity/player/Inventory;getArmor(I)Lnet/minecraft/world/item/ItemStack;")
    )
    private void adaptedMobs$renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null) return;
        if (Mindmould.getAnyRiding(Minecraft.getInstance().player) == null) return;

        this.renderTextureOverlay(guiGraphics, MindmouldRenderer.getOverlayTexture(), 1.0F);
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
