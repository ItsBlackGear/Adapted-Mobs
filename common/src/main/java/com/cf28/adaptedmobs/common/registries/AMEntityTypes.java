package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.helper.EntityRegistry;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.level.entity.AMPrimedSporeBarrel;
import com.cf28.adaptedmobs.common.level.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.level.entity.ThrownMysteryEgg;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.RocketCreeper;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.SupportCreeper;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.ThrownEgg;

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

    public static final Supplier<EntityType<Creeper>> SUPPORT_CREEPIE = REGISTRIES.entity("support_creepie",
            EntityType.Builder.<Creeper>of((type, level) -> {
                        if (TolerableCreepersCompat.isLoaded()) {
                            return (Creeper) TolerableCreepersIntegration.createSupportCreepie(type, level);
                        }
                        return new Creeper(type, level);
                    }, MobCategory.MONSTER)
                    .sized(0.3F, 0.85F)
                    .clientTrackingRange(8));

    public static final Supplier<EntityType<Creeper>> ROCKET_CREEPIE = REGISTRIES.entity("rocket_creepie",
            EntityType.Builder.<Creeper>of((type, level) -> {
                        if (TolerableCreepersCompat.isLoaded()) {
                            return (Creeper) TolerableCreepersIntegration.createRocketCreepie(type, level);
                        }
                        return new Creeper(type, level);
                    }, MobCategory.MONSTER)
                    .sized(0.3F, 0.85F)
                    .clientTrackingRange(8));

    public static final Supplier<EntityType<Creeper>> FESTIVE_CREEPIE = REGISTRIES.entity("festive_creepie",
            EntityType.Builder.<Creeper>of((type, level) -> {
                        if (TolerableCreepersCompat.isLoaded()) {
                            return (Creeper) TolerableCreepersIntegration.createFestiveCreepie(type, level);
                        }
                        return new Creeper(type, level);
                    }, MobCategory.MONSTER)
                    .sized(0.3F, 0.7F)
                    .clientTrackingRange(8));

    public static final Supplier<EntityType<Entity>> SUPPORT_SPORES = REGISTRIES.entity("support_spores",
            EntityType.Builder.of((type, level) -> {
                        if (TolerableCreepersCompat.isLoaded()) {
                            return TolerableCreepersIntegration.createSupportSpores(type, level);
                        }
                        return new ThrownEgg(EntityType.EGG, level);
                    }, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    public static final Supplier<EntityType<Entity>> ROCKET_SPORES = REGISTRIES.entity("rocket_spores",
            EntityType.Builder.of((type, level) -> {
                        if (TolerableCreepersCompat.isLoaded()) {
                            return TolerableCreepersIntegration.createRocketSpores(type, level);
                        }
                        return new ThrownEgg(EntityType.EGG, level);
                    }, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    public static final Supplier<EntityType<Entity>> FESTIVE_SPORES = REGISTRIES.entity("festive_spores",
            EntityType.Builder.of((type, level) -> {
                        if (TolerableCreepersCompat.isLoaded()) {
                            return TolerableCreepersIntegration.createFestiveSpores(type, level);
                        }
                        return new ThrownEgg(EntityType.EGG, level);
                    }, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10));

    public static final Supplier<EntityType<AMPrimedSporeBarrel>> PRIMED_SPORE_BARREL = REGISTRIES.entity("primed_spore_barrel",
            EntityType.Builder.<AMPrimedSporeBarrel>of(AMPrimedSporeBarrel::new, MobCategory.MISC)
                    .fireImmune()
                    .eyeHeight(0.15F)
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(10));
}