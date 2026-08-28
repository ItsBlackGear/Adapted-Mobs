package com.cf28.adaptedmobs.neoforge.client;

import com.cf28.adaptedmobs.client.level.model.mob.ArchaicMaskModel;
import com.cf28.adaptedmobs.client.registries.AMModelLayers;
import com.cf28.adaptedmobs.common.level.item.mask.ArchaicMaskItem;
import com.cf28.adaptedmobs.common.registries.AMItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public class AdaptedMobsNeoForgeClient {
    private static ArchaicMaskModel maskModel;

    public static void init(ModContainer modContainer) {
        modContainer.getEventBus().addListener(RegisterClientExtensionsEvent.class, AdaptedMobsNeoForgeClient::onRegisterClientExtensions);
    }

    private static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(
                new IClientItemExtensions() {
                    @Override
                    public Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                        if (equipmentSlot == EquipmentSlot.HEAD && itemStack.getItem() instanceof ArchaicMaskItem) {
                            if (maskModel == null) {
                                maskModel = new ArchaicMaskModel(Minecraft.getInstance().getEntityModels().bakeLayer(AMModelLayers.ARCHAIC_MASK));
                            }
                            maskModel.head.copyFrom(original.head);
                            return maskModel;
                        }
                        return original;
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
