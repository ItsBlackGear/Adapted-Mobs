package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntombedStalkTargetGoal extends Goal {
    private final Entombed mob;
    private final double speedModifier;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private BlockPos stalkPos;

    public EntombedStalkTargetGoal(Entombed mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        return !(target instanceof Player player) || (!player.isSpectator() && !player.isCreative());
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        return !(target instanceof Player player) || (!player.isSpectator() && !player.isCreative());
    }

    @Override
    public void start() {
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
        this.mob.setStalking(true);
    }

    @Override
    public void stop() {
        this.mob.setStalking(false);
        this.stalkPos = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return;
        }

        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        double distanceSq = this.mob.distanceToSqr(target);

        this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);

        boolean targetLit = this.mob.isTargetInLightOrHoldingLight(target);

        if (!targetLit) {
            this.mob.setStalking(false);

            if (this.ticksUntilNextPathRecalculation <= 0) {
                this.ticksUntilNextPathRecalculation = 10 + this.mob.getRandom().nextInt(5);
                this.mob.getNavigation().moveTo(target, this.speedModifier * 1.15);
            }

            this.checkAndPerformAttack(target, distanceSq);
        } else {
            this.mob.setStalking(true);
            this.checkAndPerformAttack(target, distanceSq);

            if (this.ticksUntilNextPathRecalculation <= 0) {
                this.ticksUntilNextPathRecalculation = 15 + this.mob.getRandom().nextInt(10);
                this.findAndMoveToStalkPosition(target);
            }
        }
    }

    private void findAndMoveToStalkPosition(LivingEntity target) {
        Vec3 mobVec = this.mob.position();
        Vec3 targetVec = target.position();
        Vec3 dir = mobVec.subtract(targetVec).normalize();

        BlockPos bestDarkPos = null;
        double bestDistSq = Double.MAX_VALUE;

        for (int dist = 5; dist <= 12; dist += 2) {
            for (int angleOffset = -45; angleOffset <= 45; angleOffset += 30) {
                double rad = Math.toRadians(angleOffset);
                double cos = Math.cos(rad);
                double sin = Math.sin(rad);
                double x = dir.x * cos - dir.z * sin;
                double z = dir.x * sin + dir.z * cos;

                Vec3 testVec = targetVec.add(x * dist, 0, z * dist);
                BlockPos candidate = BlockPos.containing(testVec);

                for (int yOffset = -2; yOffset <= 2; yOffset++) {
                    BlockPos groundCandidate = candidate.above(yOffset);
                    if (this.mob.level().getBlockState(groundCandidate).isAir()
                            && this.mob.level().getBlockState(groundCandidate.below()).isSolidRender(this.mob.level(), groundCandidate.below())
                            && !this.mob.isPositionInTargetLight(groundCandidate, target)
                            && this.mob.getLightLevelAt(groundCandidate) <= Entombed.MAX_COMFORT_LIGHT) {

                        double d = mobVec.distanceToSqr(Vec3.atBottomCenterOf(groundCandidate));
                        if (d < bestDistSq) {
                            bestDistSq = d;
                            bestDarkPos = groundCandidate;
                        }
                        break;
                    }
                }
            }
        }

        if (bestDarkPos != null) {
            this.stalkPos = bestDarkPos;
            if (this.mob.distanceToSqr(Vec3.atBottomCenterOf(bestDarkPos)) > 2.25) {
                this.mob.getNavigation().moveTo(bestDarkPos.getX() + 0.5, bestDarkPos.getY(), bestDarkPos.getZ() + 0.5, this.speedModifier);
            } else {
                this.mob.getNavigation().stop();
            }
        } else {
            if (this.mob.getLightLevelAt(this.mob.blockPosition()) <= Entombed.MAX_COMFORT_LIGHT
                    && !this.mob.isPositionInTargetLight(this.mob.blockPosition(), target)) {
                this.mob.getNavigation().stop();
            }
        }
    }

    private void checkAndPerformAttack(LivingEntity target, double distanceSq) {
        double attackReachSq = this.getAttackReachSqr(target);
        if (distanceSq <= attackReachSq && this.ticksUntilNextAttack <= 0) {
            this.ticksUntilNextAttack = 20;
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(target);
        }
    }

    protected double getAttackReachSqr(LivingEntity target) {
        return this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + target.getBbWidth();
    }
}
