package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class HarpyLiftGoal extends MeleeAttackGoal {
    private final Harpy harpy;

    public HarpyLiftGoal(Harpy harpy, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(harpy, speedModifier, followingTargetEvenIfNotSeen);
        this.harpy = harpy;
    }

    @Override
    public boolean canUse() {
        if (this.harpy.isBaby() || this.harpy.isVehicle() || this.harpy.isFleeingGolem()) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.harpy.isBaby() || this.harpy.isVehicle() || this.harpy.isFleeingGolem()) {
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        double attackReachSqr = this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth();
        if (this.mob.distanceToSqr(target) <= attackReachSqr && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            if (!target.isVehicle() && !target.isPassenger() && this.mob.level().canSeeSky(this.mob.blockPosition())) {
                target.startRiding(this.mob, true);
            } else {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(target);
            }
        }
    }
}
