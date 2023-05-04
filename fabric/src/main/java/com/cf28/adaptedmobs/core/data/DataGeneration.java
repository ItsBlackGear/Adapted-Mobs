package com.cf28.adaptedmobs.core.data;

import com.cf28.adaptedmobs.core.data.common.loot.EntityLootGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        gen.addProvider(EntityLootGenerator::new);
    }
}