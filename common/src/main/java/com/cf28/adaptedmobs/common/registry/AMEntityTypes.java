package com.cf28.adaptedmobs.common.registry;

import com.cf28.adaptedmobs.common.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.entity.ThrownMysteryEgg;
import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.platform.CoreRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class AMEntityTypes {
    public static final CoreRegistry<EntityType<?>> ENTITIES = CoreRegistry.create(Registry.ENTITY_TYPE, AdaptedMobs.MOD_ID);

    public static final Supplier<EntityType<FestiveCreeper>> FESTIVE_CREEPER = create("festive_creeper", EntityType.Builder.of(FestiveCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8));
    public static final Supplier<EntityType<SupportCreeper>> SUPPORT_CREEPER = create("support_creeper", EntityType.Builder.of(SupportCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));

    public static final Supplier<EntityType<PrimedFestiveTnt>> FESTIVE_TNT = create("festive_tnt", EntityType.Builder.<PrimedFestiveTnt>of(PrimedFestiveTnt::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10));
    public static final Supplier<EntityType<ThrownMysteryEgg>> MYSTERY_EGG = create("egg", EntityType.Builder.<ThrownMysteryEgg>of(ThrownMysteryEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

    private static <T extends Entity> Supplier<EntityType<T>> create(String key, EntityType.Builder<T> builder) {
        return ENTITIES.register(key, () -> builder.build(key));
    }
}