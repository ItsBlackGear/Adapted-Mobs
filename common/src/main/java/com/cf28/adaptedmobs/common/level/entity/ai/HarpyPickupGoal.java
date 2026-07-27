package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HarpyPickupGoal extends Goal {
    private final Harpy harpy;
    private LivingEntity target;
    private int liftTicks;
    private boolean isLifting;

    public HarpyPickupGoal(Harpy harpy) {
        this.harpy = harpy;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.harpy.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        if (this.harpy.isBaby()) {
            return false;
        }

        return this.harpy.getRandom().nextInt(4) != 0 || this.harpy.isTame();
    }

    @Override
    public void start() {
        this.target = this.harpy.getTarget();
        this.liftTicks = 0;
        this.isLifting = false;
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        this.harpy.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (!this.isLifting) {
            double distance = this.harpy.distanceToSqr(this.target);
            if (distance < 4.0D) {
                this.target.startRiding(this.harpy, true);
                this.isLifting = true;
            } else {
                this.harpy.getNavigation().moveTo(this.target, 1.2D);
            }
        } else {
            this.liftTicks++;
            if (this.target.getVehicle() != this.harpy) {
                if (!this.harpy.isTame() && this.target instanceof Player) {
                    this.target.startRiding(this.harpy, true);
                }
            }
            
            Vec3 forward = this.harpy.getLookAngle().multiply(0.2, 0, 0.2);
            this.harpy.setDeltaMovement(forward.x, 0.3D, forward.z);
            
            if (this.liftTicks > 60 || this.harpy.getY() > this.target.getY() + 6) {
                this.target.stopRiding();
                this.target = null;
                this.harpy.setTarget(null);
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && (this.isLifting || this.harpy.getTarget() != null);
    }

    @Override
    public void stop() {
        if (this.target != null && this.target.getVehicle() == this.harpy) {
            this.target.stopRiding();
        }
        this.target = null;
        this.isLifting = false;
        this.liftTicks = 0;
    }
}
