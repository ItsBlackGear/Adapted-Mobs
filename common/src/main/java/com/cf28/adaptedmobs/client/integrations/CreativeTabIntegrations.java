package com.cf28.adaptedmobs.client.integrations;

import com.blackgear.platform.common.CreativeTabs;
import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.common.registries.AMItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

import java.util.List;

public interface CreativeTabIntegrations {
    CreativeTabs.Modifier FUNCTIONAL_BLOCKS = (flag, output, operator) -> {
        output.addAllAfter(Items.CREEPER_HEAD, List.of(
            AMBlocks.FESTIVE_CREEPER_HEAD.getFirst().get(),
            AMBlocks.SUPPORT_CREEPER_HEAD.getFirst().get(),
            AMBlocks.ROCKET_CREEPER_HEAD.getFirst().get()
        ));
    };
    
    CreativeTabs.Modifier INGREDIENTS = (flag, output, operator) -> {
        output.addAllAfter(Items.GUNPOWDER, List.of(
            AMItems.GREEN_MYSTERY_EGG.get(),
            AMItems.RED_MYSTERY_EGG.get(),
            AMItems.YELLOW_MYSTERY_EGG.get(),
            AMItems.BLUE_MYSTERY_EGG.get()
        ));
    };
    
    CreativeTabs.Modifier SPAWN_EGGS = (flag, output, operator) -> {
        output.addAllAfter(Items.CREEPER_SPAWN_EGG, List.of(
            AMItems.FESTIVE_CREEPER_SPAWN_EGG.get(),
            AMItems.SUPPORT_CREEPER_SPAWN_EGG.get(),
            AMItems.ROCKET_CREEPER_SPAWN_EGG.get()
        ));
    };
    
    static void bootstrap() {
        CreativeTabs.modify(CreativeModeTabs.FUNCTIONAL_BLOCKS, FUNCTIONAL_BLOCKS);
        CreativeTabs.modify(CreativeModeTabs.INGREDIENTS, INGREDIENTS);
        CreativeTabs.modify(CreativeModeTabs.SPAWN_EGGS, SPAWN_EGGS);
    }
}