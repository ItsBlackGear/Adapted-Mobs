package com.cf28.adaptedmobs.core.mixin.access;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Creeper.class)
public interface CreeperAccessor {
    @Accessor
    static EntityDataAccessor<Boolean> getDATA_IS_POWERED() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    int getExplosionRadius();

    @Invoker
    void callSpawnLingeringCloud();

    @Accessor
    void setMaxSwell(int maxSwell);
}