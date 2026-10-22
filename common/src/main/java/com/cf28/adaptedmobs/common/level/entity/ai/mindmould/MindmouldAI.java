package com.cf28.adaptedmobs.common.level.entity.ai.mindmould;

import com.cf28.adaptedmobs.common.level.entity.mob.Mindmould;
import com.cf28.adaptedmobs.core.tags.AMEntityTags;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MindmouldAI {

    private static final ImmutableList<SensorType<? extends Sensor<? super Mindmould>>> SENSOR_TYPES = ImmutableList.of(
        SensorType.NEAREST_PLAYERS,
        SensorType.NEAREST_LIVING_ENTITIES,
        SensorType.IS_IN_WATER,
        SensorType.HURT_BY
    );

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
        MemoryModuleType.IS_IN_WATER,
        MemoryModuleType.HURT_BY_ENTITY,
        MemoryModuleType.NEAREST_PLAYERS,
        MemoryModuleType.NEAREST_VISIBLE_PLAYER,
        MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
        MemoryModuleType.NEAREST_LIVING_ENTITIES,
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        MemoryModuleType.ATTACK_TARGET,
        MemoryModuleType.ATTACK_COOLING_DOWN,
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.PATH,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE
    );

    public static Brain.Provider<Mindmould> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<Mindmould> brain) {
        brain.addActivity(
            Activity.CORE,
            0,
            ImmutableList.of(
                BehaviorBuilder.<Mindmould>triggerIf(mindmould -> {
                    if (!mindmould.isDisguised() || !mindmould.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
                        return false;

                    mindmould.clearDisguise();
                    mindmould.getNavigation().stop();
                    mindmould.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
                    return true;
                }),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink()
            )
        );

        brain.addActivity(
            Activity.IDLE,
            0,
            ImmutableList.of(
                StartAttacking.<Mindmould>create(MindmouldAI::findAttackTarget),
                TryFindLand.create(8, 1.0F),
                new GateBehavior<Mindmould>(
                    Map.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT),
                    Set.of(),
                    GateBehavior.OrderPolicy.ORDERED,
                    GateBehavior.RunningPolicy.TRY_ALL,
                    ImmutableList.of(
                        Pair.of(new MindmouldBehaviors.FindDisguiseGround(8, 1.0F), 1),
                        Pair.of(MindmouldBehaviors.disguiseBehavior(), 1)
                    )
                )
            )
        );

        brain.addActivity(Activity.REST, 0, ImmutableList.of(
            StartAttacking.<Mindmould>create(MindmouldAI::findAttackTarget),
            MindmouldBehaviors.disguiseBehavior()
        ));

        brain.addActivityAndRemoveMemoryWhenStopped(
            Activity.FIGHT,
            0,
            ImmutableList.of(
                StopAttackingIfTargetInvalid.<Mindmould>create(),
                SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F),
                MeleeAttack.create(20)
            ),
            MemoryModuleType.ATTACK_TARGET
        );

        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static final int TARGET_RADIUS = 6;
    private static Optional<? extends LivingEntity> findAttackTarget(Mindmould mindmould) {
        Brain<Mindmould> brain = mindmould.getBrain();
        Optional<LivingEntity> attacker = brain.getMemory(MemoryModuleType.HURT_BY_ENTITY).filter(entity -> entity != mindmould)
            .filter(entity -> Sensor.isEntityAttackableIgnoringLineOfSight(mindmould, entity));
        if (attacker.isPresent()) return attacker;

        return brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).flatMap(visible
            -> visible.findClosest(entity -> {
                if (!Sensor.isEntityAttackable(mindmould, entity)) return false;
                if (entity instanceof Player player) {
                    return (!mindmould.isDisguised() || !staringAt(player, mindmould)
                    || mindmould.distanceToSqr(player) <= (TARGET_RADIUS * TARGET_RADIUS));
                }
                return !mindmould.isDisguised() && entity.getType().is(AMEntityTags.MINDMOULD_TARGETS);
            })
        );
    }

    // Enderman Code
    private static final double STARE_PRECISION = 0.5; // 0.05
    public static boolean staringAt(LivingEntity player, Mindmould mindmould) {
        Vec3 vec3 = player.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(mindmould.getX() - player.getX(), mindmould.getEyeY() - player.getEyeY(), mindmould.getZ() - player.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return (d1 > 1.0 - STARE_PRECISION / d0 && player.hasLineOfSight(mindmould));
    }

    public static void updateActivity(Mindmould mindmould) {
        Brain<Mindmould> brain = mindmould.getBrain();
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT,
            mindmould.isDisguised() ? Activity.REST : Activity.IDLE
        ));
    }
}
