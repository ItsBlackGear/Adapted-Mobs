package com.cf28.adaptedmobs.common.level.entity.ai.mindmould;

import com.cf28.adaptedmobs.common.level.entity.ai.pathfinding.move_control.MindmouldMoveControl;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Map;

public class MindmouldBehaviors {
    public static BehaviorControl<Mindmould> disguiseBehavior() {
        return BehaviorBuilder.triggerIf((ServerLevel level, Mindmould mindmould) -> {
            Brain<Mindmould> brain = mindmould.getBrain();

            BlockState attemptDisguise = Mindmould.attemptDisguise(level, mindmould);
            if (attemptDisguise == null) return false;

            boolean validDisguise = mindmould.isDisguised()
                && attemptDisguise.is(mindmould.getDisguiseBlock().getBlock());

            if (validDisguise || brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)
            || brain.hasMemoryValue(MemoryModuleType.WALK_TARGET)
            || brain.hasMemoryValue(MemoryModuleType.PATH)) return false;

            if (!mindmould.onGround()) return false;

            if (mindmould.getGroundTicks() >= 10) {
                float yaw = Mth.wrapDegrees(mindmould.getYRot());
                float snappedYaw = Mth.wrapDegrees(Math.round(yaw / 90.0f) * 90.0f);
                if (mindmould.getMoveControl() instanceof MindmouldMoveControl moveControl)
                    moveControl.setDirection(snappedYaw);

                if (Math.abs(yaw - snappedYaw) < 5f) {
                    mindmould.getNavigation().stop();
                    Vec3 newPosition = Vec3.atBottomCenterOf(mindmould.blockPosition());

                    // minimould
                    float cornerAdjustment = (1f / 4f);
                    if (mindmould.isBaby()) {
                        newPosition = new Vec3(
                            Mth.floor(mindmould.getX() * 2.0) / 2f + cornerAdjustment, newPosition.y,
                            Mth.floor(mindmould.getZ() * 2.0) / 2f + cornerAdjustment
                        );
                    }

                    double downwards = mindmould.getDeltaMovement().y;
                    Vec3 approachSpeed = newPosition.subtract(mindmould.position())
                        .multiply(new Vec3(new Vector3f(0.175f)));

                    double jumpHeight = Math.min(approachSpeed.length() * 3f, .33f);
                    jumpHeight = jumpHeight < .15f ? downwards : Math.max(jumpHeight, .15f);
                    approachSpeed = new Vec3(approachSpeed.x, jumpHeight, approachSpeed.z);
//                    approachSpeed = approachSpeed.add(0, -1.0f, 0);
                    if (!mindmould.isDisguised()) mindmould.setDeltaMovement(approachSpeed);
                    if (approachSpeed.horizontalDistanceSqr() <= 0.0001f || mindmould.isDisguised()) {
                        if (!mindmould.isDisguised()) {
                            mindmould.setPos(newPosition);
                            Vec3 movement = mindmould.getDeltaMovement().subtract(
                                mindmould.getDeltaMovement().x(), 0, mindmould.getDeltaMovement().z());
                            mindmould.setDeltaMovement(movement);
                        }
                        mindmould.setDisguiseBlock(attemptDisguise);
                        brain.setActiveActivityIfPossible(Activity.REST);
                    }
                }
            }
            return true;
        });
    }

    public static class FindDisguiseGround extends Behavior<Mindmould> {
        private final int range;
        private final float speedModifier;
        private long nextSearchTime;

        public FindDisguiseGround(int range, float speedModifier) {
            super(Map.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT
            ));

            this.range = range;
            this.speedModifier = speedModifier;
        }

        @Override protected boolean checkExtraStartConditions(@NotNull ServerLevel level, Mindmould mindmould) {
            return !mindmould.isDisguised() && mindmould.onGround()
                && level.getGameTime() >= this.nextSearchTime
                && (Mindmould.attemptDisguise(level, mindmould) == null);
        }

        private static final long SEARCH_INTERVAL = 40L;
        @Override protected void start(
            @NotNull ServerLevel level,
            @NotNull Mindmould mindmould,
            long gameTime
        ) {
            this.nextSearchTime = gameTime + SEARCH_INTERVAL;
            Vec3 destination = LandRandomPos.getPos(
                mindmould, this.range, this.range,
                pos -> (Mindmould.attemptDisguise(level, mindmould) != null)
                    ? mindmould.getWalkTargetValue(pos) : Double.NEGATIVE_INFINITY
            );

            if (destination != null) mindmould.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(destination, this.speedModifier, 0)
            );
        }
    }
}
