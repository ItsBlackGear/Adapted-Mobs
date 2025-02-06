package com.cf28.adaptedmobs.common.entity.creeper.ai;

import com.cf28.adaptedmobs.common.entity.creeper.SupportCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Enemy;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;

public class ApplyBuffsToTargetGoal extends Goal {
    private final SupportCreeper mob;
    private LivingEntity target;
    private final double range;
    private final double speed;
    private int animationTimer;
    private boolean playingAnimation;
    private final boolean boosted;

    public ApplyBuffsToTargetGoal(SupportCreeper mob, double range, double speed) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.mob = mob;
        this.range = range;
        this.speed = speed;
        this.boosted = mob.isPowered();
    }

    @Override
    public boolean canUse() {
        if (this.target == null) {
            this.target = this.findTarget();
            return this.target != null;
        }

        return this.mob.hasLineOfSight(this.target);
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && this.mob.distanceToSqr(this.target) <= this.range * this.range && !(this.target instanceof SupportCreeper) && this.mob.hasLineOfSight(this.target) && !this.mob.isOrderedToSit();
    }

    @Override
    public void start() {
        this.mob.setSupportedUUID(this.target.getUUID());
    }

    @Override
    public void stop() {
        this.target = null;
        this.mob.setSupportedUUID(null);
        this.playingAnimation = false;
        this.animationTimer = 0;
    }

    @Override
    public void tick() {
        if (this.target != null && this.target.isAlive()) {
            if (this.mob.isOrderedToSit()) {
                this.mob.getNavigation().stop();
                return;
            }

            int amplification = this.boosted ? 1 : 0;
            if (!this.target.hasEffect(MobEffects.MOVEMENT_SPEED) || !this.target.hasEffect(MobEffects.DAMAGE_BOOST)) {
                if (!this.playingAnimation) {
                    this.playingAnimation = true;
                    this.mob.setState(CreeperState.ATTACKING);
                    this.animationTimer = 20;
                }

                if (this.animationTimer == 0) {
                    this.target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, amplification), this.mob);
                    this.target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, amplification), this.mob);
                    this.mob.setState(CreeperState.IDLING);
                    this.playingAnimation = false;
                } else {
                    this.animationTimer--;
                }
            }

            if (!this.mob.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                this.mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED));
            }

            this.mob.getNavigation().moveTo(this.target, this.speed);
        }
    }

    @Nullable
    private LivingEntity findTarget() {
        return this.mob.level.getEntitiesOfClass(LivingEntity.class, this.mob.getBoundingBox().inflate(this.range, this.range / 2, this.range))
                .stream()
                .filter(target -> {
                    boolean shouldTarget;
                    if (this.mob.isTame()) {
                        shouldTarget = target == this.mob.getOwner();
                    } else if (target instanceof TamableCreeper creeper) {
                        shouldTarget = !creeper.isTame() && creeper.isAlive() && !(target instanceof SupportCreeper);
                    } else {
                        shouldTarget = target instanceof Enemy && target.isAlive();
                    }

                    return shouldTarget && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target) && !this.mob.isInSittingPose();
                })
                .min(Comparator.comparingDouble(target -> target.distanceTo(this.mob)))
                .orElse(null);
    }
}