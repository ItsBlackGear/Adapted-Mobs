package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class EntombedAttackGoal extends MeleeAttackGoal {
    private final Entombed mob;
    private int raiseArmTicks;

    public EntombedAttackGoal(Entombed mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isAfraidOfLight()) {
            return false;
        }
        LivingEntity target = this.mob.getTarget();
        if (target != null && this.mob.isTargetInLightOrHoldingLight(target)) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isAfraidOfLight()) {
            return false;
        }
        LivingEntity target = this.mob.getTarget();
        if (target != null && this.mob.isTargetInLightOrHoldingLight(target)) {
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.raiseArmTicks++;
        this.mob.setAggressive(this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2);
    }
}
