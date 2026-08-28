package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.level.item.MysteryEggItem;
import com.cf28.adaptedmobs.common.level.item.mask.ArchaicMaskItem;
import com.cf28.adaptedmobs.common.level.item.mask.MaskVariant;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;

import java.util.function.Supplier;

import static com.blackgear.platform.core.helper.ItemRegistry.create;
import static com.blackgear.platform.core.helper.ItemRegistry.createSpawnEgg;

public class AMItems {
    public static final ItemRegistry REGISTRIES = create(AdaptedMobs.MOD_ID);

    public static final Supplier<Item> GREEN_MYSTERY_EGG = REGISTRIES.register("green_mystery_egg",
            properties -> new MysteryEggItem(AMEntityTypes.CREEPER, properties),
            new Properties().stacksTo(16));
    public static final Supplier<Item> RED_MYSTERY_EGG = REGISTRIES.register("red_mystery_egg",
            properties -> new MysteryEggItem(AMEntityTypes.FESTIVE_CREEPER, properties),
            new Properties().stacksTo(16));
    public static final Supplier<Item> YELLOW_MYSTERY_EGG = REGISTRIES.register("yellow_mystery_egg",
            properties -> new MysteryEggItem(AMEntityTypes.SUPPORT_CREEPER, properties),
            new Properties().stacksTo(16));
    public static final Supplier<Item> BLUE_MYSTERY_EGG = REGISTRIES.register("blue_mystery_egg",
            properties -> new MysteryEggItem(AMEntityTypes.ROCKET_CREEPER, properties),
            new Properties().stacksTo(16));

    public static final Supplier<Item> FESTIVE_CREEPER_SPAWN_EGG = REGISTRIES.register("festive_creeper_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.FESTIVE_CREEPER, 10571065, 0, properties));
    public static final Supplier<Item> SUPPORT_CREEPER_SPAWN_EGG = REGISTRIES.register("support_creeper_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.SUPPORT_CREEPER, 7110705, 0, properties));
    public static final Supplier<Item> ROCKET_CREEPER_SPAWN_EGG = REGISTRIES.register("rocket_creeper_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.ROCKET_CREEPER, 5999444, 0, properties));

    public static final Supplier<Item> HARPY_SPAWN_EGG = REGISTRIES.register("harpy_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.HARPY, 0x333149, 0x9790a4, properties));

    public static final Supplier<Item> ENTOMBED_SPAWN_EGG = REGISTRIES.register("entombed_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.ENTOMBED, 0x3c3d42, 0x6e737c, properties));

    public static final Supplier<Item> ARCHAIC_MASK_ALCHEMIST = REGISTRIES.register("archaic_mask_alchemist",
            properties -> new ArchaicMaskItem(MaskVariant.ALCHEMIST, properties));
    public static final Supplier<Item> ARCHAIC_MASK_ARCHITECT = REGISTRIES.register("archaic_mask_architect",
            properties -> new ArchaicMaskItem(MaskVariant.ARCHITECT, properties));
    public static final Supplier<Item> ARCHAIC_MASK_BUILDER = REGISTRIES.register("archaic_mask_builder",
            properties -> new ArchaicMaskItem(MaskVariant.BUILDER, properties));
    public static final Supplier<Item> ARCHAIC_MASK_CLERIC = REGISTRIES.register("archaic_mask_cleric",
            properties -> new ArchaicMaskItem(MaskVariant.CLERIC, properties));
    public static final Supplier<Item> ARCHAIC_MASK_CRANIAL = REGISTRIES.register("archaic_mask_cranial",
            properties -> new ArchaicMaskItem(MaskVariant.CRANIAL, properties));
    public static final Supplier<Item> ARCHAIC_MASK_ODDITY = REGISTRIES.register("archaic_mask_oddity",
            properties -> new ArchaicMaskItem(MaskVariant.ODDITY, properties));
    public static final Supplier<Item> ARCHAIC_MASK_SPIRAL = REGISTRIES.register("archaic_mask_spiral",
            properties -> new ArchaicMaskItem(MaskVariant.SPIRAL, properties));
    public static final Supplier<Item> ARCHAIC_MASK_TRAVELER = REGISTRIES.register("archaic_mask_traveler",
            properties -> new ArchaicMaskItem(MaskVariant.TRAVELER, properties));
    public static final Supplier<Item> ARCHAIC_MASK_WARRIOR = REGISTRIES.register("archaic_mask_warrior",
            properties -> new ArchaicMaskItem(MaskVariant.WARRIOR, properties));
    public static final Supplier<Item> ARCHAIC_MASK_WEAVER = REGISTRIES.register("archaic_mask_weaver",
            properties -> new ArchaicMaskItem(MaskVariant.WEAVER, properties));

    public static final Supplier<Item> HARPY_EGG = REGISTRIES.register("harpy_egg",
            () -> new BlockItem(AMBlocks.HARPY_EGG.get(), new Properties()));

    public static final Supplier<Item> FESTIVE_CREEPIE_SPAWN_EGG = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("festive_creepie_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.FESTIVE_CREEPIE, 10571065, 8754737, properties))
            : null;
    public static final Supplier<Item> SUPPORT_CREEPIE_SPAWN_EGG = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("support_creepie_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.SUPPORT_CREEPIE, 7110705, 10895394, properties))
            : null;
    public static final Supplier<Item> ROCKET_CREEPIE_SPAWN_EGG = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("rocket_creepie_spawn_egg",
            properties -> createSpawnEgg(AMEntityTypes.ROCKET_CREEPIE, 5999444, 10800234, properties))
            : null;

    public static final Supplier<Item> FESTIVE_CREEPER_SPORES = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("festive_creeper_spores", TolerableCreepersIntegration::createFestiveSporesItem, new Properties())
            : null;

    public static final Supplier<Item> ROCKET_CREEPER_SPORES = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("rocket_creeper_spores", TolerableCreepersIntegration::createRocketSporesItem, new Properties())
            : null;

    public static final Supplier<Item> SUPPORT_CREEPER_SPORES = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.register("support_creeper_spores", TolerableCreepersIntegration::createSupportSporesItem, new Properties())
            : null;

    public static Item getMaskByVariant(MaskVariant variant) {
        return switch (variant) {
            case ALCHEMIST -> ARCHAIC_MASK_ALCHEMIST.get();
            case ARCHITECT -> ARCHAIC_MASK_ARCHITECT.get();
            case BUILDER -> ARCHAIC_MASK_BUILDER.get();
            case CLERIC -> ARCHAIC_MASK_CLERIC.get();
            case CRANIAL -> ARCHAIC_MASK_CRANIAL.get();
            case ODDITY -> ARCHAIC_MASK_ODDITY.get();
            case SPIRAL -> ARCHAIC_MASK_SPIRAL.get();
            case TRAVELER -> ARCHAIC_MASK_TRAVELER.get();
            case WARRIOR -> ARCHAIC_MASK_WARRIOR.get();
            case WEAVER -> ARCHAIC_MASK_WEAVER.get();
        };
    }
}