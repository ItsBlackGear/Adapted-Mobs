package com.cf28.adaptedmobs.common.entity.creeper;

import com.cf28.adaptedmobs.common.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.registry.AMEntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

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
        this.goalSelector.addGoal(2, new ThrowTNTToTarget(this));
        this.goalSelector.addGoal(3, new TakeDistanceFromTarget(this, 7.5D));
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

    public enum State {
        IDLING,
        FIRING
    }

    public static class TakeDistanceFromTarget extends Goal {
        private final FestiveCreeper creature;
        @Nullable private LivingEntity target;
        private final double maxDistance;

        public TakeDistanceFromTarget(FestiveCreeper creature, double maxDistance) {
            this.creature = creature;
            this.maxDistance = maxDistance;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.target = this.creature.getTarget();
            if (this.target == null) {
                return false;
            } else if (!this.target.isAlive()) {
                return false;
            } else {
                return this.creature.distanceTo(this.target) <= this.maxDistance && this.creature.hasLineOfSight(this.target);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return canUse();
        }

        @Override
        public void stop() {
            target = null;
        }

        @Override
        public void tick() {
            if (this.target != null) {
                Vec3 target = this.target.position().subtract(this.creature.position()).normalize();
                double offset = -0.7D / (target.x * target.x + target.z * target.z + 0.2D) * 0.5D;
                this.creature.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
                this.creature.getLookControl().tick();
                this.creature.setYBodyRot(this.creature.getYHeadRot());
                this.creature.setDeltaMovement(target.x * offset, creature.getDeltaMovement().y, target.z * offset);

                if (this.creature.horizontalCollision) {
                    this.creature.getJumpControl().jump();
                }
            }
        }
    }

    public static class ThrowTNTToTarget extends Goal {
        private final FestiveCreeper creeper;
        private LivingEntity target;
        private int attackCooldown;

        public ThrowTNTToTarget(FestiveCreeper creeper) {
            this.creeper = creeper;
            this.attackCooldown = 20;
        }

        @Override
        public boolean canUse() {
            this.target = this.creeper.getTarget();
            if (this.target == null) {
                return false;
            } else if (!this.target.isAlive()) {
                return false;
            } else {
                return this.creeper.distanceTo(this.target) < 144D && this.creeper.hasLineOfSight(this.target);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void stop() {
            this.target = null;
            this.attackCooldown = 0;
        }

        @Override
        public void tick() {
            --this.attackCooldown;

            if (this.target != null && this.attackCooldown == 7) {
                this.creeper.transitionTo(State.FIRING);
            }

            if (this.target != null && this.attackCooldown <= 0) {
                if (!this.creeper.level.isClientSide && this.attackCooldown == 0) {
                    PrimedFestiveTnt tnt = new PrimedFestiveTnt(this.creeper.level, this.creeper.getX(), this.creeper.getY(), this.creeper.getZ(), this.creeper);
                    tnt.setFuse(30);
                    tnt.setCharged(this.creeper.isPowered());
                    tnt.setDeltaMovement((this.target.getX() - tnt.getX()) / 18D, (this.target.getY() - tnt.getY()) / 18D + 0.5D, (this.target.getZ() - tnt.getZ()) / 18D);
                    this.creeper.level.addFreshEntity(tnt);
                }

                this.creeper.transitionTo(State.IDLING);
                this.attackCooldown = 100;
            }
        }
    }
}