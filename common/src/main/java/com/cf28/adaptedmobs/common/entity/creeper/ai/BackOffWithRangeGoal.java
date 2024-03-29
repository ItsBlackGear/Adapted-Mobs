package com.cf28.adaptedmobs.common.entity.creeper.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BackOffWithRangeGoal extends Goal {
    private final PathfinderMob mob;
    @Nullable private LivingEntity target;
    private final double maxDistance;
    private final double speedModifier;

    public BackOffWithRangeGoal(PathfinderMob mob, double maxDistance, double speedModifier) {
        this.mob = mob;
        this.maxDistance = maxDistance;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target == null) {
            return false;
        } else if (!this.target.isAlive()) {
            return false;
        } else {
            return this.mob.distanceTo(this.target) <= this.maxDistance && this.mob.hasLineOfSight(this.target);
        }
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

    private double getSpeedModifier() {
        if (this.target != null && this.target instanceof Player player) {
            if (player.isSprinting()) {
                return this.speedModifier * 1.25D;
            }
        }

        return this.speedModifier;
    }

    @Override
    public void tick() {
        if (this.target != null) {
            double distance = this.mob.distanceToSqr(this.target);

            if (distance <= this.maxDistance * this.maxDistance) {
                Vec3 target = this.target.getEyePosition(1.0F);
                Vec3 source = this.mob.position();
                Vec3 diff = source.subtract(target).normalize();
                double moveDistance = this.maxDistance - Math.sqrt(distance);
                Vec3 pos = source.add(diff.scale(moveDistance));
                this.mob.getNavigation().moveTo(pos.x, pos.y, pos.z, this.getSpeedModifier());
                this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            } else {
                this.mob.getNavigation().moveTo(this.target, this.getSpeedModifier());
            }

            if (this.mob.horizontalCollision) {
                this.mob.getJumpControl().jump();
            }
        }
    }
}