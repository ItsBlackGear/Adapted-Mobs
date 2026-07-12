package com.cf28.adaptedmobs.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public final class RocketFlightMath {
    private static final double GRAVITY = 0.08;
    private static final double DRAG = 0.98;

    public static int predictFlightTicks(double initialVerticalVelocity) {
        double velocity = initialVerticalVelocity;
        double height = 0.0;
        int ticks = 0;
        do {
            ticks++;
            velocity = (velocity - GRAVITY) * DRAG;
            height += velocity;
        } while (height > 0.0);
        return ticks;
    }

    public static boolean hasEnoughVerticalSpace(Entity entity) {
        BlockPos pos = entity.blockPosition();
        while (pos.getY() < entity.level().getHeight()) {
            BlockState state = entity.level().getBlockState(pos);
            if (!state.canBeReplaced())
                return false;

            pos = pos.above();
        }

        return true;
    }
}
