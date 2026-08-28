package com.cf28.adaptedmobs.fabric.client;

import com.cf28.adaptedmobs.client.level.model.mob.ArchaicMaskModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.item.mask.ArchaicMaskItem;
import com.cf28.adaptedmobs.common.registries.AMItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;

public final class AdaptedMobsFabricClient implements ClientModInitializer {
    private static ArchaicMaskModel maskModel;

    @Override
    public void onInitializeClient() {
        ArmorRenderer.register((matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {
            if (slot == EquipmentSlot.HEAD && stack.getItem() instanceof ArchaicMaskItem maskItem) {
                if (maskModel == null) {
                    maskModel = new ArchaicMaskModel(Minecraft.getInstance().getEntityModels().bakeLayer(AMModelLayers.ARCHAIC_MASK));
                }
                maskModel.head.copyFrom(contextModel.head);
                ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, maskModel, maskItem.getVariant().getTexture());
            }
        },
                AMItems.ARCHAIC_MASK_ALCHEMIST.get(),
                AMItems.ARCHAIC_MASK_ARCHITECT.get(),
                AMItems.ARCHAIC_MASK_BUILDER.get(),
                AMItems.ARCHAIC_MASK_CLERIC.get(),
                AMItems.ARCHAIC_MASK_CRANIAL.get(),
                AMItems.ARCHAIC_MASK_ODDITY.get(),
                AMItems.ARCHAIC_MASK_SPIRAL.get(),
                AMItems.ARCHAIC_MASK_TRAVELER.get(),
                AMItems.ARCHAIC_MASK_WARRIOR.get(),
                AMItems.ARCHAIC_MASK_WEAVER.get()
        );
    }
}
