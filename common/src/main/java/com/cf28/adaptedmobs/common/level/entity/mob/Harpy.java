package com.cf28.adaptedmobs.common.level.entity.mob;

import com.cf28.adaptedmobs.common.level.entity.ai.HarpyAvoidGolemGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.HarpyBroodGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.HarpyLiftGoal;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMStructureTypes;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.tags.AMBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

public class Harpy extends TamableAnimal implements FlyingAnimal {
    private static final int NEST_CHUNK_RADIUS = 3;
    private static final int NEST_SCAN_INTERVAL = 100;
    private static final float WINGBEAT_RATE = 0.55F;
    private static final int GROUND_HYSTERESIS = 3;
    private static final int GLIDE_TICKS = 30;
    private static final double SLOW_FALL_DAMPING = 0.94D;
    private static final int RIDE_TOGGLE_COOLDOWN = 20;
    private static final double PERCH_HEIGHT_RATIO = 0.95D;
    private static final int PASSENGER_RESYNC_INTERVAL = 100;
    private static final int NATURAL_COUNT_CACHE_TICKS = 40;

    private static final Map<ServerLevel, NaturalCount> NATURAL_COUNTS = new WeakHashMap<>();
    private final FlyingPathNavigation flyingNavigation;
    private final GroundPathNavigation groundNavigation;
    private final MoveControl flyingMoveControl;
    private final MoveControl groundMoveControl;
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    private float flapping = 1.0F;
    private float nextFlap = 1.0F;
    private int nestScanCooldown;
    private boolean nestNearby;
    private int groundedTicks;
    private int rideToggleCooldown;
    private boolean awaitingShiftRelease;
    private int glideTicks;
    private boolean fleeingGolem;
    private boolean naturallySpawned;
    private BlockPos broodingEggPos;

    public Harpy(EntityType<? extends Harpy> type, Level level) {
        super(type, level);
        this.flyingNavigation = (FlyingPathNavigation) this.navigation;
        this.flyingMoveControl = new HarpyFlyControl(this);
        this.groundNavigation = new GroundPathNavigation(this, level);
        this.groundMoveControl = new MoveControl(this);
        this.moveControl = this.flyingMoveControl;
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.nestScanCooldown = this.random.nextInt(NEST_SCAN_INTERVAL);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0)
                .add(Attributes.FLYING_SPEED, 0.5F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    public static boolean checkHarpySpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (isNaturalSpawn(spawnType)) {
            if (pos.getY() < AdaptedMobs.CONFIG.harpyMinimumSpawnY.get()) {
                return false;
            }

            if (level.getLevel().isDay() && !level.getBiome(pos).is(AMBiomeTags.HARPY_DAYTIME_SPAWNS)) {
                return false;
            }

            if (spawnType == MobSpawnType.NATURAL && naturalHarpyCount(level.getLevel()) >= AdaptedMobs.CONFIG.maxNaturalHarpies.get()) {
                return false;
            }
        }

        return level.getBlockState(pos.below()).isValidSpawn(level, pos, type);
    }

    private static boolean isNaturalSpawn(MobSpawnType spawnType) {
        return spawnType == MobSpawnType.NATURAL || spawnType == MobSpawnType.CHUNK_GENERATION;
    }

    private static int naturalHarpyCount(ServerLevel level) {
        long gameTime = level.getGameTime();
        NaturalCount cached = NATURAL_COUNTS.get(level);
        if (cached != null && gameTime - cached.gameTime() < NATURAL_COUNT_CACHE_TICKS) {
            return cached.count();
        }

        int count = level.getEntities(AMEntityTypes.HARPY.get(), harpy -> harpy.isAlive() && harpy.isNaturallySpawned()).size();
        NATURAL_COUNTS.put(level, new NaturalCount(count, gameTime));
        return count;
    }

