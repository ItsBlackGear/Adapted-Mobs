package com.cf28.adaptedmobs.common.level.effect;

import com.cf28.adaptedmobs.common.registries.AMMobEffects;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.List;

public class SporeMobEffect extends MobEffect {
    private static final int CLOUD_EFFECT_DURATION = 100;
    private static final int CLOUD_DURATION = 40;
    private static final float CLOUD_RADIUS = 0.75F;

    private static final ResourceLocation SPEED_ID = AdaptedMobs.resource("effect.spore_speed");
    private static final ResourceLocation DAMAGE_ID = AdaptedMobs.resource("effect.spore_damage");

    private static final List<Modifier> MOB_MODIFIERS = List.of(
            new Modifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_ID, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)),
            new Modifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE))
    );
    private static final List<Modifier> PLAYER_MODIFIERS = List.of(
            new Modifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_ID, -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)),
            new Modifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(DAMAGE_ID, -4.0, AttributeModifier.Operation.ADD_VALUE))
    );

    public SporeMobEffect() {
        super(MobEffectCategory.NEUTRAL, 0xD7A329);
    }

    private static boolean isPlayer(AttributeMap attributeMap) {
        return attributeMap.hasAttribute(Attributes.BLOCK_INTERACTION_RANGE); // possibly slightly questionable heuristic, but vanilla only hands the attribute map, not the entity

    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
        for (Modifier modifier : isPlayer(attributeMap) ? PLAYER_MODIFIERS : MOB_MODIFIERS) {
            AttributeInstance instance = attributeMap.getInstance(modifier.attribute());
            if (instance != null) {
                instance.removeModifier(modifier.modifier().id());
                instance.addPermanentModifier(modifier.modifier());
            }
        }
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        for (Modifier modifier : MOB_MODIFIERS) {
            AttributeInstance instance = attributeMap.getInstance(modifier.attribute());
            if (instance != null) {
                instance.removeModifier(modifier.modifier().id());
            }
        }
    }

    @Override
    public void onMobRemoved(LivingEntity entity, int amplifier, Entity.RemovalReason reason) {
        if (reason != Entity.RemovalReason.KILLED || entity.level().isClientSide()) {
            return;
        }

        AreaEffectCloud cloud = new AreaEffectCloud(entity.level(), entity.getX(), entity.getY(), entity.getZ());
        cloud.setRadius(CLOUD_RADIUS);
        cloud.setDuration(CLOUD_DURATION);
        cloud.setWaitTime(0);
        cloud.setParticle(AMParticles.SPORE.get());
        cloud.addEffect(new MobEffectInstance(AMMobEffects.SPORE, CLOUD_EFFECT_DURATION, 0, false, false, true));
        entity.level().addFreshEntity(cloud);
    }

    private record Modifier(Holder<Attribute> attribute, AttributeModifier modifier) {
    }
}
