package com.cf28.adaptedmobs.core.mixin.common.villager_fear;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {
    @Unique
    private static final float HARPY_FEAR_DISTANCE = 12.0F;

    @Inject(method = "isHostile", at = @At("HEAD"), cancellable = true)
    private void adaptedmobs$fearHarpies(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Harpy harpy && !harpy.isTame()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isClose", at = @At("HEAD"), cancellable = true)
    private void adaptedmobs$harpyFearDistance(LivingEntity attacker, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof Harpy) {
            cir.setReturnValue(target.distanceToSqr(attacker) <= HARPY_FEAR_DISTANCE * HARPY_FEAR_DISTANCE);
        }
    }
}
