package com.cf28.adaptedmobs.common.entity.creeper;

import com.cf28.adaptedmobs.common.entity.creeper.ai.CreeperFollowOwnerGoal;
import com.cf28.adaptedmobs.common.entity.creeper.ai.CreeperOwnerHurtTargetGoal;
import com.cf28.adaptedmobs.common.entity.creeper.ai.CreeperSitWhenOrderedToGoal;
import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import com.cf28.adaptedmobs.common.registry.AMEntityDataSerializers;
import com.cf28.adaptedmobs.core.mixin.access.CreeperAccessor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class TamableCreeper extends Creeper implements OwnableEntity {
    private static final int AGE_UP_EVENT_ID = 14;

    // Entity Data
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<Integer> DATA_CLOTH_COLOR = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<CreeperState> DATA_STATE = SynchedEntityData.defineId(TamableCreeper.class, AMEntityDataSerializers.CREEPER_STATE);
    protected static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.BYTE);

    // Entity Animations
    public final AnimationState babyTransformationState = new AnimationState();
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState sitUpAnimationState = new AnimationState();
    public final AnimationState sitDownAnimationState = new AnimationState();

    // Entity Variables
    private boolean orderedToSit;
    private int age;
    private int forcedAge;
    private final int explosionCooldown;
    protected int explosionCooldownTimer;

    public TamableCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
        this.entityData.define(DATA_STATE, CreeperState.IDLING);
        this.explosionCooldown = 60;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SwellGoal(this) {
            @Override public boolean canUse() {
                return super.canUse() && TamableCreeper.this.shouldSwell();
            }
        });
        this.goalSelector.addGoal(2, new CreeperSitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0F, 1.2F));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0F, 1.2F));
        this.goalSelector.addGoal(3, new CreeperFollowOwnerGoal(this, 1.25D, 10.0F, 2.0F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new CreeperOwnerHurtTargetGoal(this, true));
        this.targetSelector.addGoal(2, new CreeperOwnerHurtTargetGoal(this, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true, entity -> !this.isTame() && !this.isBaby()));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this) {
            @Override public boolean canUse() {
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BABY_ID, false);
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
        this.entityData.define(DATA_CLOTH_COLOR, DyeColor.RED.getId());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.getOwnerUUID() != null) {
            tag.putUUID("Owner", this.getOwnerUUID());
        }

        tag.putBoolean("Sitting", this.orderedToSit);
        tag.putInt("Age", this.getAge());
        tag.putInt("ForcedAge", this.forcedAge);
        tag.putByte("ClothColor", (byte)this.getClothColor().getId());
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
        this.setAge(tag.getInt("Age"));
        this.forcedAge = tag.getInt("ForcedAge");

        if (tag.contains("ClothColor", 99)) {
            this.setClothColor(DyeColor.byId(tag.getInt("ClothColor")));
        }
    }

    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
    }

    @Override
    public void setBaby(boolean baby) {
        this.setAge(baby ? -24000 : 0);
    }

    public boolean canTarget() {
        return !this.isBaby();
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean hitByPlayer) {
        super.dropCustomDeathLoot(damageSource, looting, hitByPlayer);
        Entity entity = damageSource.getEntity();
        if (entity != this && entity instanceof Creeper creeper && creeper.canDropMobsSkull()) {
            creeper.increaseDroppedSkulls();
            this.spawnAtLocation(this.getSkull());
        }
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
    protected void onOffspringSpawnedFromEgg(Player player, Mob child) {
        super.onOffspringSpawnedFromEgg(player, child);
        if (child instanceof TamableCreeper creeper && this.getOwner() != null) {
            creeper.setOwnerUUID(this.getOwnerUUID());
            creeper.setTame(true);
        }
    }

    public boolean isMoving() {
        return (this.onGround || this.isInWaterOrBubble()) && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
    }

    protected void postExplosion() {
        this.setSwellDir(-1);
        this.setState(CreeperState.IDLING);
        this.explosionCooldownTimer = this.explosionCooldown;
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
            this.entityData.set(DATA_FLAGS_ID, (byte) (flag | 4));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte) (flag & -5));
        }
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
        if (target instanceof Ghast) {
            // Will not try to attack ghasts!
            return false;
        } else if (target instanceof TamableCreeper creeper) {
            // Will try to hurt creepers if they're wild or their owner is different of self.
            return !creeper.isTame() || creeper.getOwner() != owner;
        } else if (target instanceof TamableAnimal animal) {
            // Will try to hurt domestic animals if they're wild or their owner is different of self.
            return !animal.isTame() || animal.getOwner() != owner;
        } else if (target instanceof Player targetPlayer && owner instanceof Player ownerPlayer && !ownerPlayer.canHarmPlayer(targetPlayer)) {
            return false;
        } else return !(target instanceof AbstractChestedHorse horse) || !horse.isTamed();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == AGE_UP_EVENT_ID) {
            this.level.addParticle(
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
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (this.level.isClientSide) {
            boolean isTamed = this.isOwnedBy(player) || this.isTame();
            return isTamed ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                // Heal Entity if health is lower than max health.
                if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    this.heal(5);
                    return InteractionResult.SUCCESS;
                }

                // Update the Cloth color for the Entity.
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
                    // Sit the Entity if ordered to.
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
            } else if (stack.is(Items.FLINT_AND_STEEL) && this.detonateOnInteraction()) {
                this.level.playSound(
                    player,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    SoundEvents.FLINTANDSTEEL_USE,
                    this.getSoundSource(),
                    1.0F,
                    this.random.nextFloat() * 0.4F + 0.8F
                );

                // Ignites the creeper and causes damage into the flint and steel item.
                if (!this.level.isClientSide) {
                    this.ignite();
                    player.swing(hand, true);
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                }

                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            return this.onInteract(player, hand);
        }
    }

    private InteractionResult onInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.isFood(stack)) {
            int age = this.getAge();
            if (this.isBaby()) {
                // Shrink the current stack if allowed to
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                this.ageUp(this.getSpeedUpSecondsWhenFeeding(-age));
                player.swing(hand, true);

                // Display aging up particles
                this.level.broadcastEntityEvent(this, (byte)AGE_UP_EVENT_ID);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    public boolean detonateOnInteraction() {
        return true;
    }

    public boolean shouldSwell() {
        return this.explosionCooldownTimer == 0 || this.isIgnited();
    }

    private boolean isFood(ItemStack stack) {
        return stack.is(Items.GUNPOWDER);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getOwnerUUID() == null;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
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
        // Display death message if tame
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

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.isExplosion() && source.getEntity() == this) {
            return false;
        }

        if (source.getEntity() instanceof TamableCreeper creeper) {
            return creeper.getOwner() == this.getOwner() && creeper.getOwner() != null;
        }

        return super.isInvulnerableTo(source);
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isInSittingPose() ? 20 : super.getMaxHeadXRot();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.level.isClientSide) {
                this.setOrderedToSit(false);
            }

            return super.hurt(source, amount);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide && this.isAlive()) {
            int age = this.getAge();
            if (age < 0) {
                this.setAge(++age);
            } else if (age > 0) {
                this.setAge(--age);
            }
        }
    }

    @Override
    public void tick() {
        if (this.isAlive() && this.shouldExplode()) {
            CreeperAccessor access = (CreeperAccessor) this;
            access.setOldSwell(access.getSwell());
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }

            int swell = this.getSwellDir();
            if (swell > 0 && access.getSwell() == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                this.gameEvent(GameEvent.PRIME_FUSE);
            }

            access.setSwell(access.getSwell() + swell);
            if (access.getSwell() < 0) {
                access.setSwell(0);
            }

            if (access.getSwell() >= access.getMaxSwell()) {
                access.setSwell(access.getMaxSwell());
                this.causeExplosion();
            }
        }

        super.tick();

        if (this.explosionCooldownTimer > 0) {
            this.explosionCooldownTimer--;
        }

        this.setupAnimationStates();
    }

    private boolean shouldExplode() {
        return true;
    }

    protected void causeExplosion() {
        CreeperAccessor access = (CreeperAccessor)this;
        if (this.shouldSwell()) {
            if (this.isTame()) {
                if (!this.level.isClientSide) {
                    float explosionMultiplier = this.isPowered() ? 2.0F : 1.0F;
                    this.level.explode(this,
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        (float) access.getExplosionRadius() * explosionMultiplier,
                        Explosion.BlockInteraction.NONE
                    );

                    this.postExplosion();
                }
            } else {
                access.callExplodeCreeper();
            }
        } else {
            this.postExplosion();
        }
    }

    private void setupAnimationStates() {
        if (this.level.isClientSide) {
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

    protected void setupAnimations() {
        if (this.level.isClientSide) {
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

    public int getAge() {
        if (this.level.isClientSide) {
            return this.entityData.get(DATA_BABY_ID) ? -1 : 1;
        } else {
            return this.age;
        }
    }

    public void ageUp(int amount) {
        int i = this.getAge();
        i += amount * 20;
        if (i > 0) {
            i = 0;
        }

        this.setAge(i);

        if (this.getAge() == 0) {
            this.setAge(this.forcedAge);
        }
    }

    public void setAge(int age) {
        int i = this.getAge();
        this.age = age;
        if (i < 0 && age >= 0 || i >= 0 && age < 0) {
            this.entityData.set(DATA_BABY_ID, age < 0);
        }
    }

    public int getSpeedUpSecondsWhenFeeding(int seconds) {
        return (int) ((float) (seconds / 20) * 0.1F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_BABY_ID.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }

    public DyeColor getClothColor() {
        return DyeColor.byId(this.entityData.get(DATA_CLOTH_COLOR));
    }

    public void setClothColor(DyeColor color) {
        this.entityData.set(DATA_CLOTH_COLOR, color.getId());
    }

    public ClothType getClothType() {
        return ClothType.DEFAULT;
    }

    public enum ClothType {
        DEFAULT("creeper"),
        FESTIVE("festive"),
        ROCKET("rocket"),
        SUPPORT("support");

        public final String id;

        ClothType(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }
}