    private static void sendPassengerList(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetPassengersPacket(serverPlayer));
        }
    }

    public boolean isNaturallySpawned() {
        return this.naturallySpawned;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.naturallySpawned = isNaturalSpawn(spawnType);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("NaturallySpawned", this.naturallySpawned);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.naturallySpawned = tag.getBoolean("NaturallySpawned");
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
        this.goalSelector.addGoal(1, new HarpyAvoidGolemGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.5D) {
            @Override
            public boolean canUse() {
                return Harpy.this.isBaby() && super.canUse();
            }
        });
        this.goalSelector.addGoal(4, new HarpyLiftGoal(this, 1.5D, false));
        this.goalSelector.addGoal(5, new HarpyBroodGoal(this));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1D) {
            @Override
            public boolean canUse() {
                return Harpy.this.isBaby() && !Harpy.this.isTame() && super.canUse();
            }
        });
        this.goalSelector.addGoal(7, new TemptGoal(this, 1.1D, this::isFood, false) {
            @Override
            public boolean canUse() {
                return Harpy.this.isBaby() && super.canUse();
            }
        });
        this.goalSelector.addGoal(8, new WaterAvoidingRandomFlyingGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return !Harpy.this.isBaby() && super.canUse();
            }
        });
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return Harpy.this.isBaby() && super.canUse();
            }
        });
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                return !Harpy.this.isBaby() && super.canUse();
            }
        });
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> this.canHunt()));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Villager.class, 10, true, false, (entity) -> this.canHunt()));
    }

    private boolean canHunt() {
        return !this.isTame() && !this.isBaby() && !this.fleeingGolem;
    }

    public boolean isFleeingGolem() {
        return this.fleeingGolem;
    }

    public void setFleeingGolem(boolean fleeingGolem) {
        this.fleeingGolem = fleeingGolem;
    }

    @Nullable
    public BlockPos getBroodingEggPos() {
        return this.broodingEggPos;
    }

    public void setBroodingEggPos(@Nullable BlockPos broodingEggPos) {
        this.broodingEggPos = broodingEggPos;
    }

    @Override
    public void tick() {
        if (this.isVehicle()) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.25D, this.getDeltaMovement().z);
            if (!this.level().isClientSide && (this.getDistanceToGround(this.blockPosition()) >= 20.0D || !this.level().isEmptyBlock(this.blockPosition().above()))) {
                this.ejectPassengers();
            }
        }

        super.tick();
    }

    public double getDistanceToGround(BlockPos pos) {
        for (int i = 0; i < 64; ++i) {
            BlockPos currentPos = pos.below(i);
            if (!this.level().isEmptyBlock(currentPos)) {
                return this.distanceToSqr(currentPos.getX(), currentPos.getY(), currentPos.getZ());
            }
        }
        return 20.0D;
    }

    @Override
    public void aiStep() {
        this.updateControlsForAge();
        super.aiStep();
        this.updateGroundedTicks();
        this.calculateFlapping();

        if (this.rideToggleCooldown > 0) {
            this.rideToggleCooldown--;
        }

        if (!this.level().isClientSide) {
            this.tickNestTracking();
        }

        if (this.isTame() && this.getVehicle() instanceof Player player) {
            this.tickCarriedByOwner(player);
        } else {
            this.glideTicks = 0;
            this.awaitingShiftRelease = false;
        }
    }

    private void updateGroundedTicks() {
        if (this.onGround()) {
            if (this.groundedTicks < GROUND_HYSTERESIS) {
                this.groundedTicks++;
            }
        } else if (this.groundedTicks > 0) {
            this.groundedTicks--;
        }
    }

    @Override
    public void stopRiding() {
        Entity vehicle = this.getVehicle();
        super.stopRiding();
        if (vehicle instanceof Player player) {
            sendPassengerList(player);
        }
    }

    @Override
    public void rideTick() {
        super.rideTick();

        if (!(this.getVehicle() instanceof Player player)) {
            return;
        }

        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);
        this.setPos(player.getX(), player.getY() + player.getBbHeight() * PERCH_HEIGHT_RATIO, player.getZ());

        if (this.tickCount % PASSENGER_RESYNC_INTERVAL == 0) {
            sendPassengerList(player);
        }

        this.yBodyRot = player.yBodyRot;
        this.yBodyRotO = player.yBodyRotO;
        this.setYRot(player.yBodyRot);
        this.yRotO = player.yBodyRotO;
        this.setYHeadRot(player.yBodyRot);
        this.yHeadRotO = player.yBodyRotO;
        this.setXRot(0.0F);
        this.xRotO = 0.0F;
    }

    private void tickCarriedByOwner(Player player) {
        if (player.isCrouching()) {
            if (!this.awaitingShiftRelease && this.rideToggleCooldown <= 0 && !this.level().isClientSide) {
                this.stopRiding();
            }
            return;
        }

        this.awaitingShiftRelease = false;

        if (player.onGround()) {
            this.glideTicks = 0;
            return;
        }

        Vec3 motion = player.getDeltaMovement();
        if (motion.y >= 0.0D) {
            return;
        }

        this.glideTicks++;
        if (this.glideTicks <= GLIDE_TICKS) {
            player.setDeltaMovement(motion.x * 1.02D, Math.max(motion.y, -0.05D), motion.z * 1.02D);
        } else {
            player.setDeltaMovement(motion.x, motion.y * SLOW_FALL_DAMPING, motion.z);
        }
        player.fallDistance = 0.0F;
    }

    private void updateControlsForAge() {
        if (this.isBaby()) {
            if (this.navigation != this.groundNavigation) {
                this.navigation = this.groundNavigation;
                this.moveControl = this.groundMoveControl;
                this.setNoGravity(false);
            }
        } else if (this.navigation != this.flyingNavigation) {
            this.navigation = this.flyingNavigation;
            this.moveControl = this.flyingMoveControl;
        }
    }

    private void calculateFlapping() {
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        boolean airborne = this.isFlying();
        this.flapSpeed = this.flapSpeed + (float) (airborne ? 4 : -1) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (airborne && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }
        this.flapping *= 0.9F;
        this.flap = this.flap + this.flapping * WINGBEAT_RATE;
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
        return !this.isBaby() && !this.isPassenger() && this.groundedTicks < GROUND_HYSTERESIS;
    }

    public boolean isPerched() {
        return this.isInSittingPose() || this.isPassenger();
    }

    public boolean hasNestNearby() {
        return this.nestNearby;
    }

    private void tickNestTracking() {
        if (this.nestScanCooldown > 0) {
            this.nestScanCooldown--;
            return;
        }
        this.nestScanCooldown = NEST_SCAN_INTERVAL;
        this.nestNearby = this.isNearNestStructure();
    }

    private boolean isNearNestStructure() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        Structure nest = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE).get(AMStructureTypes.HARPY_NEST_STRUCTURE);
        if (nest == null) {
            return false;
        }

        ChunkPos center = this.chunkPosition();
        for (int chunkX = center.x - NEST_CHUNK_RADIUS; chunkX <= center.x + NEST_CHUNK_RADIUS; chunkX++) {
            for (int chunkZ = center.z - NEST_CHUNK_RADIUS; chunkZ <= center.z + NEST_CHUNK_RADIUS; chunkZ++) {
                if (!serverLevel.hasChunk(chunkX, chunkZ)) {
                    continue;
                }

                ChunkAccess chunk = serverLevel.getChunk(chunkX, chunkZ);
                if (chunk.getStartForStructure(nest) != null || !chunk.getReferencesForStructure(nest).isEmpty()) {
                    return true;
                }
            }
        }

        return false;
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

        if (this.isTame() && this.isOwnedBy(player) && itemstack.isEmpty()) {
            if (player.isShiftKeyDown()) {
                if (!this.level().isClientSide) {
                    if (this.isPassenger()) {
                        this.stopRiding();
                    } else {
                        this.setOrderedToSit(false);
                        this.startRiding(player, true);
                        sendPassengerList(player);
                        this.rideToggleCooldown = RIDE_TOGGLE_COOLDOWN;
                        this.awaitingShiftRelease = true;
                        this.glideTicks = 0;
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            if (!this.level().isClientSide) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return AMEntityTypes.HARPY.get().create(level);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide && this.isVehicle()) {
            this.ejectPassengers();
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
    protected void pushEntities() {
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.MEAT);
    }

    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float partialTick) {
        if (this.isVehicle() && !this.getPassengers().isEmpty() && this.getPassengers().getFirst() != null) {
            return new Vec3(0.0, this.getPassengers().getFirst().getBbHeight(), 0.0);
        }
        return super.getPassengerAttachmentPoint(passenger, dimensions, partialTick);
    }

    @Override
    protected void positionRider(Entity entity, Entity.MoveFunction moveFunction) {
        super.positionRider(entity, moveFunction);
        if (entity instanceof LivingEntity) {
            Vec3 attachment = this.getPassengerAttachmentPoint(entity, entity.getDimensions(entity.getPose()), 0.0F);
            moveFunction.accept(entity, this.getX(), this.getY() - attachment.y, this.getZ());
            if (entity.isShiftKeyDown()) {
                entity.setShiftKeyDown(false);
            }
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
            return switch (target) {
                case Player player when owner instanceof Player player1 && !player1.canHarmPlayer(player) -> false;
                case AbstractHorse abstracthorse when abstracthorse.isTamed() -> false;
                case TamableAnimal tamableanimal when tamableanimal.isTame() -> false;
                default -> true;
            };
        }
    }

    private record NaturalCount(int count, long gameTime) {
    }

    public static class HarpyFlyControl extends MoveControl {
        public HarpyFlyControl(Harpy harpy) {
            super(harpy);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                this.operation = Operation.WAIT;
                this.mob.setNoGravity(true);
                double dx = this.wantedX - this.mob.getX();
                double dy = this.wantedY - this.mob.getY();
                double dz = this.wantedZ - this.mob.getZ();
                double distSqr = dx * dx + dy * dy + dz * dz;
                if (distSqr < 2.5E-7) {
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                    return;
                }

                float targetYaw = (float) (Mth.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetYaw, 10.0F));
                float speed;
                if (this.mob.onGround()) {
                    speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    this.mob.setSpeed(speed);
                } else {
                    speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
                    this.mob.setSpeed(speed * 2.5F);
                }

                double horizontalDist = Math.sqrt(dx * dx + dz * dz);
                float targetPitch = (float) (-(Mth.atan2(dy, horizontalDist) * (180.0D / Math.PI)));
                this.mob.setXRot(this.rotlerp(this.mob.getXRot(), targetPitch, 10.0F));
                this.mob.setYya(dy > 0.0D ? speed : -speed);
            } else {
                this.mob.setNoGravity(false);
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
            }
        }
    }
}
