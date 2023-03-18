package com.cf28.adaptedmobs.core.mixin;

import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    private final LivingEntity instance = LivingEntity.class.cast(this);

    @Inject(method = "hurt", at = @At("TAIL"), cancellable = true)
    private void am$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = source.getEntity();
        if (entity instanceof FestiveCreeper creeper) {
            if (creeper.getOwner() == this.instance) {
                cir.setReturnValue(false);
            }

            if (this.instance instanceof OwnableEntity pet) {
                cir.setReturnValue(pet.getOwner() != creeper.getOwner());
            }

            if (creeper.isAlliedTo(this.instance)) {
                cir.setReturnValue(false);
            }
        }
    }
}