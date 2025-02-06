package com.cf28.adaptedmobs.client.renderer.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

import static net.minecraft.client.animation.AnimationChannel.*;

public class RocketCreeperAnimations {
    public static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(0.5f).looping()
        .addAnimation("rightlegfront",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("rightlegfront",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegfront",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0.67f, 0f), Interpolations.LINEAR),
                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0.67f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegfront",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-8.75f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-8.75f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegback",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegback",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(30f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("rightlegback",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0.33f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0.33f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("rightlegback",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(30f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upper",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-3.33f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.041676664f, KeyframeAnimations.degreeVec(-3.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(1.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-3.33f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition ROCKET = AnimationDefinition.Builder.withLength(2f)
        .addAnimation("upper",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upper",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.degreeVec(10f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.6766667f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.degreeVec(20f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("all",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.5416767f, KeyframeAnimations.degreeVec(180f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_DOWN = AnimationDefinition.Builder.withLength(0.5834334f)
        .addAnimation("rightlegfront",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegfront",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegback",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("rightlegback",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upperhalf",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("all",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, -7f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_IDLE = AnimationDefinition.Builder.withLength(2f).looping()
        .addAnimation("rightlegfront",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegfront",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegback",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("rightlegback",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upperhalf",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0.93f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.4167667f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(2f, KeyframeAnimations.degreeVec(0.93f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("all",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, -7f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_UP = AnimationDefinition.Builder.withLength(0.5834334f)
        .addAnimation("rightlegfront",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegfront",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leftlegback",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("rightlegback",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upperhalf",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0.125f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.375f, KeyframeAnimations.degreeVec(-7.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("all",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, -7f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();
}