package com.cf28.adaptedmobs.core.mixin.client;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gui.class)
public class GuiMixin {
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
