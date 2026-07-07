package com.cf28.adaptedmobs.common.level.entity.ai.goal;

import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CreeperSitWhenOrderedToGoal extends Goal {
    private final TamableCreeper mob;

    public CreeperSitWhenOrderedToGoal(TamableCreeper mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isOrderedToSit();
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isTame() || this.mob.isInWaterOrBubble() || !this.mob.onGround()) {
            return false;
        } else {
            LivingEntity owner = this.mob.getOwner();
            if (owner == null) {
                return true;
            } else {
                return (this.mob.distanceToSqr(owner) >= 144.0 || owner.getLastHurtByMob() == null) && this.mob.isOrderedToSit();
            }
        }
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
        this.mob.setInSittingPose(true);
    }

    @Override
    public void stop() {
        this.mob.setInSittingPose(false);
    }
}