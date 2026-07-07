package com.cf28.adaptedmobs.common.level.entity.ai.goal;

import com.cf28.adaptedmobs.common.level.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.CreeperState;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class ThrowTntToTargetGoal extends Goal {
    private static final int ATTACK_DISTANCE = 16;
    private static final int TNT_FUSE_TIME = 30;
    private static final double TNT_VERTICAL_OFFSET = 0.5;
    private static final int TNT_MOVEMENT_DIVISOR = 18;
    private static final int ATTACK_STATE_DELAY = 7;

    private final TamableCreeper mob;
    private LivingEntity target;
    private int attackCooldown;
    private final IntProvider farCooldown;
    private final IntProvider closeCooldown;

    public ThrowTntToTargetGoal(TamableCreeper mob, IntProvider farCooldown, IntProvider closeCooldown) {
        this.mob = mob;
        this.farCooldown = farCooldown;
        this.closeCooldown = closeCooldown;
    }
    
    @Override
    public void start() {
        this.setCooldown();
    }
    
    private void setCooldown() {
        this.target = this.mob.getTarget();
        if (this.target != null && this.mob.closerThan(this.target, 3.0)) {
            this.attackCooldown = this.closeCooldown.sample(this.mob.level().random);
        } else {
            this.attackCooldown = this.farCooldown.sample(this.mob.level().random);
        }
    }
    
    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        if (this.target == null || !this.target.canBeSeenAsEnemy()) {
            return false;
        } else {
            return this.mob.distanceTo(this.target) < ATTACK_DISTANCE && this.mob.hasLineOfSight(this.target);
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
            if (!this.mob.level().isClientSide && this.attackCooldown == 0) {
                PrimedFestiveTnt tnt = new PrimedFestiveTnt(this.mob.level(), this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob);
                tnt.setOwner(this.mob);
                tnt.setFuse(TNT_FUSE_TIME);
                tnt.setCharged(this.mob.isPowered());
                tnt.setDeltaMovement(
                    (this.target.getX() - tnt.getX()) / TNT_MOVEMENT_DIVISOR,
                    (this.target.getY() - tnt.getY()) / TNT_MOVEMENT_DIVISOR + TNT_VERTICAL_OFFSET,
                    (this.target.getZ() - tnt.getZ()) / TNT_MOVEMENT_DIVISOR
                );
                
                this.mob.level().addFreshEntity(tnt);
            }

            this.mob.setState(CreeperState.IDLING);
            
            setCooldown();
        }
    }
}