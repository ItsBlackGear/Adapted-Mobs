package com.cf28.adaptedmobs.common.util;

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
}
