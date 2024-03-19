package com.cf28.adaptedmobs.common.entity.errant;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

public class ErrantBrain {
    public static final ImmutableList<SensorType<? extends Sensor<? super Errant>>> SENSORS = ImmutableList.of(
        SensorType.NEAREST_LIVING_ENTITIES,
        SensorType.NEAREST_PLAYERS,
        SensorType.HURT_BY
    );
    public static final ImmutableList<MemoryModuleType<?>> MEMORIES = ImmutableList.of(
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.NEAREST_LIVING_ENTITIES,
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        MemoryModuleType.NEAREST_VISIBLE_PLAYER,
        MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
        MemoryModuleType.HURT_BY,
        MemoryModuleType.HURT_BY_ENTITY,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
        MemoryModuleType.ATTACK_TARGET,
        MemoryModuleType.ATTACK_COOLING_DOWN,
        MemoryModuleType.INTERACTION_TARGET,
        MemoryModuleType.PATH,
        MemoryModuleType.ANGRY_AT,
        MemoryModuleType.UNIVERSAL_ANGER
    );

    public static void initMemories(Errant errant, RandomSource random) {
    }

    public static Brain<?> makeBrain(Brain<Errant> brain) {
        addCoreActivity(brain);
        addIdleActivity(brain);
        addFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void addCoreActivity(Brain<Errant> brain) {
        brain.addActivity(
            Activity.CORE,
            0,
            ImmutableList.of(
                new Swim(0.8F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink()
            )
        );
    }

    private static void addIdleActivity(Brain<Errant> brain) {
        brain.addActivity(
            Activity.IDLE,
            10,
            ImmutableList.of(
                new RunOne<>(
                    ImmutableList.of(
                        Pair.of(new RandomStroll(0.6F), 2),
                        Pair.of(new DoNothing(30, 60), 1)
                    )
                )
            )
        );
    }

    private static void addFightActivity(Brain<Errant> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
            Activity.FIGHT,
            10,
            ImmutableList.of(
                new MeleeAttack(20)
            ),
            MemoryModuleType.ATTACK_TARGET
        );
    }

    public static void updateActivity(Errant errant) {
        errant.getBrain().setActiveActivityToFirstValid(
            ImmutableList.of(
                Activity.IDLE
            )
        );
    }
}