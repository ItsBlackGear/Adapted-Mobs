package com.cf28.adaptedmobs.client.level.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

import static net.minecraft.client.animation.AnimationChannel.Interpolations;
import static net.minecraft.client.animation.AnimationChannel.Targets;

public class RocketCreepieAnimations {
    public static final AnimationDefinition ROCKET = AnimationDefinition.Builder.withLength(1.5f)
        .addAnimation("all",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.5f, KeyframeAnimations.degreeVec(180f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();
}
