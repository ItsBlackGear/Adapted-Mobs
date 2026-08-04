package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HarpySwoopGoal extends Goal {
    private static final int PREP_TICKS = 20;
    private static final int SWOOP_TICKS = 30;
    private static final int RECOVER_TICKS = 20;
    private static final double SWOOP_SPEED = 0.45D;
    private static final double SWOOP_ACCELERATION = 0.12D;
    private static final double SWOOP_DRAG = 0.85D;
    private static final double OVERSHOOT = 2.5D;
    private static final double MIN_HEIGHT_ABOVE_TARGET = 3.0D;
    private static final double MIN_RANGE = 5.0D;
    private static final double MAX_RANGE = 16.0D;
    private static final double PULL_UP_HEIGHT = 1.5D;
    private static final double TERRAIN_CLEARANCE = 1.0D;
    private final Harpy harpy;
    private LivingEntity target;
    private Phase phase;
    private int phaseTicks;
    private boolean hasHit;
    public HarpySwoopGoal(Harpy harpy) {
        this.harpy = harpy;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.harpy.getSwoopCooldown() > 0 || this.harpy.getPickupCooldown() > 0) {
            return false;
        }

        if (this.harpy.isBaby() || this.harpy.isVehicle() || this.harpy.isFleeingGolem() || !this.harpy.hasNestNearby()) {
            return false;
        }

        LivingEntity target = this.harpy.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }

        if (this.harpy.onGround() || this.harpy.getY() - target.getY() < MIN_HEIGHT_ABOVE_TARGET) {
            return false;
        }

        double horizontal = this.horizontalDistanceTo(target);
        return horizontal >= MIN_RANGE && horizontal <= MAX_RANGE;
    }

    @Override
    public void start() {
        this.target = this.harpy.getTarget();
        this.phase = Phase.PREP;
        this.phaseTicks = PREP_TICKS;
        this.hasHit = false;
        this.harpy.setAttackState(Harpy.STATE_SWOOP_PREP);
        this.harpy.getNavigation().stop();
        this.harpy.playSound(SoundEvents.PARROT_AMBIENT, 2.0F, 1.8F);
        this.harpy.playSound(SoundEvents.PHANTOM_SWOOP, 1.2F, 1.2F);
    }

    @Override
    public void tick() {
        if (this.target == null) {
            return;
        }

        this.harpy.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        this.phaseTicks--;

        switch (this.phase) {
            case PREP -> this.tickPrep();
            case SWOOP -> this.tickSwoop();
            case RECOVER -> this.tickRecover();
        }
    }

    private void tickPrep() {
        this.harpy.setNoGravity(true);
        this.harpy.setDeltaMovement(Vec3.ZERO);
        this.harpy.getNavigation().stop();

        if (this.phaseTicks <= 0) {
            this.harpy.setNoGravity(false);
            this.enterPhase(Phase.SWOOP, SWOOP_TICKS);
            this.harpy.setAttackState(Harpy.STATE_SWOOPING);
        }
    }

    private void tickSwoop() {
        Vec3 desired = this.swoopAim().subtract(this.harpy.position());
        if (desired.lengthSqr() > 1.0E-4D) {
            desired = desired.normalize();
        }

        Vec3 motion = this.harpy.getDeltaMovement().scale(SWOOP_DRAG).add(desired.scale(SWOOP_ACCELERATION));
        if (motion.length() > SWOOP_SPEED) {
            motion = motion.normalize().scale(SWOOP_SPEED);
        }

        if (motion.y < 0.0D && this.harpy.getY() - this.target.getY() < PULL_UP_HEIGHT) {
            motion = new Vec3(motion.x, 0.0D, motion.z);
        }

        this.harpy.setDeltaMovement(motion);

        if (!this.hasHit && this.harpy.getBoundingBox().inflate(0.5D).intersects(this.target.getBoundingBox())) {
            this.hasHit = true;
            this.target.hurt(this.harpy.damageSources().mobAttack(this.harpy), (float) this.harpy.getAttributeValue(Attributes.ATTACK_DAMAGE));
            this.target.knockback(0.9D, -motion.x, -motion.z);
            this.target.push(0.0D, 0.25D, 0.0D);
        }

        Vec3 toTarget = this.target.position().subtract(this.harpy.position());
        boolean pastTarget = motion.lengthSqr() > 0.04D && toTarget.dot(motion) < 0.0D;
        if (pastTarget || this.phaseTicks <= 0) {
            this.enterPhase(Phase.RECOVER, RECOVER_TICKS);
            this.harpy.setAttackState(Harpy.STATE_NONE);
        }
    }

    private void tickRecover() {
        Vec3 away = this.harpy.position().subtract(this.target.position());
        Vec3 heading = new Vec3(away.x, 0.0D, away.z);
        if (heading.lengthSqr() < 1.0E-4D) {
            Vec3 look = this.harpy.getLookAngle();
            heading = new Vec3(look.x, 0.0D, look.z);
        }

        Vec3 destination = this.target.position().add(heading.normalize().scale(12.0D)).add(0.0D, 6.0D, 0.0D);
        this.harpy.getMoveControl().setWantedPosition(destination.x, destination.y, destination.z, 1.2D);
    }

    private Vec3 swoopAim() {
        Vec3 targetPos = this.target.position();
        Vec3 approach = targetPos.subtract(this.harpy.position());
        Vec3 heading = new Vec3(approach.x, 0.0D, approach.z);
        if (heading.lengthSqr() < 1.0E-4D) {
            Vec3 look = this.harpy.getLookAngle();
            heading = new Vec3(look.x, 0.0D, look.z);
        }

        Vec3 aim = heading.lengthSqr() < 1.0E-4D ? targetPos : targetPos.add(heading.normalize().scale(OVERSHOOT));
        return new Vec3(aim.x, Math.max(aim.y + 0.2D, this.groundHeightAt(aim) + TERRAIN_CLEARANCE), aim.z);
    }

    private double groundHeightAt(Vec3 pos) {
        return this.harpy.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor(pos.x), Mth.floor(pos.z));
    }

    private void enterPhase(Phase phase, int ticks) {
        this.phase = phase;
        this.phaseTicks = ticks;
    }

    private double horizontalDistanceTo(LivingEntity target) {
        double dx = target.getX() - this.harpy.getX();
        double dz = target.getZ() - this.harpy.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && this.target.isAlive() && !this.harpy.isVehicle()
                && (this.phase != Phase.RECOVER || this.phaseTicks > 0);
    }

    @Override
    public void stop() {
        this.harpy.setAttackState(Harpy.STATE_NONE);
        this.harpy.setNoGravity(false);
        this.harpy.setSwoopCooldown(80 + this.harpy.getRandom().nextInt(60));
        this.harpy.setPickupCooldown(40);

        if (this.harpy.onGround()) {
            this.harpy.setDeltaMovement(this.harpy.getDeltaMovement().add(0.0D, 0.35D, 0.0D));
        }

        this.target = null;
        this.hasHit = false;
    }

    private enum Phase {
        PREP,
        SWOOP,
        RECOVER
    }
}
