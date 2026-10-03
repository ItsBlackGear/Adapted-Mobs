package com.cf28.adaptedmobs.core.mixin.client;

import com.cf28.adaptedmobs.client.level.StrangerSpawner;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void adaptedmobs$tick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        StrangerSpawner.tick((ClientLevel) (Object) this);
    }
}
