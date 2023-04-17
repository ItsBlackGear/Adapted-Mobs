package com.cf28.adaptedmobs.common.entity.creeper.ai;

import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class OwnerHurtTargetGoal extends TargetGoal {
    private final TamableCreeper creeper;
    private final boolean isHurtBy;
    private LivingEntity target;
    private int timestamp;

    public OwnerHurtTargetGoal(TamableCreeper creeper, boolean isHurtBy) {
        super(creeper, false);
        this.creeper = creeper;
        this.isHurtBy = isHurtBy;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.creeper.isTame() && !this.creeper.isBaby() && !this.creeper.isOrderedToSit()) {
            LivingEntity owner = this.creeper.getOwner();
            if (owner == null) {
                return false;
            } else {
                this.target = this.isHurtBy ? owner.getLastHurtByMob() : owner.getLastHurtMob();
                int timestamp = this.isHurtBy ? owner.getLastHurtByMobTimestamp() : owner.getLastHurtMobTimestamp();
                return timestamp != this.timestamp && this.canAttack(this.target, TargetingConditions.DEFAULT) && this.creeper.wantsToAttack(this.target, owner);
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.target);
        LivingEntity owner = this.creeper.getOwner();
        if (owner != null) this.timestamp = this.isHurtBy ? owner.getLastHurtByMobTimestamp() : owner.getLastHurtMobTimestamp();
        super.start();
    }
}