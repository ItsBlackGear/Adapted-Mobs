package com.cf28.adaptedmobs.common.entity.creeper.ai;

import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Enemy;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class BuffTargetGoal extends Goal {
    private final SupportCreeper mob;
    private LivingEntity target;
    private final double range;
    private final double speedModifier;

    public BuffTargetGoal(SupportCreeper mob, double range, double speedModifier) {
        this.mob = mob;
        this.range = range;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.target == null) {
            this.target = this.findTarget();
            return this.target != null;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && this.mob.distanceToSqr(this.target) <= this.range * this.range && !(this.target instanceof SupportCreeper);
    }

    @Override
    public void start() {
        this.mob.setSupportedUUID(this.target.getUUID());
    }

    @Override
    public void stop() {
        this.target = null;
        this.mob.setSupportedUUID(null);
    }

    @Override
    public void tick() {
        if (this.target != null && this.target.isAlive()) {
            if (!this.target.hasEffect(MobEffects.MOVEMENT_SPEED) && !this.target.hasEffect(MobEffects.DAMAGE_BOOST)) {
                this.target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3600), this.mob);
                this.target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3600), this.mob);
                this.mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3600));
            }

            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }
    }

    @Nullable
    private LivingEntity findTarget() {
        List<LivingEntity> targets = this.mob.level.getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate(this.range, this.range / 2, this.range), target -> {
            return target instanceof Enemy && target.isAlive() && !(target instanceof SupportCreeper);
        });
        if (!targets.isEmpty()) {
            return targets.get(0);
        }

        return null;
    }
}