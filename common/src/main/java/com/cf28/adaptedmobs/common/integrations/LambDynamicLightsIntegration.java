package com.cf28.adaptedmobs.common.integrations;

import dev.lambdaurora.lambdynlights.api.DynamicLightsContext;
import dev.lambdaurora.lambdynlights.api.DynamicLightsInitializer;
import dev.lambdaurora.lambdynlights.api.item.ItemLightSourceManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LambDynamicLightsIntegration implements DynamicLightsInitializer {
    private static DynamicLightsContext context;
    private static ItemLightSourceManager itemManager;

    @Override
    public void onInitializeDynamicLights(DynamicLightsContext ctx) {
        context = ctx;
        itemManager = ctx.itemLightSourceManager();
    }

    @SuppressWarnings("removal")
    @Override
    public void onInitializeDynamicLights(ItemLightSourceManager manager) {
        itemManager = manager;
    }

    public static int getLivingEntityLuminance(LivingEntity entity) {
        if (entity == null || context == null) {
            return 0;
        }

        try {
            int lum = context.entityLightSourceManager().getLuminance(entity);
            if (lum > 0) {
                return lum;
            }
        } catch (Throwable ignored) {
        }

        return getHeldItemLuminance(entity);
    }

    public static int getLuminance(ItemStack stack) {
        if (stack == null || stack.isEmpty() || itemManager == null) {
            return 0;
        }

        try {
            return itemManager.getLuminance(stack);
        } catch (Throwable ignored) {
            return 0;
        }
    }

    public static int getHeldItemLuminance(LivingEntity entity) {
        if (entity == null || itemManager == null) {
            return 0;
        }

        int main = getLuminance(entity.getMainHandItem());
        int off = getLuminance(entity.getOffhandItem());
        return Math.max(main, off);
    }
}
