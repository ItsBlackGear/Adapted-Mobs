package com.cf28.adaptedmobs.core.mixin.common;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerDismountMixin {
    @Inject(method = "wantsToStopRiding", at = @At("HEAD"), cancellable = true)
    private void adaptedmobs$preventHarpyDismount(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (player.getVehicle() instanceof Harpy) {
            cir.setReturnValue(false);
        }
    }
}
