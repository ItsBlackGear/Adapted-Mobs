package com.cf28.adaptedmobs.core.mixin.common.spore_effect;

import com.cf28.adaptedmobs.common.registries.AMMobEffects;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {
    // gotta lie and say spored players aren't hurt to skip natural regen
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isHurt()Z"))
    private boolean am$blockSporedRegen(Player player, Operation<Boolean> original) {
        return original.call(player) && !player.hasEffect(AMMobEffects.SPORE);
    }
}
