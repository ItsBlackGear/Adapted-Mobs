package com.cf28.adaptedmobs.common.level.entity.ai.goal;

import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class CreeperFollowOwnerGoal extends Goal {
    private final TamableCreeper mob;
    private LivingEntity owner;
    private final LevelReader level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;

    public CreeperFollowOwnerGoal(TamableCreeper mob, double speedModifier, float startDistance, float stopDistance) {
        this.mob = mob;
        this.level = mob.level();
        this.speedModifier = speedModifier;
        this.navigation = mob.getNavigation();
        this.startDistance = startDistance;
        this.stopDistance = stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity owner = this.mob.getOwner();
        if (owner == null
            || owner.isSpectator()
            || this.mob.isOrderedToSit()
            || !this.mob.canFollow()
            || this.mob.distanceToSqr(owner) < (double) (this.startDistance * this.startDistance)) {
            return false;
        } else {
            this.owner = owner;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.navigation.isDone()
            || this.mob.isOrderedToSit()
            || !this.mob.canFollow()) {
            return false;
        } else {
            return this.mob.distanceToSqr(this.owner) > (double) (this.stopDistance * this.stopDistance);
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
        this.mob.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.owner, 10.0F, (float)this.mob.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (!this.mob.isLeashed() && !this.mob.isPassenger()) {
                if (this.mob.distanceToSqr(this.owner) >= 256.0D) {
                    this.teleportToOwner();
                } else {
                    this.navigation.moveTo(this.owner, this.speedModifier);
                }
            }
        }
    }

    private void teleportToOwner() {
        BlockPos pos = this.owner.blockPosition();

        for (int i = 0; i < 10; i++) {
            int x = this.randomIntInclusive(-3, 3);
            int y = this.randomIntInclusive(-1, 1);
            int z = this.randomIntInclusive(-3, 3);
            boolean shouldTeleportToPlayer = this.maybeTeleportTo(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
            if (shouldTeleportToPlayer) return;
        }
    }

    private boolean maybeTeleportTo(int x, int y, int z) {
        if (Math.abs((double) x - this.owner.getX()) < 2.0 && Math.abs((double) z - this.owner.getZ()) < 2.0) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.mob.moveTo((double) x + 0.5, y, (double) z + 0.5, this.mob.getYRot(), this.mob.getXRot());
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathType path = WalkNodeEvaluator.getPathTypeStatic(this.mob, pos.mutable());
        if (path != PathType.WALKABLE) {
            return false;
        } else {
            BlockState state = this.level.getBlockState(pos.below());
            if (state.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos current = pos.subtract(this.mob.blockPosition());
                return this.level.noCollision(this.mob, this.mob.getBoundingBox().move(current));
            }
        }
    }

    private int randomIntInclusive(int min, int max) {
        return this.mob.getRandom().nextInt(max - min + 1) + min;
    }
}