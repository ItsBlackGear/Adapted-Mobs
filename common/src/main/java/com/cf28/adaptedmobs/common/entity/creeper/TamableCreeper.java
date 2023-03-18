package com.cf28.adaptedmobs.common.entity.creeper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public abstract class TamableCreeper extends Creeper implements OwnableEntity {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.OPTIONAL_UUID);
    private boolean orderedToSit;

    public TamableCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
        this.reassessTameGoals();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0F, 1.2F));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0F, 1.2F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this, true));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getOwnerUUID() != null) {
            tag.putUUID("Owner", this.getOwnerUUID());
        }

        tag.putBoolean("Sitting", this.orderedToSit);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        UUID uuid = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
                this.setTame(true);
            } catch (Throwable throwable) {
                this.setTame(false);
            }
        }

        this.orderedToSit = tag.getBoolean("Sitting");
        this.setInSittingPose(this.orderedToSit);
    }

    public boolean isMoving() {
        return (this.onGround || this.isInWaterOrBubble()) && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return !this.isLeashed() && this.isTame();
    }

    public boolean isTame() {
        return (this.entityData.get(DATA_FLAGS_ID) & 4) != 0;
    }

    public void setTame(boolean tame) {
        byte flag = this.entityData.get(DATA_FLAGS_ID);
        if (tame) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(flag | 4));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(flag & -5));
        }

        this.reassessTameGoals();
    }

    protected void reassessTameGoals() {
    }

    public boolean isInSittingPose() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setInSittingPose(boolean sitting) {
        byte flag = this.entityData.get(DATA_FLAGS_ID);
        if (sitting) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(flag | 1));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(flag | 1));
        }
    }

    @Nullable @Override
    public UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Nullable @Override
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !this.isOwnedBy(target) && super.canAttack(target);
    }

    public boolean isOwnedBy(LivingEntity target) {
        return target == this.getOwner();
    }

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        return true;
    }

    @Nullable @Override
    public Team getTeam() {
        if (this.isTame()) {
            LivingEntity entity = this.getOwner();
            if (entity != null) {
                return entity.getTeam();
            }
        }

        return super.getTeam();
    }

    @Override
    public boolean isAlliedTo(Entity target) {
        if (this.isTame()) {
            LivingEntity entity = this.getOwner();
            if (target == entity) {
                return true;
            }

            if (entity != null) {
                return entity.isAlliedTo(target);
            }
        }

        return super.isAlliedTo(target);
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
            this.getOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }

        super.die(source);
    }

    public boolean isOrderedToSit() {
        return this.orderedToSit;
    }

    public void setOrderedToSit(boolean orderedToSit) {
        this.orderedToSit = orderedToSit;
    }

    public static class OwnerHurtTargetGoal extends TargetGoal {
        private final TamableCreeper creeper;
        private final boolean isHurtBy;
        private LivingEntity target;
        private int timestamp;

        public OwnerHurtTargetGoal(TamableCreeper creeper, boolean isHurtBy) {
            super(creeper, false);
            this.creeper = creeper;
            this.isHurtBy = isHurtBy;
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (this.creeper.isTame() && !this.creeper.isOrderedToSit()) {
                LivingEntity owner = this.creeper.getOwner();
                if (owner == null) {
                    return false;
                } else {
                    this.target = this.isHurtBy ? owner.getLastHurtByMob() : owner.getLastHurtMob();
                    int timestamp = this.isHurtBy ? owner.getLastHurtByMobTimestamp() : owner.getLastHurtMobTimestamp();
                    return timestamp != this.timestamp && this.canAttack(this.target, TargetingConditions.DEFAULT) && this.creeper.wantsToAttack(this.target, owner);
                }
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.mob.setTarget(this.target);
            LivingEntity owner = this.creeper.getOwner();
            if (owner != null) this.timestamp = this.isHurtBy ? owner.getLastHurtByMobTimestamp() : owner.getLastHurtMobTimestamp();
            super.start();
        }
    }
}