package com.cf28.adaptedmobs.core.mixin;

import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster {
    protected CreeperMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "tick",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private void tick(CallbackInfo ci) {
        if ((Object) this instanceof TamableCreeper) {
            super.tick();
            ci.cancel();
        }
    }
}