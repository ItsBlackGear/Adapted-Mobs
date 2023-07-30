package com.cf28.adaptedmobs.core.mixin.access;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Creeper.class)
public interface CreeperAccessor {
    @Accessor
    int getExplosionRadius();

    @Accessor
    void setOldSwell(int oldSwell);

    @Accessor
    int getSwell();

    @Accessor
    void setSwell(int swell);

    @Accessor
    int getMaxSwell();

    @Invoker
    void callExplodeCreeper();
}