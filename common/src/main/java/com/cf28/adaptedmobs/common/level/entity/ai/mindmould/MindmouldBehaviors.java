package com.cf28.adaptedmobs.common.level.entity.ai.mindmould;

import com.cf28.adaptedmobs.common.level.entity.ai.pathfinding.move_control.MindmouldMoveControl;
import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Optional;

public class MindmouldBehaviors {
    public static BehaviorControl<Mindmould> disguiseBehavior() {
        return BehaviorBuilder.triggerIf((ServerLevel level, Mindmould mindmould) -> {
            Brain<Mindmould> brain = mindmould.getBrain();
            if (mindmould.isDummy()) return false;
            if (mindmould.getMergePartner() != null) return true;

            BlockState attemptDisguise = Mindmould.attemptDisguise(
                level, mindmould, mindmould.attemptDisguiseBlockPosition()
            );
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
            return !mindmould.isDisguised() && mindmould.onGround() && level.getGameTime() >= this.nextSearchTime
                && (Mindmould.attemptDisguise(level, mindmould, mindmould.attemptDisguiseBlockPosition()) == null);
        }

        private static final long SEARCH_INTERVAL = 40L;
        @Override protected void start(
            @NotNull ServerLevel level,
            @NotNull Mindmould mindmould,
            long gameTime
        ) {
            BlockPos current = mindmould.blockPosition();
            Vec3 destination = LandRandomPos.getPos(
                mindmould, this.range, this.range,
                pos -> !pos.equals(current)
                    && Mindmould.canDisguiseOn(level, mindmould, pos.below())
                    ? -pos.distSqr(current) : Double.NEGATIVE_INFINITY
            );

            if (destination == null) return;
            
            this.nextSearchTime = gameTime + SEARCH_INTERVAL;
            mindmould.getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                new WalkTarget(destination, this.speedModifier, 0)
            );
        }
    }

    public static class LeapOntoTarget extends Behavior<Mindmould> {
        private static final double LEAP_RANGE = 6.0;
        private static final double LEAP_VELOCITY = 0.6;

        private int leapCooldown = 0;

        public LeapOntoTarget() {
            super(
                Map.of(
                    MemoryModuleType.ATTACK_TARGET,
                    MemoryStatus.VALUE_PRESENT
                ), 60
            );
        }

        private boolean canRidePlayer(Mindmould mindmould, Player player) { return !mindmould.isBaby() && player.getPassengers().isEmpty(); }

        @Override protected boolean checkExtraStartConditions(@NotNull ServerLevel level, Mindmould mindmould) {
            boolean shouldLeap = mindmould.onGround() && !mindmould.isPassenger() && !mindmould.isInWater()
                && mindmould.getTargetPlayer().filter(player -> this.canRidePlayer(mindmould, player)
                    && player.position().subtract(mindmould.position()).horizontalDistance() <= LEAP_RANGE)
                .isPresent();
            if (shouldLeap && leapCooldown > 0) leapCooldown--;
            return (shouldLeap && leapCooldown <= 0);
        }

        @Override protected void start(@NotNull ServerLevel level, @NotNull Mindmould mindmould, long gameTime) {
            leapCooldown = 60;
            mindmould.getNavigation().stop();

            Vec3 playerDistance = mindmould.getTargetPlayer().map(player
                    -> player.position().subtract(mindmould.position())
                ).orElse(Vec3.ZERO).multiply(1f, 0f, 1f);
            Vec3 horizontalLeap = playerDistance.scale(0.15f);
            mindmould.setDeltaMovement(
                horizontalLeap.x, LEAP_VELOCITY
                + (playerDistance.length() * 0.05),
                horizontalLeap.z
            );
        }

        @Override protected boolean canStillUse(@NotNull ServerLevel level, @NotNull Mindmould mindmould, long gameTime) {
            return (!mindmould.onGround() || mindmould.getDeltaMovement().y > 0)
                && !mindmould.isPassenger() && mindmould.getTargetPlayer().isPresent();
        }

