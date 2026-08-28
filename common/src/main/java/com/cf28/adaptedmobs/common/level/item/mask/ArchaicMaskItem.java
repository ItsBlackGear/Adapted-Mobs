package com.cf28.adaptedmobs.common.level.item.mask;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;

public class ArchaicMaskItem extends ArmorItem {
    private final MaskVariant variant;

    public ArchaicMaskItem(MaskVariant variant, Properties properties) {
        super(ArmorMaterials.IRON, Type.HELMET, properties.durability(275));
        this.variant = variant;
    }

    public MaskVariant getVariant() {
        return this.variant;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_GENERIC;
    }

    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return this.variant.getTexture();
    }
}
