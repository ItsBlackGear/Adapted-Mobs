package com.cf28.adaptedmobs.common.level.entity.mob;

import com.cf28.adaptedmobs.common.level.entity.ai.HarpyDashGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.HarpyPickupGoal;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Harpy extends TamableAnimal implements FlyingAnimal {
    private static final EntityDataAccessor<Boolean> DATA_DASHING = SynchedEntityData.defineId(Harpy.class, EntityDataSerializers.BOOLEAN);

    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;

    public Harpy(EntityType<? extends Harpy> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    public static boolean checkHarpySpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isValidSpawn(level, pos, type);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DASHING, false);
    }

    public boolean isDashing() {
        return this.entityData.get(DATA_DASHING);
    }

    public void setDashing(boolean dashing) {
        this.entityData.set(DATA_DASHING, dashing);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HarpyDashGoal(this));
        this.goalSelector.addGoal(2, new HarpyPickupGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true) {
            @Override
            public boolean canUse() {
                return !Harpy.this.isBaby() && !Harpy.this.isVehicle() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !Harpy.this.isBaby() && !Harpy.this.isVehicle() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> !this.isTame() && !this.isBaby()));
    }

    private int pickupCooldown;

    public int getPickupCooldown() {
        return this.pickupCooldown;
    }

    public void setPickupCooldown(int pickupCooldown) {
        this.pickupCooldown = pickupCooldown;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.calculateFlapping();

        if (this.pickupCooldown > 0) {
            this.pickupCooldown--;
        }

        if (this.isTame() && this.getVehicle() instanceof Player player) {
            if (player.isCrouching()) {
                if (!this.level().isClientSide) {
                    this.stopRiding();
                }
            } else if (!player.onGround() && player.getDeltaMovement().y < 0.0) {
                Vec3 vec3 = player.getDeltaMovement();
                player.setDeltaMovement(vec3.x, Math.max(vec3.y, -0.15D), vec3.z);
                player.fallDistance = 0.0F;
            }
        }
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = this.flapSpeed + (float) (!this.onGround() && !this.isPassenger() ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }
        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0) {
            this.setDeltaMovement(vec3.multiply(1.0, 0.6, 1.0));
        }
        this.flap = this.flap + this.flapping * 2.0F;
    }

    @Override
    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    @Override
    protected void onFlap() {
        this.playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (this.isBaby() && !this.isTame() && itemstack.is(ItemTags.MEAT)) {
            itemstack.consume(1, player);
            if (!this.level().isClientSide) {
                if (this.random.nextInt(5) == 0) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (this.isTame() && this.isOwnedBy(player) && !this.isBaby()) {
            if (player.isShiftKeyDown() && itemstack.isEmpty()) {
                if (!this.level().isClientSide) {
                    if (this.isPassenger()) {
                        this.stopRiding();
                    } else {
                        this.startRiding(player, true);
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        return super.mobInteract(player, hand);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return AMEntityTypes.HARPY.get().create(level);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && this.hasPassenger(source.getEntity())) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.hasPassenger(entity) || this.isPassengerOfSameVehicle(entity)) {
            return false;
        }
        return super.doHurtTarget(entity);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.MEAT);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Dashing", this.isDashing());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setDashing(compound.getBoolean("Dashing"));
    }

    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (this.hasPassenger(passenger)) {
            double yOffset = -passenger.getBbHeight() + 0.1D;
            callback.accept(passenger, this.getX(), this.getY() + yOffset, this.getZ());
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isTame() && this.isBaby() && target instanceof Harpy harpy && !harpy.isBaby()) {
            return false;
        }
        return super.canAttack(target);
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        if (target instanceof Creeper || target instanceof Ghast || target instanceof ArmorStand) {
            return false;
        } else if (target instanceof Harpy harpy) {
            if (this.isBaby() && !harpy.isBaby()) {
                return false;
            }
            return !harpy.isTame() || harpy.getOwner() != owner;
        } else {
            if (target instanceof Player player && owner instanceof Player player1 && !player1.canHarmPlayer(player)) {
                return false;
            }

            if (target instanceof AbstractHorse abstracthorse && abstracthorse.isTamed()) {
                return false;
            }

            if (target instanceof TamableAnimal tamableanimal && tamableanimal.isTame()) {
                return false;
            }

            return true;
        }
    }
}
