package com.cf28.adaptedmobs.common.entity.creeper.ai;

import com.cf28.adaptedmobs.common.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class ThrowTntToTargetGoal extends Goal {
    private static final int ATTACK_COOLDOWN = 60;
    private static final int ATTACK_DISTANCE_SQUARED = 144;
    private static final int TNT_FUSE_TIME = 30;
    private static final double TNT_VERTICAL_OFFSET = 0.5;
    private static final int TNT_MOVEMENT_DIVISOR = 18;
    private static final int ATTACK_STATE_DELAY = 7;

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
        if (this.target == null || !this.target.canBeSeenAsEnemy()) {
            return false;
        } else {
            return this.mob.distanceTo(this.target) < ATTACK_DISTANCE_SQUARED && this.mob.hasLineOfSight(this.target);
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

        if (this.target != null && this.attackCooldown == ATTACK_STATE_DELAY) {
            this.mob.setState(CreeperState.ATTACKING);
        }

        if (this.target != null && this.attackCooldown <= 0) {
            if (!this.mob.level.isClientSide && this.attackCooldown == 0) {
                PrimedFestiveTnt tnt = new PrimedFestiveTnt(this.mob.level, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob);
                tnt.setOwner(this.mob);
                tnt.setFuse(TNT_FUSE_TIME);
                tnt.setCharged(this.mob.isPowered());
                tnt.setDeltaMovement(
                    (this.target.getX() - tnt.getX()) / TNT_MOVEMENT_DIVISOR,
                    (this.target.getY() - tnt.getY()) / TNT_MOVEMENT_DIVISOR + TNT_VERTICAL_OFFSET,
                    (this.target.getZ() - tnt.getZ()) / TNT_MOVEMENT_DIVISOR
                );
                this.mob.level.addFreshEntity(tnt);
            }

            this.mob.setState(CreeperState.IDLING);
            this.attackCooldown = ATTACK_COOLDOWN;
        }
    }
}