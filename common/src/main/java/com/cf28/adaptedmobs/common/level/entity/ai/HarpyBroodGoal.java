package com.cf28.adaptedmobs.common.level.entity.ai;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.common.registries.AMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HarpyBroodGoal extends Goal {
    private static final double PLAYER_SPOT_RANGE = 16.0D;
    private static final double PLAYER_FLUSH_RANGE = 8.0D;
    private static final int SEARCH_HORIZONTAL = 10;
    private static final int SEARCH_VERTICAL = 4;
    private static final int SEARCH_COOLDOWN = 40;
    private static final double APPROACH_RANGE = 1.5D;
    private static final double PERCHED_RANGE = 0.4D;
    private static final double SETTLE_SPEED = 0.08D;
    private static final double APPROACH_SPEED = 1.0D;

    private final Harpy harpy;
    private BlockPos egg;
    private int searchCooldown;

    public HarpyBroodGoal(Harpy harpy) {
        this.harpy = harpy;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (!this.canBrood(PLAYER_SPOT_RANGE)) {
            return false;
        }

        if (this.searchCooldown > 0) {
            this.searchCooldown--;
            return false;
        }

        this.searchCooldown = SEARCH_COOLDOWN;
        this.egg = this.findEgg();
        return this.egg != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.egg != null && this.isEgg(this.egg) && this.canBrood(PLAYER_FLUSH_RANGE);
    }

    @Override
    public void start() {
        this.harpy.getNavigation().stop();
    }

    @Override
    public void tick() {
        Vec3 perch = Vec3.atBottomCenterOf(this.egg.above());
        this.harpy.getLookControl().setLookAt(perch.x, perch.y, perch.z);

        double dx = perch.x - this.harpy.getX();
        double dz = perch.z - this.harpy.getZ();
        double distance = Math.sqrt(dx * dx + dz * dz);

        if (distance > APPROACH_RANGE) {
            this.harpy.setInSittingPose(false);
            this.harpy.getMoveControl().setWantedPosition(perch.x, perch.y, perch.z, APPROACH_SPEED);
            return;
        }

        this.harpy.setNoGravity(false);
        this.harpy.getNavigation().stop();

        Vec3 motion = this.harpy.getDeltaMovement();
        if (distance > PERCHED_RANGE) {
            this.harpy.setDeltaMovement(dx / distance * SETTLE_SPEED, motion.y, dz / distance * SETTLE_SPEED);
        } else {
            this.harpy.setDeltaMovement(0.0D, motion.y, 0.0D);
            this.harpy.setInSittingPose(true);
        }
    }

    @Override
    public void stop() {
        this.harpy.setInSittingPose(false);
        this.harpy.setNoGravity(false);
        this.egg = null;
    }

    private boolean canBrood(double playerRange) {
        if (this.harpy.isBaby() || this.harpy.isTame() || this.harpy.isPassenger() || this.harpy.isVehicle()) {
            return false;
        }

        if (this.harpy.getTarget() != null || this.harpy.isFleeingGolem()) {
            return false;
        }

        Player player = this.harpy.level().getNearestPlayer(this.harpy, playerRange);
        return player == null;
    }

    private BlockPos findEgg() {
        BlockPos origin = this.harpy.blockPosition();
        BlockPos best = null;
        double bestDistance = Double.MAX_VALUE;

        for (BlockPos candidate : BlockPos.betweenClosed(
                origin.offset(-SEARCH_HORIZONTAL, -SEARCH_VERTICAL, -SEARCH_HORIZONTAL),
                origin.offset(SEARCH_HORIZONTAL, SEARCH_VERTICAL, SEARCH_HORIZONTAL))) {
            if (!this.isEgg(candidate)) {
                continue;
            }

            double distance = candidate.distToCenterSqr(this.harpy.position());
            if (distance < bestDistance) {
                best = candidate.immutable();
                bestDistance = distance;
            }
        }

        return best;
    }

    private boolean isEgg(BlockPos pos) {
        Level level = this.harpy.level();
        return level.isLoaded(pos) && level.getBlockState(pos).is(AMBlocks.HARPY_EGG.get());
    }
}
