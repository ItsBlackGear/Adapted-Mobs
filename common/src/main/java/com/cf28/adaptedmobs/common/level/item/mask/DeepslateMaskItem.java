package com.cf28.adaptedmobs.common.level.item.mask;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;

public class DeepslateMaskItem extends ArmorItem {
    private final MaskVariant variant;

    public DeepslateMaskItem(MaskVariant variant, Properties properties) {
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
}
