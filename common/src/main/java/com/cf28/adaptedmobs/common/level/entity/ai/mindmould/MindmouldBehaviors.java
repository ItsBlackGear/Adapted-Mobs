package com.cf28.adaptedmobs.common.level.entity.ai.mindmould;

import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MindmouldBehaviors {
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
            return !mindmould.isDisguised() && mindmould.onGround() && level.getGameTime() >= this.nextSearchTime
                && !(Mindmould.isValidDisguiseBlock(level, mindmould.attemptDisguiseBlockPosition())
                && canAlign(level, mindmould, mindmould.blockPosition()));
        }

        private static final long SEARCH_INTERVAL = 40L;
        @Override protected void start(
            @NotNull ServerLevel level,
            @NotNull Mindmould mindmould,
            long gameTime
        ) {
            this.nextSearchTime = gameTime + SEARCH_INTERVAL;
            Vec3 destination = LandRandomPos.getPos(
                mindmould, this.range, this.range, pos
                    -> Mindmould.isValidDisguiseBlock(level, pos.below()) // TODO sable stuff sometime maybe
                    && (!pos.equals(mindmould.blockPosition()) || canAlign(level, mindmould, pos))
                    ? mindmould.getWalkTargetValue(pos) : Double.NEGATIVE_INFINITY
            );

            if (destination != null) mindmould.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(destination, this.speedModifier, 0)
            );
        }
    }

    private static boolean canAlign(ServerLevel level, Mindmould mindmould, BlockPos feetPosition) {
        Vec3 center = Vec3.atBottomCenterOf(feetPosition);
        AABB bounds = mindmould.getBoundingBox().expandTowards(center.subtract(mindmould.position()));
        return level.noCollision(mindmould, bounds);
    }
}
