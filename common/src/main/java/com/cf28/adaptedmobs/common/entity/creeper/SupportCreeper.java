package com.cf28.adaptedmobs.common.entity.creeper;

import com.cf28.adaptedmobs.common.entity.creeper.ai.ApplyBuffsToTargetGoal;
import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

///**
// * FEATURES:
// * - they run away from players and look for other hostile mobs to follow *
// * - once following a mob, they buff themselves and the mob with potions such as speed and fire resistance *
// * - when they follow creepers, they will turn them into charged creepers
// * - if it happens to be charged, the potion effects that it provides will be more powerful *
// * - they have a rare chance of dropping a yellow Mystery Egg that hatches a tame baby Support Creeper *
// * - the baby support creeper will start helping you right away and give you several helpful potion effects *
// * - [additional note] apparently, owners aren't able to hit tamed creepers... *
// * ADDITIONAL CHANGES:
// * - runs quickly like an ocelot away from players *
// * - looks for hostile mobs to follow and buff with strength I and speed I *
// * - when below 50% health and there is no mob nearby to buff, it will explode like a normal creeper if you get too close *
// * - when charged, the buffs it gives are tier II and its explosion when at low health is larger and deals more damage *
// * - the charge applied by support creeper will go away after 5 minutes
// * LOOT:
// * - gunpowder [similar to creepers]
// * - yellow mystery egg [very rare]
// */
public class SupportCreeper extends TamableCreeper {
    private static final EntityDataAccessor<Optional<UUID>> SUPPORTED_ENTITY_UUID = SynchedEntityData.defineId(SupportCreeper.class, EntityDataSerializers.OPTIONAL_UUID);
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState bestowingAnimationState = new AnimationState();

    public SupportCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ApplyBuffsToTargetGoal(this, 16.0D, 1.25D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.0F, 1.2F, target -> {
            return EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target) && this.getOwner() == null;
        }));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUPPORTED_ENTITY_UUID, Optional.empty());
    }

    @Override
    public void tick() {
        if (!this.isMoving() && !this.isInWater()) {
            this.walkingAnimationState.stop();
        } else {
            this.walkingAnimationState.startIfStopped(this.tickCount);
        }

        if (this.level.isClientSide) {
            if (this.getState().is(CreeperState.ATTACKING)) {
                this.bestowingAnimationState.startIfStopped(this.tickCount);
            } else {
                this.bestowingAnimationState.stop();
            }
        }

        super.tick();
    }

    @Nullable
    public UUID getSupportedUUID() {
        return this.entityData.get(SUPPORTED_ENTITY_UUID).orElse(null);
    }

    public void setSupportedUUID(@Nullable UUID uuid) {
        this.entityData.set(SUPPORTED_ENTITY_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public boolean shouldSwell() {
        return this.getSupportedUUID() == null && this.getHealth() <= this.getMaxHealth() / 2;
    }
}