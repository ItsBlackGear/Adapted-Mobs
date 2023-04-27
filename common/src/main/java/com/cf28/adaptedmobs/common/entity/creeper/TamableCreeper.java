package com.cf28.adaptedmobs.common.entity.creeper;

import com.cf28.adaptedmobs.common.entity.PrimedFestiveTnt;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class TamableCreeper extends Creeper implements OwnableEntity {
    protected static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(TamableCreeper.class, EntityDataSerializers.OPTIONAL_UUID);
    protected static final EntityDataAccessor<CreeperState> DATA_STATE = SynchedEntityData.defineId(TamableCreeper.class, AMEntityDataSerializers.CREEPER_STATE);
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState babyTransformationState = new AnimationState();
    public final AnimationState sittingTransformationState = new AnimationState();
    private boolean orderedToSit;
    private int age;
    private int forcedAge;
    private int forcedAgeTimer;
    private static final int CHARGE_COOLDOWN = 6000;
    private int cooldownTicks = -1;

    public TamableCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
        this.entityData.define(DATA_STATE, CreeperState.IDLING);
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
                return !TamableCreeper.this.isBaby() && super.canUse();
            }
        });
    }

    public CreeperState getState() {
        return this.entityData.get(DATA_STATE);
    }

    public void setState(CreeperState state) {
        this.entityData.set(DATA_STATE, state);
    }

    public void transitionTo(CreeperState state) {
        this.setState(state);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_BABY_ID, false);
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
        tag.putInt("Age", this.getAge());
        tag.putInt("ForcedAge", this.forcedAge);
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

    }

    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
    }

    @Override
    public void setBaby(boolean baby) {
        this.setAge(baby ? -24000 : 0);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
    }

    public boolean isMoving() {
        return (this.onGround || this.isInWaterOrBubble()) && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;
    }

    @Override
    protected void explodeCreeper() {
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction interaction = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.getOwner() == null ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            float explosionMultiplier = this.isPowered() ? 2.0F : 1.0F;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)((CreeperAccessor)this).getExplosionRadius() * explosionMultiplier, interaction);
            if (this.getOwner() != null) {
                this.postExplosion();
            } else {
                this.dead = true;
                this.discard();
                ((CreeperAccessor)this).callSpawnLingeringCloud();
            }
        }
    }

    protected void postExplosion() {
        this.hurt(DamageSource.GENERIC, 1);
        this.setSwellDir(-1);
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
            this.entityData.set(DATA_FLAGS_ID, (byte)(flag & -5));
        }
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
        return !this.isOwnedBy(target) && !(target instanceof Creeper) && super.canAttack(target);
    }

    public boolean isOwnedBy(LivingEntity target) {
        return target == this.getOwner();
    }

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof Ghast) {
            return false;
        } else if (target instanceof TamableCreeper creeper) {
            return !creeper.isTame() || creeper.getOwner() != owner;
        } else if (target instanceof Wolf wolf) {
            return !wolf.isTame() || wolf.getOwner() != owner;
        } else if (target instanceof Player targetP && owner instanceof Player ownerP && !ownerP.canHarmPlayer(targetP)) {
            return false;
        } else if (target instanceof AbstractChestedHorse horse && horse.isTamed()) {
            return false;
        } else {
            return !(target instanceof TamableAnimal) || !((TamableAnimal)target).isTame();
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.level.isClientSide) {
            boolean isTamed = this.isOwnedBy(player) || this.isTame();
            return isTamed ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                if (this.isFood(stack) && this.getHealth() < this.getMaxHealth()) {
                    if (!player.getAbilities().instabuild) stack.shrink(1);

                    this.heal(5);
                    return InteractionResult.SUCCESS;
                }

                InteractionResult result = this.onInteract(player, hand);
                if ((!result.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget(null);
                    return InteractionResult.SUCCESS;
                }

                return result;
            }
        }

        return this.onInteract(player, hand);
    }

    private InteractionResult onInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.FLINT_AND_STEEL) && this.detonateOnInteraction()) {
            this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
            if (!this.level.isClientSide) {
                this.ignite();
                stack.hurtAndBreak(1, player, playerx -> playerx.broadcastBreakEvent(hand));
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            if (this.isFood(stack)) {
                int age = this.getAge();
                if (this.isBaby()) {
                    if (!player.getAbilities().instabuild) stack.shrink(1);
                    this.ageUp(this.getSpeedUpSecondsWhenFeeding(-age), true);
                    return InteractionResult.sidedSuccess(this.level.isClientSide);
                }

                if (this.level.isClientSide) {
                    return InteractionResult.CONSUME;
                }
            }

            return InteractionResult.PASS;
        }
    }

    public boolean detonateOnInteraction() {
        return true;
    }

    public boolean shouldSwell() {
        return true;
    }

    private boolean isFood(ItemStack stack) {
        return stack.is(Items.GUNPOWDER);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getOwner() == null;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isPersistenceRequired();
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

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        Entity entity = source.getEntity();
        if (source.isExplosion() && source.getDirectEntity() == this) {
            return false;
        }

        if (source.getDirectEntity() instanceof PrimedFestiveTnt tnt && source.isExplosion()) {
            if (tnt.getOwner() instanceof FestiveCreeper creeper) {
                return creeper.getOwner() == this.getOwner();
            }

            return tnt.getOwner() == this;
        } else if (entity instanceof TamableCreeper creeper) {
            return creeper.getOwner() == this.getOwner();
        } else if (entity instanceof LivingEntity living) {
            return this.isOwnedBy(living);
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
        if (this.level.isClientSide) {
            if (this.forcedAgeTimer > 0) {
                if (this.forcedAgeTimer % 4 == 0) {
                    this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
                }

                --this.forcedAgeTimer;
            }
        } else if (this.isAlive()) {
            int i = this.getAge();
            if (i < 0) {
                this.setAge(++i);
            } else if (i > 0) {
                this.setAge(--i);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isPowered() && !(this instanceof SupportCreeper)) {
            SupportCreeper support = this.level.getNearestEntity(SupportCreeper.class, TargetingConditions.DEFAULT, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().expandTowards(10.0F, 10.0F, 10.0F));
            if (support != null && support.isSupportingTarget(this) && !this.isDeadOrDying()) {
                this.cooldownTicks = CHARGE_COOLDOWN;
                this.entityData.set(CreeperAccessor.getDATA_IS_POWERED(), true);
                this.level.playSound(null, this.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 1.0F, 1.0F);
            }
        }

        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
            if (this.cooldownTicks == 0) {
                this.entityData.set(CreeperAccessor.getDATA_IS_POWERED(), false);
                this.cooldownTicks = -1;
            }
        }

        if (this.isBaby()) {
            this.babyTransformationState.startIfStopped(this.tickCount);
        } else {
            this.babyTransformationState.stop();
        }
    }

    protected void processAnimations() {
        if (!this.isMoving() && !this.isInWater()) {
            this.walkingAnimationState.stop();
        } else {
            this.walkingAnimationState.startIfStopped(this.tickCount);
        }

        if (this.level.isClientSide) {
            if (this.getState().is(CreeperState.ATTACKING)) {
                this.attackAnimationState.startIfStopped(this.tickCount);
            } else {
                this.attackAnimationState.stop();
            }

            if (this.isInSittingPose()) {
                this.sittingTransformationState.startIfStopped(this.tickCount);
            } else {
                this.sittingTransformationState.stop();
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

    @SuppressWarnings({"ConstantValue", "PointlessArithmeticExpression", "DataFlowIssue"})
    public void ageUp(int amount, boolean forced) {
        int i = this.getAge();
        i += amount * 20;
        if (i > 0) {
            i = 0;
        }

        int k = i - i;
        this.setAge(i);
        if (forced) {
            this.forcedAge += k;
            if (this.forcedAgeTimer == 0) {
                this.forcedAgeTimer = 40;
            }
        }

        if (this.getAge() == 0) {
            this.setAge(this.forcedAge);
        }
    }

    public void setAge(int age) {
        int i = this.getAge();
        this.age = age;
        if (i < 0 && age >= 0 || i >= 0 && age < 0) {
            this.entityData.set(DATA_BABY_ID, age < 0);
            this.ageBoundaryReached();
        }
    }

    protected void ageBoundaryReached() {
    }

    public int getSpeedUpSecondsWhenFeeding(int seconds) {
        return (int)((float)(seconds / 20) * 0.1F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_BABY_ID.equals(key)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(key);
    }
}