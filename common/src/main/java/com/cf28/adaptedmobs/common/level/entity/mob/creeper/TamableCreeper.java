package com.cf28.adaptedmobs.common.level.entity.mob.creeper;


import com.cf28.adaptedmobs.common.level.entity.ai.goal.CreeperFollowOwnerGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.goal.CreeperOwnerHurtTargetGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.goal.CreeperSitWhenOrderedToGoal;
import com.cf28.adaptedmobs.common.registries.AMEntityDataSerializers;
import com.cf28.adaptedmobs.core.tags.AMItemTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class TamableCreeper extends Creeper implements OwnableEntity {
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> DATA_CLOTH_COLOR = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<CreeperState> DATA_STATE = SynchedEntityData.defineId(TamableCreeper.class, AMEntityDataSerializers.CREEPER_STATE.get());
    protected static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.BYTE);
    private static final int AGE_UP_EVENT = 14;
    private static final float BLAST_DAMAGE_MULTIPLIER = 0.85F;
    public final AnimationState babyTransformationState = new AnimationState();
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState sitUpAnimationState = new AnimationState();
    public final AnimationState sitDownAnimationState = new AnimationState();
    protected int explosionCooldown;
    private boolean isTame;
    private boolean orderedToSit;
    private boolean forceDetonate;
    private int age;
    private int forcedAge;
    private boolean am$exploded;

    public TamableCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, CreeperState.IDLING);
        builder.define(DATA_BABY_ID, false);
        builder.define(DATA_FLAGS_ID, (byte) 0);
        builder.define(DATA_OWNER_UUID, Optional.empty());
        builder.define(DATA_CLOTH_COLOR, DyeColor.RED.getId());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerUUID() != null)
            compound.putUUID("Owner", this.getOwnerUUID());

        compound.putBoolean("Tamed", this.isTame);
        compound.putBoolean("Sitting", this.orderedToSit);
        compound.putInt("Age", this.getAge());
        compound.putInt("ForcedAge", this.forcedAge);
        compound.putBoolean("ForceDetonate", this.forceDetonate);
        compound.putByte("ClothColor", (byte) this.getClothColor().getId());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid = compound.hasUUID("Owner") ? compound.getUUID("Owner") : null;
        this.isTame = compound.getBoolean("Tamed");

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
                this.setTame(true);
            } catch (Throwable throwable) {
                this.setTame(false);
            }
        }

        this.setOrderedToSit(compound.getBoolean("Sitting"));
        this.setAge(compound.getInt("Age"));
        this.forcedAge = compound.getInt("ForcedAge");
        this.forceDetonate = compound.getBoolean("ForceDetonate");

        if (compound.contains("ClothColor", 99))
            this.setClothColor(DyeColor.byId(compound.getInt("ClothColor")));
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (DATA_BABY_ID.equals(key))
            this.refreshDimensions();

        super.onSyncedDataUpdated(key);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == AGE_UP_EVENT) {
            this.level().addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    this.getRandomX(1.0),
                    this.getRandomY() + 0.5,
                    this.getRandomZ(1.0),
                    0.0,
                    0.0,
                    0.0
            );
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SwellGoal(this) {
            @Override
            public boolean canUse() {
                return TamableCreeper.this.shouldDetonate() || (super.canUse() && TamableCreeper.this.shouldSwell());
            }
        });
        this.goalSelector.addGoal(2, new CreeperSitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0F, 1.2F));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0F, 1.2F));
        this.goalSelector.addGoal(3, new CreeperFollowOwnerGoal(this, 1.25, 10.0F, 2.0F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new CreeperOwnerHurtTargetGoal(this, true));
        this.targetSelector.addGoal(2, new CreeperOwnerHurtTargetGoal(this, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true, entity -> !this.isTame() && !this.isBaby()));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                return TamableCreeper.this.canTarget() && super.canUse();
            }
        });
    }

    public CreeperState getState() {
        return this.entityData.get(DATA_STATE);
    }

    public void setState(CreeperState state) {
        this.entityData.set(DATA_STATE, state);
    }

    public DyeColor getClothColor() {
        return DyeColor.byId(this.entityData.get(DATA_CLOTH_COLOR));
    }

    public void setClothColor(DyeColor color) {
        this.entityData.set(DATA_CLOTH_COLOR, color.getId());
    }

    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
    }

    @Override
    public void setBaby(boolean baby) {
        this.setAge(baby ? -24000 : 0);
    }

    public boolean isTame() {
        return (this.entityData.get(DATA_FLAGS_ID) & 4) != 0;
    }

    public void setTame(boolean tame) {
        this.isTame = tame;
        byte flag = this.entityData.get(DATA_FLAGS_ID);
        this.entityData.set(DATA_FLAGS_ID, tame ? (byte) (flag | 4) : (byte) (flag & -5));
    }

    public boolean isInSittingPose() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setInSittingPose(boolean sitting) {
        byte flag = this.entityData.get(DATA_FLAGS_ID);
        if (sitting) {
            this.entityData.set(DATA_FLAGS_ID, (byte) (flag | 1));
            this.setState(CreeperState.SITTING);
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (flag & -2));
            this.setState(CreeperState.STANDING);
        }
    }

    public boolean isOrderedToSit() {
        return this.orderedToSit;
    }

    public void setOrderedToSit(boolean orderedToSit) {
        this.orderedToSit = orderedToSit;
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            return uuid == null ? null : this.level().getPlayerByUUID(uuid);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public boolean isOwnedBy(LivingEntity target) {
        return target == this.getOwner();
    }

    public int getAge() {
        if (this.level().isClientSide) {
            return this.entityData.get(DATA_BABY_ID) ? -1 : 1;
        } else {
            return this.age;
        }
    }

    public void setAge(int value) {
        int age = this.getAge();
        this.age = value;
        if (age < 0 && value >= 0 || age >= 0 && value < 0) {
            this.entityData.set(DATA_BABY_ID, value < 0);
        }
    }

    public void ageUp(int amount) {
        int age = this.getAge();
        age += amount * 20;
        if (age > 0)
            age = 0;

        this.setAge(age);

        if (this.getAge() == 0)
            this.setAge(this.forcedAge);
    }

    public int getSpeedUpSecondsWhenFeeding(int seconds) {
        return (int) ((float) (seconds / 20) * 0.1F);
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return !this.isOwnedBy(target) && super.canAttack(target);
    }

    public boolean canTarget() {
        return !this.isBaby();
    }

    public boolean shouldSwell() {
        return this.explosionCooldown == 0 || this.isIgnited();
    }

    public void setShouldDetonate(boolean detonate) {
        this.forceDetonate = detonate;
    }

    public boolean shouldDetonate() {
        return this.forceDetonate;
    }

    protected void postExplosion() {
        this.setSwellDir(-1);
        this.setState(CreeperState.IDLING);
        this.explosionCooldown = 60;
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean bl) {
        this.am$exploded = damageSource.is(DamageTypeTags.IS_EXPLOSION);
        super.dropFromLootTable(damageSource, bl);
    }

    @Override
    protected @NotNull ResourceKey<LootTable> getDefaultLootTable() {
        if (this.am$exploded) {
            ResourceKey<LootTable> explosionLootTable = this.getExplosionLootTable();
            if (explosionLootTable != null) {
                return explosionLootTable;
            }
        }
        return super.getDefaultLootTable();
    }

    @Nullable
    protected ResourceKey<LootTable> getExplosionLootTable() {
        return null;
    }

    @Override
    protected void explodeCreeper() {
        if (this.shouldSwell()) {
            if (this.isTame()) {
                if (!this.level().isClientSide()) {
                    float explosionMultiplier = this.isPowered() ? 2.0F : 1.0F;
                    this.level().explode(
                            this,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            (float) this.explosionRadius * explosionMultiplier,
                            Level.ExplosionInteraction.NONE
                    );

                    this.postExplosion();
                }
            } else {
                super.explodeCreeper();
                this.postExplosion();
            }
        } else {
            this.postExplosion();
        }
    }

    public float getExplosionDamageMultiplier() {
        return 1.0F;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getOwnerUUID() == null;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
    }

    public boolean canFollow() {
        return true;
    }

    @Override
    public boolean canBeLeashed() {
        return !this.isLeashed() && this.isTame();
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isInSittingPose() ? 20 : super.getMaxHeadXRot();
    }

    private boolean isFood(ItemStack stack) {
        return stack.is(AMItemTags.CREEPER_FOOD);
    }

    public ClothType getClothType() {
        return ClothType.DEFAULT;
    }

    public boolean isMoving() {
        return (this.onGround() || this.isInWaterOrBubble()) && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
    }

    @Override
    public boolean canDropMobsSkull() {
        return super.canDropMobsSkull() && !this.isTame();
    }

    protected ItemStack getSkull() {
        return new ItemStack(Items.CREEPER_HEAD);
    }

    @Override
    public boolean shouldDropExperience() {
        return !this.isBaby();
    }

    @Override
    protected boolean shouldDropLoot() {
        return !this.isBaby();
    }

    @Override
    public void tick() {
        if (this.explosionCooldown > 0) {
            this.explosionCooldown--;
        }

        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int i = this.getSwellDir();
            if (i > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            this.swell += i;
            if (this.swell < 0) {
                this.swell = 0;
            }

            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
        }

        super.tick();
        this.setupAnimationStates();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive()) {
            int age = this.getAge();
            if (age < 0) {
                this.setAge(++age);
            } else if (age > 0) {
                this.setAge(--age);
            }
        }
    }

    protected void setupAnimations() {
        if (this.level().isClientSide) {
            if (!this.isMoving() && !this.isInWater()) {
                this.walkingAnimationState.stop();
            } else {
                this.walkingAnimationState.startIfStopped(this.tickCount);
            }

            if (this.getState().is(CreeperState.ATTACKING)) {
                this.attackAnimationState.startIfStopped(this.tickCount);
            } else {
                this.attackAnimationState.stop();
            }
        }
    }

    private void setupAnimationStates() {
        if (this.level().isClientSide) {
            if (this.isInSittingPose()) {
                this.sitDownAnimationState.startIfStopped(this.tickCount);
                this.sitUpAnimationState.stop();
            } else {
                if (this.getState().is(CreeperState.STANDING)) {
                    this.sitUpAnimationState.startIfStopped(this.tickCount);
                    this.sitDownAnimationState.stop();
                } else {
                    this.sitDownAnimationState.stop();
                    this.sitUpAnimationState.stop();
                }
            }

            if (this.isBaby()) {
                this.babyTransformationState.startIfStopped(this.tickCount);
            } else {
                this.babyTransformationState.stop();
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.is(DamageTypeTags.IS_EXPLOSION) && source.getEntity() == this) {
            return false;
        }

        if (source.getEntity() instanceof TamableCreeper creeper) {
            return creeper.getOwner() == this.getOwner() && creeper.getOwner() != null;
        }

        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.level().isClientSide) {
                this.setOrderedToSit(false);
            }

            if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                amount *= BLAST_DAMAGE_MULTIPLIER;
            }

            return super.hurt(source, amount);
        }
    }

    @Override
    public void die(@NotNull DamageSource source) {
        if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
            this.getOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }

        super.die(source);
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        Entity entity = damageSource.getEntity();
        if (entity != this && entity instanceof Creeper creeper && creeper.canDropMobsSkull()) {
            creeper.increaseDroppedSkulls();
            this.spawnAtLocation(this.getSkull());
        }
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity target) {
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

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        return switch (target) {
            case Ghast ignored -> false;
            case TamableCreeper creeper -> !creeper.isTame() || creeper.getOwner() != owner;
            case TamableAnimal animal -> !animal.isTame() || animal.getOwner() != owner;
            case Player targetPlayer when owner instanceof Player ownerPlayer && !ownerPlayer.canHarmPlayer(targetPlayer) ->
                    false;
            case null, default -> !(target instanceof AbstractChestedHorse horse) || !horse.isTamed();
        };
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (this.level().isClientSide) {
            boolean isTamed = this.isOwnedBy(player) || this.isTame();
            return isTamed ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    this.heal(5);
                    return InteractionResult.SUCCESS;
                }

                if (item instanceof DyeItem dye) {
                    DyeColor color = dye.getDyeColor();
                    if (color != this.getClothColor() && this.isOwnedBy(player)) {
                        this.setClothColor(color);

                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                        }

                        return InteractionResult.SUCCESS;
                    }
                } else {
                    InteractionResult result = this.onInteract(player, hand);
                    if (!result.consumesAction() && this.isOwnedBy(player)) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                        return InteractionResult.SUCCESS;
                    }

                    return result;
                }
            } else if (stack.is(Items.FLINT_AND_STEEL) && !this.shouldDetonate()) {
                this.setShouldDetonate(true);
                this.level().playSound(
                        player,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        SoundEvents.FLINTANDSTEEL_USE,
                        this.getSoundSource(),
                        1.0F,
                        this.random.nextFloat() * 0.4F + 0.8F
                );

                if (!this.level().isClientSide) {
                    this.ignite();
                    player.swing(hand, true);
                    stack.hurtAndBreak(1, player, getSlotForHand(hand));
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            return this.onInteract(player, hand);
        }
    }

    private InteractionResult onInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isFood(stack)) {
            int age = this.getAge();
            if (this.isBaby()) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                this.ageUp(this.getSpeedUpSecondsWhenFeeding(-age));
                player.swing(hand, true);

                this.level().broadcastEntityEvent(this, (byte) AGE_UP_EVENT);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (this.level().isClientSide) {
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void onOffspringSpawnedFromEgg(@NotNull Player player, @NotNull Mob child) {
        super.onOffspringSpawnedFromEgg(player, child);
        if (child instanceof TamableCreeper creeper && this.getOwner() != null) {
            creeper.setOwnerUUID(this.getOwnerUUID());
            creeper.setTame(true);
        }
    }
}