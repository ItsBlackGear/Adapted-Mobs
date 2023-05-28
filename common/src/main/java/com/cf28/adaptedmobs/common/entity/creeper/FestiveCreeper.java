package com.cf28.adaptedmobs.common.entity.creeper;

import com.cf28.adaptedmobs.common.entity.creeper.ai.BackOffWithRangeGoal;
import com.cf28.adaptedmobs.common.entity.creeper.ai.ThrowTntToTargetGoal;
import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

/**
 * FEATURES:
 * - they throw TNT at you and are very destructive! *
 * - when trying to approach a festive creeper, it will back away *
 * - when charged, their TNT is stronger *
 * - they have a rare chance of dropping a red Mystery Egg that hatches a tame baby Festive Creeper *
 * - baby Festive Creepers are.. quite useless... *
 * - but once they grow up, they will fight alongside you! *
 * - If they get hurt, you can heal them with gunpowder *
 * ADDITIONAL CHANGES:
 * - tries to keep a distance from the player by walking backwards while shooting festive TNT *
 * - festive TNT is unobtainable and does not grief blocks, but deals 25 damage at point-blank distance *
 * - when charged, the TNT they shoot is regular TNT that destroy blocks, and deal the usual amount of damage *
 * LOOT:
 * - gunpowder [similar to creepers]
 * - TNT [rare]
 * - red mystery egg [very rare]
 */
public class FestiveCreeper extends TamableCreeper {
    public FestiveCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ThrowTntToTargetGoal(this));
        this.goalSelector.addGoal(3, new BackOffWithRangeGoal(this, 5.5D, 1.25D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public void tick() {
        this.setupWalkAnimations();
        super.tick();
    }

    @Override
    public void setState(CreeperState state) {
        if (state.is(CreeperState.ATTACKING)) {
            this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 1.0F);
            super.setState(CreeperState.ATTACKING);
        } else {
            super.setState(state);
        }
    }

    @Override
    public boolean detonateOnInteraction() {
        return false;
    }

    @Override
    public boolean shouldSwell() {
        return false;
    }
}