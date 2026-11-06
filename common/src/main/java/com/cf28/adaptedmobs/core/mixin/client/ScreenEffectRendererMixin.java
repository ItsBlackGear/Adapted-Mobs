package com.cf28.adaptedmobs.core.mixin.client;

import com.cf28.adaptedmobs.client.level.renderer.mob.mindmould.MindmouldRenderer;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "renderScreenEffect", at = @At("HEAD"))
    private static void adaptedMobs$renderMindmouldEffect(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        if (minecraft.player == null) return;

        Mindmould mindmould = Mindmould.getAnyRiding(minecraft.player);
        if (mindmould == null || mindmould.isInvisible()) return;

//        MindmouldRenderer.renderScreenEffect(mindmould, poseStack);
    }
}
