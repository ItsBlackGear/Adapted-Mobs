package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.cf28.adaptedmobs.common.level.effect.SporeMobEffect;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

public class AMMobEffects {
    public static final CoreRegistry<MobEffect> REGISTRIES = CoreRegistry.create(Registries.MOB_EFFECT, AdaptedMobs.MOD_ID);

    public static final Holder<MobEffect> SPORE = REGISTRIES.holder("spore", SporeMobEffect::new);
}
