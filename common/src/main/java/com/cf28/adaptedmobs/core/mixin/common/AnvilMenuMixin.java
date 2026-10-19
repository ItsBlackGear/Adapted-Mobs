package com.cf28.adaptedmobs.core.mixin.common;

import com.cf28.adaptedmobs.common.registries.AMItems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    /*
    * If you're wondering why this method exists when we already override it in ItemMixin
    * it's because Elytras, for example, don't call the base Item.isValidRepairItem()
    *
    * instead of fixing it for every item, I'm just going to bandage it for the anvil as well
    * the general item mixin helps if someone is trying to reimplement repairing in some way
    * but there's only so much you can do if people don't implement things correctly !!!!!!
    */
    @WrapOperation(method = "createResult", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/item/Item;isValidRepairItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
    ))
    private boolean adaptedMobs$quicksilverRepairall(
        Item instance, ItemStack stack, ItemStack repairCandidate, Operation<Boolean> original
    ) {
        if (repairCandidate.is(AMItems.QUICKSILVER.get())) return true;
        return original.call(instance, stack, repairCandidate);
    }
}
