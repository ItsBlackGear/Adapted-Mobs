package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.helper.ParticleRegistry;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class AMParticles {
    public static final ParticleRegistry REGISTRIES = ParticleRegistry.create(AdaptedMobs.MOD_ID);

    public static final Supplier<SimpleParticleType> FESTIVE_TNT_PARTICLETRAIL = REGISTRIES.register("festive_tnt_particletrail", false);
    public static final Supplier<SimpleParticleType> FESTIVE_SPORES = REGISTRIES.register("festive_spores", false);
    public static final Supplier<SimpleParticleType> ROCKET_SPORES = REGISTRIES.register("rocket_spores", false);
    public static final Supplier<SimpleParticleType> SUPPORTED_RED = REGISTRIES.register("supported_red", false);
    public static final Supplier<SimpleParticleType> SUPPORTED_BLUE = REGISTRIES.register("supported_blue", false);
    public static final Supplier<SimpleParticleType> SUPPORTED_YELLOW = REGISTRIES.register("supported_yellow", false);
    public static final Supplier<SimpleParticleType> SUPPORTED_GREY = REGISTRIES.register("supported_grey", false);
    public static final Supplier<SimpleParticleType> CREEPER_HEAL = REGISTRIES.register("creeper_heal", false);
}
