package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Entombed;
import net.minecraft.core.BlockPos;
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
        if (this.mob.getTarget() != null) {
            return false;
        }
        if (!this.mob.isLit()) {
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
        return !this.mob.getNavigation().isDone() && this.mob.isLit();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
    }

    private Vec3 findDarkPos() {
        BlockPos mobPos = this.mob.blockPosition();
        for (int i = 0; i < 16; i++) {
            Vec3 randomPos = DefaultRandomPos.getPos(this.mob, 10, 4);
            if (randomPos != null) {
                BlockPos pos = BlockPos.containing(randomPos);
                if (this.mob.getLightLevelAt(pos) <= Entombed.MAX_COMFORT_LIGHT) {
                    return randomPos;
                }
            }
        }

        for (int dx = -8; dx <= 8; dx += 2) {
            for (int dz = -8; dz <= 8; dz += 2) {
                for (int dy = -2; dy <= 2; dy++) {
                    BlockPos candidate = mobPos.offset(dx, dy, dz);
                    if (this.mob.level().getBlockState(candidate).isAir()
                            && this.mob.getLightLevelAt(candidate) <= Entombed.MAX_COMFORT_LIGHT
                            && this.mob.level().getBlockState(candidate.below()).isSolidRender(this.mob.level(), candidate.below())) {
                        return Vec3.atBottomCenterOf(candidate);
                    }
                }
            }
        }
        return null;
    }
}
