package com.cf28.adaptedmobs.core.mixin.common.creeper_head_detection;

import com.cf28.adaptedmobs.common.registries.AMBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private static boolean am$isCreeperHead(ItemStack itemstack) {
        return itemstack.is(Items.CREEPER_HEAD)
                || itemstack.is(AMBlocks.FESTIVE_CREEPER_HEAD.getFirst().get().asItem())
                || itemstack.is(AMBlocks.SUPPORT_CREEPER_HEAD.getFirst().get().asItem())
                || itemstack.is(AMBlocks.ROCKET_CREEPER_HEAD.getFirst().get().asItem())
                || itemstack.is(AMBlocks.PEEPER_CREEPER_HEAD.getFirst().get().asItem());
    }

    @Inject(method = "getVisibilityPercent", at = @At("RETURN"), cancellable = true)
    private void am$extendCreeperHeadDetection(Entity lookingEntity, CallbackInfoReturnable<Double> cir) {
        if (!(lookingEntity instanceof Creeper)) {
            return;
        }

        LivingEntity self = (LivingEntity) (Object) this;
        ItemStack itemstack = self.getItemBySlot(EquipmentSlot.HEAD);
        boolean vanillaMatch = lookingEntity.getType() == EntityType.CREEPER && itemstack.is(Items.CREEPER_HEAD);
        if (!vanillaMatch && am$isCreeperHead(itemstack)) {
            cir.setReturnValue(cir.getReturnValue() * 0.5);
        }
    }
}
