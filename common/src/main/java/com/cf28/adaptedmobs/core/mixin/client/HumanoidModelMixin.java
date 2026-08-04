package com.cf28.adaptedmobs.core.mixin.client;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {
    @Unique
    private static final float ADAPTEDMOBS$ARM_LIFT = (float) Math.PI;

    @Final
    @Shadow
    public ModelPart rightArm;
    @Final
    @Shadow
    public ModelPart leftArm;

    @Unique
    private static boolean adaptedmobs$carriesHarpy(Player player) {
        for (Entity passenger : player.getPassengers()) {
            if (passenger instanceof Harpy) {
                return true;
            }
        }
        return false;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void adaptedmobs$raiseArmsForPerchedHarpy(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!(entity instanceof Player player) || !adaptedmobs$carriesHarpy(player)) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (player == minecraft.getCameraEntity() && minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }

        this.rightArm.xRot = ADAPTEDMOBS$ARM_LIFT;
        this.rightArm.yRot = 0.0F;
        this.rightArm.zRot = 0.0F;
        this.leftArm.xRot = ADAPTEDMOBS$ARM_LIFT;
        this.leftArm.yRot = 0.0F;
        this.leftArm.zRot = 0.0F;
    }
}
