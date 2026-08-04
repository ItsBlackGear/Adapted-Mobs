package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HarpyPickupGoal extends Goal {
    private static final double GRAB_RANGE_SQR = 4.0D;
    private static final double LIFT_HEIGHT = 6.0D;
    private static final int MAX_CARRY_TICKS = 90;
    private static final int RELOCATE_MAX_TICKS = 80;
    private static final int RELOCATE_SEARCH_RADIUS = 12;
    private static final double RELOCATE_SPEED = 0.22D;
    private final Harpy harpy;
    private LivingEntity target;
    private Stage stage;
    private int carryTicks;
    private int relocateTicks;
    private double startY;
    private BlockPos relocateDestination;
    public HarpyPickupGoal(Harpy harpy) {
        this.harpy = harpy;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.harpy.isBaby() || this.harpy.isFleeingGolem() || this.harpy.getPickupCooldown() > 0 || this.harpy.getRegrabCooldown() > 0) {
            return false;
        }

        LivingEntity target = this.harpy.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        if (target.isPassenger() && target.getVehicle() != this.harpy) {
            return false;
        }

        if (this.harpy.isVehicle() || this.harpy.isPassenger()) {
            return false;
        }

        return this.harpy.hasLiftRoom(target.blockPosition());
    }

    @Override
    public void start() {
        this.target = this.harpy.getTarget();
        this.stage = Stage.APPROACH;
        this.carryTicks = 0;
        this.relocateTicks = 0;
        this.startY = 0.0D;
        this.relocateDestination = null;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }

        switch (this.stage) {
            case APPROACH -> this.tickApproach();
            case ASCEND -> this.tickAscend();
            case RELOCATE -> this.tickRelocate();
        }
    }

    private void tickApproach() {
        this.harpy.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (this.target.isPassenger() && this.target.getVehicle() != this.harpy) {
            this.target = null;
            return;
        }

        if (this.harpy.distanceToSqr(this.target) >= GRAB_RANGE_SQR) {
            this.harpy.getNavigation().moveTo(this.target, 1.0D);
            return;
        }

        if (!this.harpy.hasLiftRoom(this.target.blockPosition())) {
            this.target = null;
            return;
        }

        this.harpy.startCarrying(this.target);
        this.stage = Stage.ASCEND;
        this.startY = this.harpy.getY();
        this.harpy.setNoGravity(true);
        this.harpy.setDeltaMovement(this.harpy.getDeltaMovement().x, 0.3D, this.harpy.getDeltaMovement().z);
        this.harpy.getNavigation().stop();
    }

    private void tickAscend() {
        if (!this.stillCarrying()) {
            return;
        }

        this.carryTicks++;

        double remaining = this.startY + LIFT_HEIGHT - this.harpy.getY();
        if (remaining <= 0.0D || this.carryTicks > MAX_CARRY_TICKS) {
            this.harpy.releaseCarriedTarget();
            this.target = null;
            return;
        }

        int needed = Mth.clamp(Mth.ceil(remaining), 1, Harpy.LIFT_CLEARANCE);
        if (this.harpy.verticalCollision || !this.harpy.hasClearanceAbove(this.harpy.blockPosition(), needed)) {
            this.stage = Stage.RELOCATE;
            this.relocateTicks = 0;
            this.relocateDestination = null;
            return;
        }

        Vec3 look = this.harpy.getLookAngle();
        Vec3 forward = new Vec3(look.x, 0.0D, look.z).normalize().scale(0.15D);
        this.harpy.setDeltaMovement(forward.x, 0.25D, forward.z);
    }

    private void tickRelocate() {
        if (!this.stillCarrying()) {
            return;
        }

        this.carryTicks++;
        this.relocateTicks++;

        if (this.relocateDestination == null) {
            this.relocateDestination = this.findOpenAir();
        }

        if (this.relocateDestination == null || this.relocateTicks > RELOCATE_MAX_TICKS || this.carryTicks > MAX_CARRY_TICKS) {
            this.harpy.releaseCarriedTarget();
            this.target = null;
            return;
        }

        Vec3 destination = Vec3.atCenterOf(this.relocateDestination);
        this.harpy.getLookControl().setLookAt(destination.x, this.harpy.getEyeY(), destination.z);

        Vec3 heading = new Vec3(destination.x - this.harpy.getX(), 0.0D, destination.z - this.harpy.getZ());
        if (heading.lengthSqr() < 1.0D) {
            this.stage = Stage.ASCEND;
            this.startY = this.harpy.getY();
            this.relocateDestination = null;
            return;
        }

        heading = heading.normalize().scale(RELOCATE_SPEED);
        this.harpy.setDeltaMovement(heading.x, 0.0D, heading.z);

        if (this.harpy.horizontalCollision) {
            this.relocateDestination = null;
        }
    }

    private boolean stillCarrying() {
        if (this.target.getVehicle() != this.harpy) {
            this.harpy.setNoGravity(false);
            this.target = null;
            return false;
        }
        return true;
    }

    private BlockPos findOpenAir() {
        Level level = this.harpy.level();
        BlockPos origin = this.harpy.blockPosition();
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;

        for (int dx = -RELOCATE_SEARCH_RADIUS; dx <= RELOCATE_SEARCH_RADIUS; dx++) {
            for (int dz = -RELOCATE_SEARCH_RADIUS; dz <= RELOCATE_SEARCH_RADIUS; dz++) {
                double distance = dx * dx + dz * dz;
                if (distance > RELOCATE_SEARCH_RADIUS * RELOCATE_SEARCH_RADIUS || distance >= bestDistance) {
                    continue;
                }

                BlockPos candidate = origin.offset(dx, 0, dz);
                if (!level.isLoaded(candidate) || !level.getBlockState(candidate).getCollisionShape(level, candidate).isEmpty()) {
                    continue;
                }

                if (!this.harpy.hasLiftRoom(candidate)) {
                    continue;
                }

                best = candidate;
                bestDistance = distance;
            }
        }

        return best;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.target == null || !this.target.isAlive()) {
            return false;
        }
        if (this.stage != Stage.APPROACH) {
            return this.target.getVehicle() == this.harpy;
        }
        return this.harpy.getTarget() != null;
    }

    @Override
    public void stop() {
        if (this.target != null && this.target.getVehicle() == this.harpy) {
            this.harpy.releaseCarriedTarget();
        } else {
            this.harpy.setNoGravity(false);
        }

        this.target = null;
        this.stage = Stage.APPROACH;
        this.carryTicks = 0;
        this.relocateTicks = 0;
        this.startY = 0.0D;
        this.relocateDestination = null;
    }

    private enum Stage {
        APPROACH,
        ASCEND,
        RELOCATE
    }
}
