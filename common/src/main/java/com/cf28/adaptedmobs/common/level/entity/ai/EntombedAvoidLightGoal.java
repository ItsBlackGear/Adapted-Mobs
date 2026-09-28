package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.integrations.LambDynLightsCompat;
import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntombedAvoidLightGoal extends Goal {
    private final Entombed mob;
    private final double speedModifier;
    private double posX;
    private double posY;
    private double posZ;

    public EntombedAvoidLightGoal(Entombed mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    private int recheckTicks;

    public boolean isSafePos(BlockPos pos) {
        if (this.mob.isPosLit(pos) || this.mob.isPosLit(pos.above())) {
            return false;
        }
        if (this.mob.isPositionInPlayerLight(pos)) {
            return false;
        }
        LivingEntity target = this.mob.getTarget();
        if (target != null && this.mob.isPositionInTargetLight(pos, target)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canUse() {
        BlockPos currentPos = this.mob.blockPosition();
        BlockPos eyePos = BlockPos.containing(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        if (this.isSafePos(currentPos) && this.isSafePos(eyePos)) {
            return false;
        }
        Vec3 hidePos = this.findDarkPos();
        if (hidePos == null) {
            return false;
        }
        this.posX = hidePos.x;
        this.posY = hidePos.y;
        this.posZ = hidePos.z;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        BlockPos currentPos = this.mob.blockPosition();
        BlockPos eyePos = BlockPos.containing(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        return !this.isSafePos(currentPos) || !this.isSafePos(eyePos);
    }

    @Override
    public void start() {
        this.recheckTicks = 0;
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.recheckTicks++;
        if (this.recheckTicks >= 10 || this.mob.getNavigation().isDone()) {
            this.recheckTicks = 0;
            BlockPos currentPos = this.mob.blockPosition();
            BlockPos eyePos = BlockPos.containing(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            if (!this.isSafePos(currentPos) || !this.isSafePos(eyePos)) {
                Vec3 hidePos = this.findDarkPos();
                if (hidePos != null) {
                    this.posX = hidePos.x;
                    this.posY = hidePos.y;
                    this.posZ = hidePos.z;
                    this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
                }
            }
        }
    }

    private Vec3 findDarkPos() {
        BlockPos mobPos = this.mob.blockPosition();
        BlockPos mobEyePos = BlockPos.containing(this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        int currentLight = Math.max(this.mob.getLightLevelAt(mobPos), this.mob.getLightLevelAt(mobEyePos));

        Player lightingPlayer = null;
        if (LambDynLightsCompat.isLoaded()) {
            lightingPlayer = this.mob.getNearestLightingPlayer(16.0D);
        }

        BlockPos bestSafePos = null;
        double bestSafeScore = Double.MAX_VALUE;

        BlockPos bestDarkerPos = null;
        int lowestDarkerLight = currentLight;
        double bestDarkerScore = Double.MAX_VALUE;

        LivingEntity target = this.mob.getTarget();
        Vec3 targetPos = target != null ? target.position() : null;

        for (int dx = -14; dx <= 14; dx += 2) {
            for (int dz = -14; dz <= 14; dz += 2) {
                for (int dy = -4; dy <= 4; dy += 2) {
                    BlockPos candidate = mobPos.offset(dx, dy, dz);
                    if (!this.isValidStandable(candidate)) {
                        continue;
                    }

                    double distSq = mobPos.distSqr(candidate);
                    boolean isSafe = this.isSafePos(candidate);

                    if (isSafe) {
                        double score = distSq;
                        if (lightingPlayer != null) {
                            double distToLightPlayerSq = candidate.distToCenterSqr(lightingPlayer.position());
                            score -= distToLightPlayerSq;
                        } else if (targetPos != null) {
                            double distToTargetSq = candidate.distToCenterSqr(targetPos);
                            score -= distToTargetSq * 0.25;
                        }
                        if (score < bestSafeScore) {
                            bestSafeScore = score;
                            bestSafePos = candidate;
                        }
                    } else if (bestSafePos == null) {
                        int candidateLight = Math.max(this.mob.getLightLevelAt(candidate), this.mob.getLightLevelAt(candidate.above()));
                        if (lightingPlayer != null) {
                            double distToLightPlayerSq = candidate.distToCenterSqr(lightingPlayer.position());
                            double currentDistToPlayerSq = mobPos.distToCenterSqr(lightingPlayer.position());
                            if (distToLightPlayerSq > currentDistToPlayerSq) {
                                double score = distSq - distToLightPlayerSq + candidateLight * 4.0;
                                if (score < bestDarkerScore) {
                                    lowestDarkerLight = candidateLight;
                                    bestDarkerPos = candidate;
                                    bestDarkerScore = score;
                                }
                            }
                        } else if (candidateLight < currentLight) {
                            double score = distSq + candidateLight * 4.0;
                            if (candidateLight < lowestDarkerLight || (candidateLight == lowestDarkerLight && score < bestDarkerScore)) {
                                lowestDarkerLight = candidateLight;
                                bestDarkerPos = candidate;
                                bestDarkerScore = score;
                            }
                        }
                    }
                }
            }
        }

        if (bestSafePos != null) {
            return Vec3.atBottomCenterOf(bestSafePos);
        }

        if (bestDarkerPos != null) {
            return Vec3.atBottomCenterOf(bestDarkerPos);
        }

        return null;
    }

    private boolean isValidStandable(BlockPos pos) {
        BlockPos below = pos.below();
        BlockState stateBelow = this.mob.level().getBlockState(below);
        BlockState stateAt = this.mob.level().getBlockState(pos);
        BlockPos above = pos.above();
        BlockState stateAbove = this.mob.level().getBlockState(above);

        if (stateBelow.getCollisionShape(this.mob.level(), below).isEmpty()) {
            return false;
        }

        if (!stateAt.getCollisionShape(this.mob.level(), pos).isEmpty()) {
            return false;
        }

        return stateAbove.getCollisionShape(this.mob.level(), above).isEmpty();
    }
}
