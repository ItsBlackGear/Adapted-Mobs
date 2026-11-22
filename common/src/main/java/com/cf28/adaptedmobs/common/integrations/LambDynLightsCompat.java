package com.cf28.adaptedmobs.common.integrations;

import com.blackgear.platform.core.Environment;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.state.BlockState;

public final class LambDynLightsCompat {
    public static final String MOD_ID = "lambdynlights_runtime";

    public static boolean isLoaded() {
        return Environment.hasModLoaded(MOD_ID);
    }

    public static int getLivingEntityLuminance(final LivingEntity entity) {
        if (!isLoaded() || entity == null) {
            return 0;
        }

        if (entity.isOnFire()) {
            return 15;
        }

        return Math.max(getItemLight(entity.getItemInHand(InteractionHand.MAIN_HAND)), getItemLight(entity.getItemInHand(InteractionHand.OFF_HAND)));
    }

    private static int getItemLight(final ItemStack itemStack) {
        if (itemStack.getItem() instanceof final BlockItem blockItem) {
            final BlockItemStateProperties properties = itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
            BlockState state = blockItem.getBlock().defaultBlockState();

            if (!properties.isEmpty()) {
                state = properties.apply(state);
            }

            return state.getLightEmission();
        }

        return 0;
    }
}
