package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class HarpyAvoidGolemGoal extends Goal {
    private static final double FEAR_RADIUS = 20.0D;
    private static final double FLEE_DISTANCE = 24.0D;
    private static final double FLEE_ALTITUDE = 10.0D;
    private static final double FLEE_SPEED = 1.6D;

    private final Harpy harpy;
    private IronGolem golem;

    public HarpyAvoidGolemGoal(Harpy harpy) {
        this.harpy = harpy;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.harpy.isBaby() || this.harpy.isTame() || this.harpy.isPassenger()) {
            return false;
        }

        this.golem = this.nearestGolem();
        return this.golem != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.golem != null && this.golem.isAlive() && this.harpy.distanceToSqr(this.golem) <= FEAR_RADIUS * FEAR_RADIUS;
    }

    @Override
    public void start() {
        this.harpy.setFleeingGolem(true);
        this.harpy.setTarget(null);
        this.harpy.setInSittingPose(false);
        this.harpy.setOrderedToSit(false);

        if (this.harpy.onGround()) {
            this.harpy.setDeltaMovement(this.harpy.getDeltaMovement().add(0.0D, 0.4D, 0.0D));
        }

        if (this.harpy.isVehicle()) {
            this.harpy.ejectPassengers();
        }
    }

    @Override
    public void tick() {
        Vec3 away = this.harpy.position().subtract(this.golem.position());
        Vec3 heading = new Vec3(away.x, 0.0D, away.z);
        if (heading.lengthSqr() < 1.0E-4D) {
            Vec3 look = this.harpy.getLookAngle();
            heading = new Vec3(look.x, 0.0D, look.z);
        }
        if (heading.lengthSqr() < 1.0E-4D) {
            heading = new Vec3(1.0D, 0.0D, 0.0D);
        }

        Vec3 destination = this.golem.position().add(heading.normalize().scale(FLEE_DISTANCE)).add(0.0D, FLEE_ALTITUDE, 0.0D);
        this.harpy.getLookControl().setLookAt(destination.x, destination.y, destination.z);

        if (!this.harpy.getNavigation().moveTo(destination.x, destination.y, destination.z, FLEE_SPEED)) {
            this.harpy.getMoveControl().setWantedPosition(destination.x, destination.y, destination.z, FLEE_SPEED);
        }

        if (this.harpy.onGround()) {
            this.harpy.setDeltaMovement(this.harpy.getDeltaMovement().add(0.0D, 0.35D, 0.0D));
        }
    }

    @Override
    public void stop() {
        this.harpy.setFleeingGolem(false);
        this.golem = null;
    }

    private IronGolem nearestGolem() {
        AABB range = this.harpy.getBoundingBox().inflate(FEAR_RADIUS);
        List<IronGolem> golems = this.harpy.level().getEntitiesOfClass(IronGolem.class, range, IronGolem::isAlive);

        IronGolem nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (IronGolem candidate : golems) {
            double distance = this.harpy.distanceToSqr(candidate);
            if (distance < nearestDistance) {
                nearest = candidate;
                nearestDistance = distance;
            }
        }

        return nearest;
    }
}
