package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HarpyKiteGoal extends Goal {
    private static final int MIN_DWELL_TICKS = 40;
    private static final int MAX_DWELL_TICKS = 80;
    private static final int REPATH_INTERVAL = 20;
    private static final double MIN_RETREAT = 12.0D;
    private static final double MAX_RETREAT = 16.0D;
    private static final double MIN_ALTITUDE = 6.0D;
    private static final double MAX_ALTITUDE = 10.0D;
    private static final float ORBIT_STEP = 0.35F;

    private final Harpy harpy;
    private LivingEntity target;
    private int dwellTicks;
    private int repathTicks;
    private float bearing;
    private double retreatDistance;
    private double altitude;

    public HarpyKiteGoal(Harpy harpy) {
        this.harpy = harpy;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.harpy.isBaby() || this.harpy.isVehicle()) {
            return false;
        }

        LivingEntity target = this.harpy.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        this.target = this.harpy.getTarget();
        this.dwellTicks = MIN_DWELL_TICKS + this.harpy.getRandom().nextInt(MAX_DWELL_TICKS - MIN_DWELL_TICKS);
        this.repathTicks = 0;
        this.bearing = this.harpy.getRandom().nextFloat() * (float) (Math.PI * 2.0D);
        this.retreatDistance = Mth.lerp(this.harpy.getRandom().nextDouble(), MIN_RETREAT, MAX_RETREAT);
        this.altitude = Mth.lerp(this.harpy.getRandom().nextDouble(), MIN_ALTITUDE, MAX_ALTITUDE);
    }

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }

        this.dwellTicks--;
        this.harpy.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (this.repathTicks > 0) {
            this.repathTicks--;
            return;
        }
        this.repathTicks = REPATH_INTERVAL;

        this.bearing += ORBIT_STEP;
        double x = this.target.getX() + Mth.cos(this.bearing) * this.retreatDistance;
        double y = this.target.getY() + this.altitude;
        double z = this.target.getZ() + Mth.sin(this.bearing) * this.retreatDistance;

        if (!this.harpy.getNavigation().moveTo(x, y, z, 1.0D)) {
            this.harpy.getMoveControl().setWantedPosition(x, y, z, 1.0D);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.dwellTicks > 0 && this.target != null && this.target.isAlive() && !this.harpy.isVehicle();
    }

    @Override
    public void stop() {
        this.target = null;
        this.harpy.getNavigation().stop();
    }
}
