package com.cf28.adaptedmobs.fabric.data.client;

import com.cf28.adaptedmobs.common.registries.AMItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;

public class ModelGenerator extends FabricModelProvider {
    private static final ResourceLocation TEMPLATE_SPAWN_EGG = ModelLocationUtils.decorateItemModelLocation("template_spawn_egg");
    
    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }
    
    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        gen.delegateItemModel(AMItems.FESTIVE_CREEPER_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);
        gen.delegateItemModel(AMItems.SUPPORT_CREEPER_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);
        gen.delegateItemModel(AMItems.ROCKET_CREEPER_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);
        gen.delegateItemModel(AMItems.FESTIVE_CREEPIE_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);
        gen.delegateItemModel(AMItems.SUPPORT_CREEPIE_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);
        gen.delegateItemModel(AMItems.ROCKET_CREEPIE_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);
        gen.delegateItemModel(AMItems.HARPY_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);
        gen.delegateItemModel(AMItems.ENTOMBED_SPAWN_EGG.get(), TEMPLATE_SPAWN_EGG);
    }
    
    @Override
    public void generateItemModels(ItemModelGenerators gen) {
        gen.generateFlatItem(AMItems.RED_MYSTERY_EGG.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(AMItems.YELLOW_MYSTERY_EGG.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(AMItems.BLUE_MYSTERY_EGG.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(AMItems.GREEN_MYSTERY_EGG.get(), ModelTemplates.FLAT_ITEM);

        gen.generateFlatItem(AMItems.DEEPSLATE_MASK.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(AMItems.GRINNING_DEEPSLATE_MASK.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(AMItems.WEEPING_DEEPSLATE_MASK.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(AMItems.WRATH_DEEPSLATE_MASK.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(AMItems.ANCIENT_DEEPSLATE_MASK.get(), ModelTemplates.FLAT_ITEM);
        gen.generateFlatItem(AMItems.SCREAMING_DEEPSLATE_MASK.get(), ModelTemplates.FLAT_ITEM);
    }
}