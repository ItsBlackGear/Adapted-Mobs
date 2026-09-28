package com.cf28.adaptedmobs.core.mixin.common.sun_protection;

import com.cf28.adaptedmobs.common.level.item.mask.ArchaicMaskItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FleeSunGoal.class)
public class FleeSunGoalMixin {
    @WrapOperation(
            method = "canUse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/PathfinderMob;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;")
    )
    private ItemStack adaptedmobs$ignoreMaskForFleeSun(PathfinderMob instance, EquipmentSlot slot, Operation<ItemStack> original) {
        ItemStack stack = original.call(instance, slot);
        if (slot == EquipmentSlot.HEAD && stack.getItem() instanceof ArchaicMaskItem) {
            return ItemStack.EMPTY;
        }
        return stack;
    }
}
