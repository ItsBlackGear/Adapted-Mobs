package com.cf28.adaptedmobs.client.integrations;

import com.blackgear.platform.common.CreativeTabs;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.common.registries.AMItems;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

import java.util.List;

public interface CreativeTabIntegrations {
    CreativeTabs.Modifier FUNCTIONAL_BLOCKS = (flag, output, operator) -> {
        output.addAllAfter(Items.CREEPER_HEAD, List.of(
                AMBlocks.FESTIVE_CREEPER_HEAD.getFirst().get(),
                AMBlocks.SUPPORT_CREEPER_HEAD.getFirst().get(),
                AMBlocks.ROCKET_CREEPER_HEAD.getFirst().get(),
                AMItems.HARPY_EGG.get()
        ));

        if (TolerableCreepersCompat.isLoaded()) {
            output.addAllAfter(Items.BARREL, List.of(
                    AMBlocks.SUPPORT_SPORE_BARREL.get().asItem(),
                    AMBlocks.ROCKET_SPORE_BARREL.get().asItem(),
                    AMBlocks.FESTIVE_SPORE_BARREL.get().asItem()
            ));
        }
    };

    CreativeTabs.Modifier INGREDIENTS = (flag, output, operator) -> {
        if (AdaptedMobs.CONFIG.enableMysteryEggs.get()) {
            output.addAllAfter(Items.GUNPOWDER, List.of(
                    AMItems.GREEN_MYSTERY_EGG.get(),
                    AMItems.RED_MYSTERY_EGG.get(),
                    AMItems.YELLOW_MYSTERY_EGG.get(),
                    AMItems.BLUE_MYSTERY_EGG.get()
            ));
        }

        if (TolerableCreepersCompat.isLoaded()) {
            output.addAllAfter(Items.GUNPOWDER, List.of(
                    AMItems.SUPPORT_CREEPER_SPORES.get(),
                    AMItems.ROCKET_CREEPER_SPORES.get(),
                    AMItems.FESTIVE_CREEPER_SPORES.get()
            ));
        }
    };

    CreativeTabs.Modifier COMBAT = (flag, output, operator) -> {
        output.addAllAfter(Items.TURTLE_HELMET, List.of(
                AMItems.DEEPSLATE_MASK.get(),
                AMItems.GRINNING_DEEPSLATE_MASK.get(),
                AMItems.WEEPING_DEEPSLATE_MASK.get(),
                AMItems.WRATH_DEEPSLATE_MASK.get(),
                AMItems.ANCIENT_DEEPSLATE_MASK.get(),
                AMItems.SCREAMING_DEEPSLATE_MASK.get()
        ));
    };

    CreativeTabs.Modifier SPAWN_EGGS = (flag, output, operator) -> {
        output.addAllAfter(Items.CREEPER_SPAWN_EGG, List.of(
                AMItems.FESTIVE_CREEPER_SPAWN_EGG.get(),
                AMItems.SUPPORT_CREEPER_SPAWN_EGG.get(),
                AMItems.ROCKET_CREEPER_SPAWN_EGG.get(),
                AMItems.HARPY_SPAWN_EGG.get(),
                AMItems.ENTOMBED_SPAWN_EGG.get()
        ));

        if (TolerableCreepersCompat.isLoaded()) {
            output.addAllAfter(AMItems.ROCKET_CREEPER_SPAWN_EGG.get(), List.of(
                    AMItems.FESTIVE_CREEPIE_SPAWN_EGG.get(),
                    AMItems.SUPPORT_CREEPIE_SPAWN_EGG.get(),
                    AMItems.ROCKET_CREEPIE_SPAWN_EGG.get()
            ));
        }
    };

    static void bootstrap() {
        CreativeTabs.modify(CreativeModeTabs.FUNCTIONAL_BLOCKS, FUNCTIONAL_BLOCKS);
        CreativeTabs.modify(CreativeModeTabs.INGREDIENTS, INGREDIENTS);
        CreativeTabs.modify(CreativeModeTabs.COMBAT, COMBAT);
        CreativeTabs.modify(CreativeModeTabs.SPAWN_EGGS, SPAWN_EGGS);
    }
}