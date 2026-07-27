package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HarpyPickupGoal extends Goal {
    private static final Map<UUID, Long> RECENTLY_DROPPED_TARGETS = new ConcurrentHashMap<>();

    private final Harpy harpy;
    private LivingEntity target;
    private int liftTicks;
    private boolean isLifting;
    private double startY;

    public HarpyPickupGoal(Harpy harpy) {
        this.harpy = harpy;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.harpy.getPickupCooldown() > 0) {
            return false;
        }

        LivingEntity target = this.harpy.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        Long cooldownEnd = RECENTLY_DROPPED_TARGETS.get(target.getUUID());
        if (cooldownEnd != null && this.harpy.level().getGameTime() < cooldownEnd) {
            return false;
        }

        if (target.isPassenger() && target.getVehicle() != this.harpy) {
            return false;
        }

        if (this.harpy.isVehicle() || this.harpy.isPassenger()) {
            return false;
        }

        return this.harpy.getRandom().nextInt(4) != 0 || this.harpy.isTame();
    }

    @Override
    public void start() {
        this.target = this.harpy.getTarget();
        this.liftTicks = 0;
        this.isLifting = false;
        this.startY = 0.0D;
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        this.harpy.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (!this.isLifting) {
            if (this.target.isPassenger() && this.target.getVehicle() != this.harpy) {
                this.target = null;
                return;
            }

            double distance = this.harpy.distanceToSqr(this.target);
            if (distance < 4.0D) {
                this.target.startRiding(this.harpy, true);
                this.isLifting = true;
                this.startY = this.harpy.getY();
                this.harpy.setNoGravity(true);
                this.harpy.setDeltaMovement(this.harpy.getDeltaMovement().x, 0.3D, this.harpy.getDeltaMovement().z);
                this.harpy.getNavigation().stop();
            } else {
                this.harpy.getNavigation().moveTo(this.target, 1.0D);
            }
        } else {
            if (this.target.getVehicle() != this.harpy) {
                this.target = null;
                return;
            }

            this.liftTicks++;
            Vec3 look = this.harpy.getLookAngle();
            Vec3 forward = new Vec3(look.x, 0.0D, look.z).normalize().scale(0.15D);
            this.harpy.setDeltaMovement(forward.x, 0.25D, forward.z);

            if (this.liftTicks > 100 || this.harpy.getY() >= this.startY + 8.0D || (this.liftTicks > 20 && this.harpy.verticalCollision)) {
                this.harpy.setNoGravity(false);
                this.harpy.setPickupCooldown(100);
                if (this.target != null) {
                    RECENTLY_DROPPED_TARGETS.put(this.target.getUUID(), this.harpy.level().getGameTime() + 80L);
                    this.target.stopRiding();
                }
                this.target = null;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.target == null || !this.target.isAlive()) {
            return false;
        }
        if (this.isLifting && this.target.getVehicle() != this.harpy) {
            return false;
        }
        return this.isLifting || this.harpy.getTarget() != null;
    }

    @Override
    public void stop() {
        this.harpy.setNoGravity(false);
        if (this.target != null) {
            if (this.target.getVehicle() == this.harpy) {
                this.target.stopRiding();
            }
            if (this.isLifting) {
                RECENTLY_DROPPED_TARGETS.put(this.target.getUUID(), this.harpy.level().getGameTime() + 80L);
                this.harpy.setPickupCooldown(100);
            }
        }
        this.target = null;
        this.isLifting = false;
        this.liftTicks = 0;
        this.startY = 0.0D;
    }
}
