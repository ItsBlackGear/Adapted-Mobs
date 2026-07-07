package com.cf28.adaptedmobs.common.level.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class BackUpIfTooCloseGoal extends Goal {
    private final Mob mob;
    private final int tooCloseDistance;
    private final float strafeSpeed;
    private LivingEntity target;
    
    public BackUpIfTooCloseGoal(Mob mob, int tooCloseDistance, float strafeSpeed) {
        this.mob = mob;
        this.tooCloseDistance = tooCloseDistance;
        this.strafeSpeed = strafeSpeed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        this.target = this.mob.getTarget();
        
        return this.target != null
            && this.target.isAlive()
            && this.mob.getSensing().hasLineOfSight(this.target)
            && this.target.closerThan(this.mob, this.tooCloseDistance);
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }
    
    @Override
    public void tick() {
        if (this.target == null) return;
        
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        this.mob.getMoveControl().strafe(-this.strafeSpeed, 0.0F);
        this.mob.setYRot(Mth.rotateIfNecessary(this.mob.getYRot(), this.mob.yHeadRot, 0.0F));
    }
    
    @Override
    public void stop() {
        this.target = null;
    }
    
    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}