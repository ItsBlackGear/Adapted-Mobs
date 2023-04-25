package com.cf28.adaptedmobs.client.renderer.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class GlobalAnimations {
    public static final AnimationDefinition BABY_TRANSFORM = AnimationDefinition.Builder
            .withLength(0.0F)
            .addAnimation(
                    "head",
                    new AnimationChannel(
                            AnimationChannel.Targets.SCALE,
                            new Keyframe(
                                    0.0F,
                                    KeyframeAnimations.scaleVec(
                                            1.5F,
                                            1.5F,
                                            1.5F
                                    ),
                                    AnimationChannel.Interpolations.LINEAR
                            )
                    )
            ).build();
}