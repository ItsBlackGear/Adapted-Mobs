package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.helper.EntityRegistry;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.level.entity.AMPrimedSporeBarrel;
import com.cf28.adaptedmobs.common.level.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.level.entity.ThrownMysteryEgg;
import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.RocketCreeper;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.SupportCreeper;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Creeper;

import java.util.function.Supplier;

public class AMEntityTypes {
    public static final EntityRegistry REGISTRIES = EntityRegistry.create(AdaptedMobs.MOD_ID);

    public static final Supplier<EntityType<TamableCreeper>> CREEPER = REGISTRIES.entity("creeper",
            EntityType.Builder.of(TamableCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F)
                    .clientTrackingRange(8));
    public static final Supplier<EntityType<FestiveCreeper>> FESTIVE_CREEPER = REGISTRIES.entity("festive_creeper",
            EntityType.Builder.of(FestiveCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.5F)
                    .clientTrackingRange(8));
    public static final Supplier<EntityType<SupportCreeper>> SUPPORT_CREEPER = REGISTRIES.entity("support_creeper",
            EntityType.Builder.of(SupportCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.6F)
                    .clientTrackingRange(8));
    public static final Supplier<EntityType<RocketCreeper>> ROCKET_CREEPER = REGISTRIES.entity("rocket_creeper",
            EntityType.Builder.of(RocketCreeper::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.25F)
                    .clientTrackingRange(8));

    public static final Supplier<EntityType<PrimedFestiveTnt>> FESTIVE_TNT = REGISTRIES.entity("festive_tnt",
            EntityType.Builder.<PrimedFestiveTnt>of(PrimedFestiveTnt::new, MobCategory.MISC)
                    .fireImmune()
                    .eyeHeight(0.15F)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(10));
    public static final Supplier<EntityType<ThrownMysteryEgg>> MYSTERY_EGG = REGISTRIES.entity("mystery_egg",
            EntityType.Builder.<ThrownMysteryEgg>of(ThrownMysteryEgg::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    public static final Supplier<EntityType<Creeper>> SUPPORT_CREEPIE = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.entity("support_creepie",
            EntityType.Builder.<Creeper>of((type, level) -> (Creeper) TolerableCreepersIntegration.createSupportCreepie(type, level), MobCategory.MONSTER)
                    .sized(0.3F, 0.85F)
                    .clientTrackingRange(8))
            : null;

    public static final Supplier<EntityType<Creeper>> ROCKET_CREEPIE = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.entity("rocket_creepie",
            EntityType.Builder.<Creeper>of((type, level) -> (Creeper) TolerableCreepersIntegration.createRocketCreepie(type, level), MobCategory.MONSTER)
                    .sized(0.3F, 0.85F)
                    .clientTrackingRange(8))
            : null;

    public static final Supplier<EntityType<Creeper>> FESTIVE_CREEPIE = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.entity("festive_creepie",
            EntityType.Builder.<Creeper>of((type, level) -> (Creeper) TolerableCreepersIntegration.createFestiveCreepie(type, level), MobCategory.MONSTER)
                    .sized(0.3F, 0.7F)
                    .clientTrackingRange(8))
            : null;

    public static final Supplier<EntityType<Entity>> SUPPORT_SPORES = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.entity("support_spores",
            EntityType.Builder.of(TolerableCreepersIntegration::createSupportSpores, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10))
            : null;

    public static final Supplier<EntityType<Entity>> ROCKET_SPORES = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.entity("rocket_spores",
            EntityType.Builder.of(TolerableCreepersIntegration::createRocketSpores, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10))
            : null;

    public static final Supplier<EntityType<Entity>> FESTIVE_SPORES = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.entity("festive_spores",
            EntityType.Builder.of(TolerableCreepersIntegration::createFestiveSpores, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10))
            : null;

    public static final Supplier<EntityType<AMPrimedSporeBarrel>> PRIMED_SPORE_BARREL = REGISTRIES.entity("primed_spore_barrel",
            EntityType.Builder.<AMPrimedSporeBarrel>of(AMPrimedSporeBarrel::new, MobCategory.MISC)
                    .fireImmune()
                    .eyeHeight(0.15F)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(10));

    public static final Supplier<EntityType<Harpy>> HARPY = REGISTRIES.entity("harpy",
            EntityType.Builder.of(Harpy::new, MobCategory.CREATURE)
                    .sized(0.75F, 1.4F)
                    .clientTrackingRange(8));

    public static final Supplier<EntityType<Entombed>> ENTOMBED = REGISTRIES.entity("entombed",
            EntityType.Builder.of(Entombed::new, MobCategory.MONSTER)
                    .sized(0.6F, 2.1F)
                    .clientTrackingRange(8));
}