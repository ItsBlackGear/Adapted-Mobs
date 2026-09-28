package com.cf28.adaptedmobs.core.mixin.common.sun_protection;

import com.cf28.adaptedmobs.common.level.item.mask.ArchaicMaskItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Zombie.class)
public class ZombieSunProtectionMixin {
    @WrapOperation(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zombie;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;")
    )
    private ItemStack adaptedmobs$ignoreMaskForSunProtection(Zombie instance, EquipmentSlot slot, Operation<ItemStack> original) {
        ItemStack stack = original.call(instance, slot);
        if (slot == EquipmentSlot.HEAD && stack.getItem() instanceof ArchaicMaskItem) {
            return ItemStack.EMPTY;
        }
        return stack;
    }
}
