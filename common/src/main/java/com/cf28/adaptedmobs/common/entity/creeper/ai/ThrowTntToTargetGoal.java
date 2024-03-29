package com.cf28.adaptedmobs.common.entity.creeper.ai;

import com.cf28.adaptedmobs.common.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class ThrowTntToTargetGoal extends Goal {
    private static final int ATTACK_COOLDOWN = 60;
    private final TamableCreeper mob;
    private LivingEntity target;
    private int attackCooldown;

    public ThrowTntToTargetGoal(TamableCreeper mob, IntProvider cooldown) {
        this.mob = mob;
        this.attackCooldown = cooldown.sample(mob.level.random);
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target == null) {
            return false;
        } else if (!this.target.isAlive()) {
            return false;
        } else {
            return this.mob.distanceTo(this.target) < 144D && this.mob.hasLineOfSight(this.target);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void stop() {
        this.target = null;
        this.attackCooldown = 0;
    }

    @Override
    public void tick() {
        --this.attackCooldown;

        if (this.target != null && this.attackCooldown == 7) {
            this.mob.setState(CreeperState.ATTACKING);
        }

        if (this.target != null && this.attackCooldown <= 0) {
            if (!this.mob.level.isClientSide && this.attackCooldown == 0) {
                PrimedFestiveTnt tnt = new PrimedFestiveTnt(this.mob.level, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob);
                tnt.setOwner(this.mob);
                tnt.setFuse(30);
                tnt.setCharged(this.mob.isPowered());
                tnt.setDeltaMovement((this.target.getX() - tnt.getX()) / 18D, (this.target.getY() - tnt.getY()) / 18D + 0.5D, (this.target.getZ() - tnt.getZ()) / 18D);
                this.mob.level.addFreshEntity(tnt);
            }

            this.mob.setState(CreeperState.IDLING);
            this.attackCooldown = ATTACK_COOLDOWN;
        }
    }
}