package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class EntombedStalkTargetGoal extends Goal {
    private final Entombed mob;
    private final double speedModifier;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;

    public EntombedStalkTargetGoal(Entombed mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        if (this.mob.isTargetInLightOrHoldingLight(target)) {
            return false;
        }
        return !(target instanceof Player player) || (!player.isSpectator() && !player.isCreative());
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        if (this.mob.isTargetInLightOrHoldingLight(target)) {
            return false;
        }
        return !(target instanceof Player player) || (!player.isSpectator() && !player.isCreative());
    }

    @Override
    public void start() {
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return;
        }

        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double distanceSq = this.mob.distanceToSqr(target);

        this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);

        if (this.ticksUntilNextPathRecalculation <= 0) {
            this.ticksUntilNextPathRecalculation = 10 + this.mob.getRandom().nextInt(5);
            this.mob.getNavigation().moveTo(target, this.speedModifier * 1.15);
        }

        this.checkAndPerformAttack(target, distanceSq);
    }

    private void checkAndPerformAttack(LivingEntity target, double distanceSq) {
        double attackReachSq = this.getAttackReachSqr(target);
        if (distanceSq <= attackReachSq && this.ticksUntilNextAttack <= 0) {
            this.ticksUntilNextAttack = 20;
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(target);
        }
    }

    protected double getAttackReachSqr(LivingEntity target) {
        return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth();
    }
}
