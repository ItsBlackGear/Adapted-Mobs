package com.cf28.adaptedmobs.core.mixin.client;

import com.cf28.adaptedmobs.client.level.renderer.effect.MindmouldEffectRenderer;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getMainRenderTarget()Lcom/mojang/blaze3d/pipeline/RenderTarget;"))
    private void adaptedMobs$renderPostEffects(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        Entity cameraEntity = Minecraft.getInstance().cameraEntity;
        if (cameraEntity == null) return;

        boolean isMindmould = cameraEntity.getType().equals(AMEntityTypes.MINDMOULD.get());
        if (isMindmould || Mindmould.getAnyRiding(cameraEntity) != null) {
            MindmouldEffectRenderer.getInstance().render(
                this.minecraft, deltaTracker.getRealtimeDeltaTicks(), isMindmould
            );
        }
    }

}
