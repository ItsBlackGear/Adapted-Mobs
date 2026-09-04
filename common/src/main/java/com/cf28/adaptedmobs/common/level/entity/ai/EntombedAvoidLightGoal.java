package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
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

    @Override
    public boolean canUse() {
        if (!this.mob.isLit() && !this.mob.isOnFire()) {
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
        if (this.mob.getNavigation().isDone()) {
            return false;
        }
        if (this.mob.isOnFire()) {
            return true;
        }
        if (this.mob.level().isDay() && this.mob.level().canSeeSky(this.mob.blockPosition())) {
            return true;
        }
        return this.mob.isLit();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
    }

    private Vec3 findDarkPos() {
        BlockPos mobPos = this.mob.blockPosition();
        RandomSource random = this.mob.getRandom();
        boolean currentlyInSun = this.mob.level().isDay() && this.mob.level().canSeeSky(mobPos);
        int currentLight = this.mob.getLightLevelAt(mobPos);

        for (int i = 0; i < 20; i++) {
            Vec3 randomPos = DefaultRandomPos.getPos(this.mob, 12, 6);
            if (randomPos != null) {
                BlockPos pos = BlockPos.containing(randomPos);
                if (this.mob.getLightLevelAt(pos) <= Entombed.MAX_COMFORT_LIGHT) {
                    return randomPos;
                }
            }
        }

        BlockPos bestShade = null;
        int lowestLight = currentLight;

        for (int dx = -12; dx <= 12; dx += 2) {
            for (int dz = -12; dz <= 12; dz += 2) {
                for (int dy = -4; dy <= 4; dy++) {
                    BlockPos candidate = mobPos.offset(dx, dy, dz);
                    if (this.isValidStandable(candidate)) {
                        int candidateLight = this.mob.getLightLevelAt(candidate);
                        if (candidateLight <= Entombed.MAX_COMFORT_LIGHT) {
                            return Vec3.atBottomCenterOf(candidate);
                        }
                        boolean shaded = !this.mob.level().canSeeSky(candidate);
                        if (currentlyInSun) {
                            if (shaded && (bestShade == null || candidateLight < lowestLight || mobPos.distSqr(candidate) < mobPos.distSqr(bestShade))) {
                                lowestLight = candidateLight;
                                bestShade = candidate;
                            }
                        } else {
                            if (shaded && candidateLight < lowestLight) {
                                lowestLight = candidateLight;
                                bestShade = candidate;
                            }
                        }
                    }
                }
            }
        }

        if (bestShade != null) {
            return Vec3.atBottomCenterOf(bestShade);
        }

        if (currentlyInSun) {
            for (int i = 0; i < 16; i++) {
                BlockPos randomCandidate = mobPos.offset(random.nextInt(20) - 10, random.nextInt(8) - 4, random.nextInt(20) - 10);
                if (this.isValidStandable(randomCandidate) && !this.mob.level().canSeeSky(randomCandidate)) {
                    return Vec3.atBottomCenterOf(randomCandidate);
                }
            }
        }

        return null;
    }

    private boolean isValidStandable(BlockPos pos) {
        return (this.mob.level().getBlockState(pos).isAir() || this.mob.level().getBlockState(pos).canBeReplaced())
                && this.mob.level().getBlockState(pos.above()).isAir()
                && this.mob.level().getBlockState(pos.below()).isSolidRender(this.mob.level(), pos.below());
    }
}
