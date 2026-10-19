package com.cf28.adaptedmobs.core.mixin.common;

import com.cf28.adaptedmobs.common.registries.AMItems;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin {

    @WrapMethod(method = "isValidRepairItem")
    private boolean adaptedMobs$quicksilverRepairall(ItemStack toRepair, ItemStack repair, Operation<Boolean> original) {
        if (toRepair.isDamageableItem() && repair.is(AMItems.QUICKSILVER.get())) return true;
        return original.call(toRepair, repair);
    }
}
