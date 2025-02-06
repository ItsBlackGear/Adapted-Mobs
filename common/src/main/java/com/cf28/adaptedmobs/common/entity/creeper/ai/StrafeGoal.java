package com.cf28.adaptedmobs.common.entity.creeper.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class StrafeGoal extends Goal {
    private final PathfinderMob mob;
    @Nullable private LivingEntity target;
    private final double maxDistance;
    private final double speedModifier;
    private int strafingTime = -1;
    private boolean strafingClockwise;
    private boolean strafingBackwards;

    public StrafeGoal(PathfinderMob mob, double maxDistance, double speedModifier) {
        this.mob = mob;
        this.maxDistance = maxDistance;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        return this.target != null && this.target.isAlive() && this.mob.distanceTo(this.target) <= this.maxDistance && this.mob.hasLineOfSight(this.target);
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void tick() {
        if (this.target != null) {
            double distance = this.mob.distanceToSqr(this.target);

            if (distance <= this.maxDistance * this.maxDistance) {
                ++this.strafingTime;
                if (this.strafingTime >= 20) {
                    if (this.mob.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if (this.mob.getRandom().nextFloat() < 0.3) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (distance > (this.maxDistance * this.maxDistance * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if (distance < (this.maxDistance * this.maxDistance * 0.25F)) {
                        this.strafingBackwards = true;
                    }

                    this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.mob.lookAt(this.target, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
                }
            } else {
                this.mob.getNavigation().moveTo(this.target, this.speedModifier);
            }
        }
    }
}