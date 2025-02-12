package com.cf28.adaptedmobs.client.renderer.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationChannel.*;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class SupportCreeperAnimations {
    public static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(0.5f).looping()
        .addAnimation("leg0",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0.33f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0.33f, 0f), Interpolations.LINEAR)))
        .addAnimation("leg0",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(30f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f), Interpolations.LINEAR)))
        .addAnimation("leg1",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)))
        .addAnimation("leg1",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(30f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)))
        .addAnimation("leg2",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR)))
        .addAnimation("leg2",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f), Interpolations.LINEAR)))
        .addAnimation("leg3",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0.67f, 0f), Interpolations.LINEAR),
                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, 0.67f, 0f), Interpolations.LINEAR)))
        .addAnimation("leg3",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-8.75f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(-17.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(17.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-8.75f, 0f, 0f), Interpolations.LINEAR)))
        .addAnimation("upper",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f), Interpolations.LINEAR)))
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-0.83f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.041676664f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-0.83f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition BESTOW = AnimationDefinition.Builder.withLength(1.0834333f)
        .addAnimation("upper",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0.08343333f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 0f, 20 / 2f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(0f, 0f, -20 / 2f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(0f, 0f, 15 / 2f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, -10 / 2f), Interpolations.LINEAR),
                new Keyframe(0.9167666f, KeyframeAnimations.degreeVec(0f, 0f, 5 / 2f), Interpolations.LINEAR),
                new Keyframe(1.0834333f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.degreeVec(-10f, 0f, 20f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(-10f, 0f, -20f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(-10f, 0f, 15f), Interpolations.LINEAR),
                new Keyframe(0.6766666f, KeyframeAnimations.degreeVec(-10f, 0f, -10f), Interpolations.LINEAR),
                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(-5f, 0f, 5f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_DOWN = AnimationDefinition.Builder.withLength(0.375f)
        .addAnimation("body",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, -7f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg0",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(87.46148353811986f, -9.990384523016473f, 0.44066890015938043f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg1",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(87.47845532339807f, 7.4928206685040095f, -0.3290232005838334f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg2",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-87.47845532339807f, 7.4928206685040095f, 0.3290232005838334f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg3",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-87.47845532339807f, -7.4928206685040095f, -0.3290232005838334f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upper",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.scaleVec(1f, 0.95f, 1f), Interpolations.LINEAR),
                new Keyframe(0.375f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_IDLE = AnimationDefinition.Builder.withLength(2f).looping()
        .addAnimation("body",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, -7f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg0",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(87.46148353811986f, -9.990384523016473f, 0.44066890015938043f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg1",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(87.47845532339807f, 7.4928206685040095f, -0.3290232005838334f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg2",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-87.47845532339807f, 7.4928206685040095f, 0.3290232005838334f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg3",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-87.47845532339807f, -7.4928206685040095f, -0.3290232005838334f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upper",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.scaleVec(1f, 0.95f, 1f), Interpolations.LINEAR),
                new Keyframe(2f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_UP = AnimationDefinition.Builder.withLength(0.5f)
        .addAnimation("body",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, -7f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg0",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(87.46148353811986f, -9.990384523016473f, 0.44066890015938043f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg1",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(87.47845532339807f, 7.4928206685040095f, -0.3290232005838334f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg2",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-87.47845532339807f, 7.4928206685040095f, 0.3290232005838334f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg3",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-87.47845532339807f, -7.4928206685040095f, -0.3290232005838334f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upper",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.scaleVec(1f, 1.05f, 1f), Interpolations.LINEAR),
                new Keyframe(0.375f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .build();
}