package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HarpyDashGoal extends Goal {
    private final Harpy harpy;
    private LivingEntity target;
    private int prepTicks;
    private int dashTicks;
    private int cooldownTicks;
    private Vec3 dashDir;

    public HarpyDashGoal(Harpy harpy) {
        this.harpy = harpy;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
            return false;
        }

        if (this.harpy.getPickupCooldown() > 0) {
            return false;
        }

        LivingEntity target = this.harpy.getTarget();
        if (target == null || !target.isAlive() || this.harpy.isBaby() || this.harpy.isVehicle()) {
            return false;
        }

        double distSqr = this.harpy.distanceToSqr(target);
        if (distSqr > 256.0D || distSqr < 4.0D) {
            return false;
        }

        return this.harpy.getRandom().nextInt(5) == 0;
    }

    @Override
    public void start() {
        this.target = this.harpy.getTarget();
        this.prepTicks = 15;
        this.dashTicks = 8;
        this.harpy.setDashing(true);
        this.harpy.playSound(SoundEvents.PARROT_AMBIENT, 2.0F, 1.8F);
        this.harpy.playSound(SoundEvents.PHANTOM_SWOOP, 1.2F, 1.2F);
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        this.harpy.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (this.prepTicks > 0) {
            this.prepTicks--;
            this.harpy.setDeltaMovement(this.harpy.getDeltaMovement().multiply(0.5, 0.5, 0.5));
            if (this.prepTicks == 0) {
                Vec3 targetPos = this.target.getEyePosition();
                Vec3 harpyPos = this.harpy.position();
                this.dashDir = targetPos.subtract(harpyPos).normalize();
            }
        } else if (this.dashTicks > 0) {
            this.dashTicks--;
            if (this.dashDir != null) {
                this.harpy.setDeltaMovement(this.dashDir.scale(0.8D));
            }

            if (this.harpy.getBoundingBox().inflate(0.5).intersects(this.target.getBoundingBox())) {
                this.target.hurt(this.harpy.damageSources().mobAttack(this.harpy), 4.0F);
                Vec3 kb = this.dashDir != null ? this.dashDir : this.harpy.getLookAngle();
                this.target.knockback(1.2, -kb.x, -kb.z);
                this.dashTicks = 0;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return (this.prepTicks > 0 || this.dashTicks > 0) && this.target != null && this.target.isAlive();
    }

    @Override
    public void stop() {
        this.harpy.setDashing(false);
        this.harpy.setPickupCooldown(100);
        this.target = null;
        this.cooldownTicks = 140 + this.harpy.getRandom().nextInt(60);
    }
}
