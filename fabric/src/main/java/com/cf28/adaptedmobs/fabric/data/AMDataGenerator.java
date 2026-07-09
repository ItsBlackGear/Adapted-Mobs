package com.cf28.adaptedmobs.fabric.data;

import com.cf28.adaptedmobs.fabric.data.client.LangGenerator;
import com.cf28.adaptedmobs.fabric.data.client.ModelGenerator;
import com.cf28.adaptedmobs.fabric.data.server.loot.BlockLootGenerator;
import com.cf28.adaptedmobs.fabric.data.server.loot.EntityLootGenerator;
import com.cf28.adaptedmobs.fabric.data.server.tags.BiomeTagGenerator;
import com.cf28.adaptedmobs.fabric.data.server.tags.ItemTagGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class AMDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        
        pack.addProvider(LangGenerator::new);
        pack.addProvider(ModelGenerator::new);
        
        pack.addProvider(BlockLootGenerator::new);
        pack.addProvider(EntityLootGenerator::new);
        
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(BiomeTagGenerator::new);
    }
}