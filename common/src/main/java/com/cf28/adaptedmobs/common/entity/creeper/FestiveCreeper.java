package com.cf28.adaptedmobs.common.entity.creeper;

import com.cf28.adaptedmobs.common.entity.creeper.ai.BackOffWithRangeGoal;
import com.cf28.adaptedmobs.common.entity.creeper.ai.ThrowTntToTargetGoal;
import com.cf28.adaptedmobs.common.registry.AMEntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

///**
// * FEATURES:
// * - they throw TNT at you and are very destructive!
// * - there should be an option to disable block damage in the config file
// * - when trying to approach a festive creeper, it will back away
// * - they will set fires underneath them to try and make giving chase to them more hazardous
// * - they are immune to fire and lava
// * - when charged, their TNT is stronger
// * - they have a rare chance of dropping a red Mystery Egg that hatches a tame baby Festive Creeper
// * - baby Festive Creepers are.. quite useless...
// * - but once they grow up, they will fight alongside you!
// * - If they get hurt, you can heal them with gunpowder
// * ADDITIONAL CHANGES:
// * - tries to keep a distance from the player by walking backwards while shooting festive TNT
// * - festive TNT is unobtainable and does not grief blocks, but deals 25 damage at point-blank distance
// * - when charged, the TNT they shoot is regular TNT that destroy blocks, and deal the usual amount of damage
// * LOOT:
// * - gunpowder [similar to creepers]
// * - TNT [rare]
// * - red mystery egg [very rare]
// */
public class FestiveCreeper extends TamableCreeper {
    private static final EntityDataAccessor<State> DATA_STATE = SynchedEntityData.defineId(FestiveCreeper.class, AMEntityDataSerializers.FESTIVE_CREEPER_STATE);
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState firingAnimationState = new AnimationState();

    public FestiveCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
        this.entityData.define(DATA_STATE, State.IDLING);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ThrowTntToTargetGoal(this));
        this.goalSelector.addGoal(3, new BackOffWithRangeGoal(this, 7.5D, 1.25D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public void tick() {
        if (!this.isMoving() && !this.isInWater()) {
            this.walkingAnimationState.stop();
        } else {
            this.walkingAnimationState.startIfStopped(this.tickCount);
        }

        if (this.level.isClientSide) {
            if (this.getState() == State.FIRING) {
                this.firingAnimationState.startIfStopped(this.tickCount);
            } else {
                this.firingAnimationState.stop();
            }
        }

        super.tick();
    }

    private State getState() {
        return this.entityData.get(DATA_STATE);
    }

    private void setState(State state) {
        this.entityData.set(DATA_STATE, state);
    }

    public void transitionTo(State state) {
        switch (state) {
            case IDLING -> this.setState(State.IDLING);
            case FIRING -> {
                this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 1.0F);
                this.setState(State.FIRING);
            }
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

    public enum State {
        IDLING,
        FIRING
    }
}