        @Override protected void tick(@NotNull ServerLevel level, @NotNull Mindmould mindmould, long gameTime) {
            mindmould.getTargetPlayer().ifPresent(player -> {
                Vec3 deltaMovement = mindmould.getDeltaMovement();
                Vec3 toHead = player.position().subtract(mindmould.position()).multiply(0.2, 0, 0.2);
                mindmould.setDeltaMovement(toHead.x, deltaMovement.y, toHead.z);

                if (deltaMovement.y <= 0 && mindmould.getY() >= player.getEyeY()
                    && mindmould.getBoundingBox().intersects(
                        player.getBoundingBox().inflate(0, 0.5, 0)
                    ) && this.canRidePlayer(mindmould, player)
                ) mindmould.startRiding(player, true);
            });
        }
    }

    public static class MinimouldMerge extends Behavior<Mindmould> {
        private static final double SYNC_DISTANCE = 1.5;
        private static final int SYNC_INTERVAL = 20;
        private boolean jumped;

        public MinimouldMerge() {
            super(Map.of(
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                MemoryStatus.VALUE_PRESENT
            ), 200);
        }

        private Optional<LivingEntity> getPotentialPartner(Mindmould mindmould) {
            return mindmould.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).flatMap(visible
                -> visible.findClosest(entity -> entity instanceof Mindmould other && other.isBaby()
                    && other.getMergeCooldown() <= 0 && !other.isPassenger()
                    && (other.getMergePartner() == null || other.getMergePartner() == mindmould))
            );
        }

        @Override protected boolean checkExtraStartConditions(@NotNull ServerLevel level, Mindmould mindmould) {
            return mindmould.isBaby() && mindmould.getMergeCooldown() <= 0
                && !mindmould.isPassenger() && this.getPotentialPartner(mindmould).isPresent();
        }

        @Override protected void start(@NotNull ServerLevel level, @NotNull Mindmould mindmould, long gameTime) {
            mindmould.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
            mindmould.setMergePartner((Mindmould) this.getPotentialPartner(mindmould).orElseThrow());
            mindmould.clearDisguise();
            this.jumped = false;
        }

        @Override protected boolean canStillUse(@NotNull ServerLevel level, @NotNull Mindmould mindmould, long gameTime) {
            Mindmould partner = mindmould.getMergePartner();
            return partner != null && partner.isAlive() && partner.isBaby()
                && (partner.getMergePartner() == null || partner.getMergePartner() == mindmould);
        }

        @Override protected void tick(@NotNull ServerLevel level, @NotNull Mindmould mindmould, long gameTime) {
            Mindmould partner = mindmould.getMergePartner();
            if (partner == null) return;

            boolean paired = (partner.getMergePartner() == mindmould);
            if (!this.jumped && (mindmould.distanceTo(partner) > SYNC_DISTANCE || !mindmould.onGround())) {
                BehaviorUtils.setWalkAndLookTargetMemories(mindmould, partner, 1.0F, 1);
                return;
            }
            mindmould.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            mindmould.getNavigation().stop();

            if (!paired || mindmould.getId() > partner.getId()) return;

            if (!this.jumped) {
                if (mindmould.onGround() && partner.onGround()
                    && (gameTime % SYNC_INTERVAL == 0)
                ) {
                    this.launchTowards(mindmould, partner);
                    this.launchTowards(partner, mindmould);
                    this.jumped = true;
                }
            } else if (mindmould.getBoundingBox().intersects(partner.getBoundingBox())) {
                partner.discard();
                mindmould.setBaby(false);
//                mindmould.jumpFromGround();

                level.sendParticles(ParticleTypes.EXPLOSION,
                    mindmould.getX(), mindmould.getY(),
                    mindmould.getZ(), 1, 0, 0, 0, 0
                );
            } else if (mindmould.onGround() && partner.onGround()) this.jumped = false;
        }

        private void launchTowards(Mindmould mindmould, Mindmould partner) {
            Vec3 toPartner = partner.position().subtract(mindmould.position()).multiply(0.2, 0, 0.2);
            mindmould.setDeltaMovement(toPartner.x, 0.42, toPartner.z);
        }

        @Override protected void stop(@NotNull ServerLevel level, @NotNull Mindmould mindmould, long gameTime) {
            mindmould.setMergePartner(null);
            this.jumped = false;
        }
    }
}
