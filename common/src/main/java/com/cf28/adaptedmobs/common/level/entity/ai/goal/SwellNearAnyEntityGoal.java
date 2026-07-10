package com.cf28.adaptedmobs.common.level.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Creeper;

import java.util.Comparator;
import java.util.EnumSet;

public class SwellNearAnyEntityGoal extends Goal {
    private final Creeper creeper;
    private final double range;
    private LivingEntity target;

    public SwellNearAnyEntityGoal(Creeper creeper, double range) {
        this.creeper = creeper;
        this.range = range;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.creeper.getSwellDir() > 0 || this.findNearestEntity() != null;
    }

    @Override
    public void start() {
        this.creeper.getNavigation().stop();
        this.target = this.findNearestEntity();
    }

    @Override
    public void stop() {
        this.target = null;
        this.creeper.setSwellDir(-1);
    }

    @Override
    public void tick() {
        if (this.target == null || !this.target.isAlive() || this.creeper.distanceToSqr(this.target) > this.range * this.range) {
            this.creeper.setSwellDir(-1);
        } else {
            this.creeper.setSwellDir(1);
        }
    }

    private LivingEntity findNearestEntity() {
        return this.creeper.level()
                .getEntitiesOfClass(LivingEntity.class, this.creeper.getBoundingBox().inflate(this.range), entity -> entity != this.creeper && entity.isAlive())
                .stream()
                .min(Comparator.comparingDouble(this.creeper::distanceToSqr))
                .orElse(null);
    }
}
