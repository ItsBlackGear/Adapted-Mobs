package com.cf28.adaptedmobs.fabric.data.server.tags;

import com.cf28.adaptedmobs.core.tags.AMItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider<Item> {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, Registries.ITEM, lookup);
    }
    
    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.getOrCreateTagBuilder(AMItemTags.CREEPER_FOOD)
            .add(Items.GUNPOWDER);
    }
}