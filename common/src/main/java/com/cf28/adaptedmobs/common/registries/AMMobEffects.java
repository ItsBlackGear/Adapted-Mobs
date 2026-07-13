package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AMMobEffects {
    public static final CoreRegistry<MobEffect> REGISTRIES = CoreRegistry.create(Registries.MOB_EFFECT, AdaptedMobs.MOD_ID);

    public static final Holder<MobEffect> SUPPORT_SPEED = REGISTRIES.holder("support_speed", () ->
            new MobEffect(MobEffectCategory.BENEFICIAL, 0xD7A329) {
            }
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, AdaptedMobs.resource("support_speed"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final Holder<MobEffect> SUPPORT_STRENGTH = REGISTRIES.holder("support_strength", () ->
            new MobEffect(MobEffectCategory.BENEFICIAL, 0xA64022) {
            }
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, AdaptedMobs.resource("support_strength"), 3.0, AttributeModifier.Operation.ADD_VALUE));

    public static final Holder<MobEffect> SUPPORT_SLOWNESS = REGISTRIES.holder("support_slowness", () ->
            new MobEffect(MobEffectCategory.HARMFUL, 0x518382) {
            }
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, AdaptedMobs.resource("support_slowness"), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final Holder<MobEffect> SUPPORT_WEAKNESS = REGISTRIES.holder("support_weakness", () ->
            new MobEffect(MobEffectCategory.HARMFUL, 0x5A696D) {
            }
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, AdaptedMobs.resource("support_weakness"), -4.0, AttributeModifier.Operation.ADD_VALUE));
}
