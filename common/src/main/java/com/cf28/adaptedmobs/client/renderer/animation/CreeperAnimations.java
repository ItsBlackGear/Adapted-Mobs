package com.cf28.adaptedmobs.client.renderer.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationChannel.*;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class CreeperAnimations {
    public static final AnimationDefinition SIT_DOWN = AnimationDefinition.Builder.withLength(0.25f)
        .addAnimation("leg1",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg2",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg3",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg4",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("all",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, -6f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_IDLE = AnimationDefinition.Builder.withLength(2f).looping()
        .addAnimation("all",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, -6f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.degreeVec(-2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(2f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg1",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.9167666f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.1676667f, KeyframeAnimations.degreeVec(90f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.375f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg2",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.6766666f, KeyframeAnimations.degreeVec(90f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.875f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg3",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.3433333f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.5834333f, KeyframeAnimations.degreeVec(-90f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.8343333f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg4",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.5f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.75f, KeyframeAnimations.degreeVec(-90f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(2f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_UP = AnimationDefinition.Builder.withLength(0.25f)
        .addAnimation("leg1",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg2",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg3",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("leg4",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-85f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("all",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, -6f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();
}