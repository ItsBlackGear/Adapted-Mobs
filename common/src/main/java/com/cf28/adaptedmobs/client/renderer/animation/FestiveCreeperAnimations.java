package com.cf28.adaptedmobs.client.renderer.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationChannel.*;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

@Environment(EnvType.CLIENT)
public class FestiveCreeperAnimations {
    public static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(0.8343334f).looping()
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(-25f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(-25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(25f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(-25f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(-25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(25f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upper",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.20834334f, KeyframeAnimations.posVec(0f, -0.3f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.posVec(0f, -0.3f, 0f), Interpolations.LINEAR),
                new Keyframe(0.8343334f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.20834334f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.degreeVec(-5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.degreeVec(-2.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(-5f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition FIRE = AnimationDefinition.Builder.withLength(1.5f)
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upper",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, -1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, -1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.6766666f, KeyframeAnimations.posVec(0f, -1f, 0f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upper",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-20f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.degreeVec(5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.degreeVec(-20f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.8343334f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_DOWN = AnimationDefinition.Builder.withLength(1f)
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.degreeVec(27.5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0.375f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.degreeVec(90f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0.625f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0.375f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.degreeVec(90f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0.625f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0.375f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.degreeVec(-90f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0.625f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0.375f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.degreeVec(-90f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0.625f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("all",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0.375f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.posVec(0f, -3f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SIT_IDLE = AnimationDefinition.Builder.withLength(2f).looping()
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(90f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(90f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-90f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-90f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.posVec(0f, 0.5f, 0f), Interpolations.LINEAR),
                new Keyframe(2f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-1.25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(1.3433333f, KeyframeAnimations.degreeVec(-5f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(2f, KeyframeAnimations.degreeVec(-1.25f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("belly",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR),
                new Keyframe(1f, KeyframeAnimations.scaleVec(1f, 1.05f, 1f), Interpolations.LINEAR),
                new Keyframe(2f, KeyframeAnimations.scaleVec(1f, 1f, 1f), Interpolations.LINEAR)
            )
        )
        .addAnimation("all",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, -3f, 0f), Interpolations.LINEAR)
            )
        )
        .build();

    public static final AnimationDefinition SITUP = AnimationDefinition.Builder.withLength(0.75f)
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.125f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.20834334f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.2916767f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.375f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4583433f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5416766f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.6766666f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(90f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.125f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.20834334f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.2916767f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.375f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4583433f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5416766f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.6766666f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(90f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("backrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.125f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.20834334f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.2916767f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.375f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4583433f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5416766f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.posVec(0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.6766666f, KeyframeAnimations.posVec(0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-90f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontrightleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.125f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.20834334f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.2916767f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.375f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4583433f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5416766f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.posVec(-0.2f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.6766666f, KeyframeAnimations.posVec(-0.4f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(-90f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("frontleftleg",
            new AnimationChannel(Targets.SCALE,
                new Keyframe(0f, KeyframeAnimations.scaleVec(1.01f, 1.01f, 1.01f), Interpolations.LINEAR)
            )
        )
        .addAnimation("upper",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.08343333f, KeyframeAnimations.posVec(0f, -0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.125f, KeyframeAnimations.posVec(0f, 0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.16766666f, KeyframeAnimations.posVec(0f, -0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.20834334f, KeyframeAnimations.posVec(0f, 0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.25f, KeyframeAnimations.posVec(0f, -0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.2916767f, KeyframeAnimations.posVec(0f, 0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.3433333f, KeyframeAnimations.posVec(0f, -0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.375f, KeyframeAnimations.posVec(0f, 0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4167667f, KeyframeAnimations.posVec(0f, -0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.4583433f, KeyframeAnimations.posVec(0f, 0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5f, KeyframeAnimations.posVec(0f, -0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5416766f, KeyframeAnimations.posVec(0f, 0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.5834334f, KeyframeAnimations.posVec(0f, -0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.625f, KeyframeAnimations.posVec(0f, 0.1f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("head",
            new AnimationChannel(Targets.ROTATION,
                new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.2916767f, KeyframeAnimations.degreeVec(-25f, 0f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.degreeVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .addAnimation("all",
            new AnimationChannel(Targets.POSITION,
                new Keyframe(0f, KeyframeAnimations.posVec(0f, -3f, 0f), Interpolations.LINEAR),
                new Keyframe(0.75f, KeyframeAnimations.posVec(0f, 0f, 0f), Interpolations.LINEAR)
            )
        )
        .build();
}