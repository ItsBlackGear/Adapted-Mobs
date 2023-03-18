package com.cf28.adaptedmobs.core.mixin;

import com.cf28.adaptedmobs.common.entity.PrimedFestiveTnt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Shadow @Final private @Nullable Entity source;

    @Inject(method = "getSourceMob", at = @At("TAIL"), cancellable = true)
    private void am$getSource(CallbackInfoReturnable<LivingEntity> cir) {
        if (this.source instanceof PrimedFestiveTnt tnt) {
            cir.setReturnValue(tnt.getOwner());
        }
    }
}