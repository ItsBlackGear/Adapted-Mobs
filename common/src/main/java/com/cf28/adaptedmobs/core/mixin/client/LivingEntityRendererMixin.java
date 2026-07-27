package com.cf28.adaptedmobs.core.mixin.client;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(
        method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/EntityModel;riding:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER)
    )
    private void adaptedmobs$preventHarpyPassengerSitting(LivingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity.getVehicle() instanceof Harpy) {
            ((LivingEntityRenderer<?, ?>)(Object)this).getModel().riding = false;
        }
    }
}
