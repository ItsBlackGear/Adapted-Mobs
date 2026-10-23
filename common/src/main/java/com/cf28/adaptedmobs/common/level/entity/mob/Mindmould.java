package com.cf28.adaptedmobs.common.level.entity.mob;

import com.cf28.adaptedmobs.common.level.entity.ai.mindmould.MindmouldAI;
import com.cf28.adaptedmobs.common.level.entity.ai.pathfinding.move_control.MindmouldMoveControl;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.cf28.adaptedmobs.core.tags.AMBlockTags;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Mindmould extends AgeableMob implements Enemy {
    @SuppressWarnings("deprecation")
    public Mindmould(EntityType<? extends AgeableMob> entityType, Level level) {
        super(entityType, level);
        this.fixupDimensions();

        this.moveControl = new MindmouldMoveControl(this);
    }

    public Optional<Player> getTargetPlayer() {
        if (!(this.getTargetFromBrain() instanceof Player player))
            return Optional.empty();
        return Optional.of(player);
    }

    protected @NotNull ParticleOptions getParticleType() {
        if (this.isDisguised()) {
            return new BlockParticleOption(ParticleTypes.BLOCK, this.getDisguiseBlock());
        }
        return AMParticles.BRAIN_GOO.get();
    }
    @Override public @NotNull SoundSource getSoundSource() { return SoundSource.HOSTILE; }

    public int getJumpDelay() {
        return this.random.nextInt(20) + 10;
    }

    @Override
    protected Brain.@NotNull Provider<Mindmould> brainProvider() {
        return MindmouldAI.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return MindmouldAI.makeBrain(brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked") @Override
    public @NotNull Brain<Mindmould> getBrain() {
        return (Brain<Mindmould>) super.getBrain();
    }

    @Override public void customServerAiStep() {
        super.customServerAiStep();
        ServerLevel level = (ServerLevel) this.level();
        getBrain().tick(level, this);
        MindmouldAI.updateActivity(this);
    }

    @Override
    public @NotNull Vec3 getVehicleAttachmentPoint(@NotNull Entity entity) {
        return super.getVehicleAttachmentPoint(entity).add(0, 0.5, 0);
    }

    private static final class TransformContext {
        private final BlockState transformTo;

        private static float getTransformTicks() { return 40.0f; }
        boolean validate(BlockState currentTranform, float partialTick) {
            if (transformTo.isAir()) return false;
            if (currentTranform.is(transformTo.getBlock())) {
                transformTime = Math.min(transformTime + (partialTick / getTransformTicks()), 1.0f);
                return true;
            }
            return false;
        }

        void undisguise(float partialTick) {
            transformTime = Math.max(transformTime - (partialTick / getTransformTicks()), 0.0f);
        }

        float transformTime = 0.0f;
        TransformContext(BlockState transformTo) {
            this.transformTo = transformTo;
        }
    }

    private TransformContext clientTransformContext;

    public void updateClientDisguise(float partialTick) {
        BlockState disguiseBlock = this.getDisguiseBlock();
        if (clientTransformContext == null) {
            clientTransformContext = new TransformContext(disguiseBlock);
            if (!disguiseBlock.isAir()) clientTransformContext.transformTime = 1.0f;
        }

        if (clientTransformContext.validate(disguiseBlock, partialTick)) return;
        else if (clientTransformContext.transformTime > 0f || !this.canUpdateClientDisguise()) {
            clientTransformContext.undisguise(partialTick);
            return;
        }
        clientTransformContext = new TransformContext(disguiseBlock);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean canUpdateClientDisguise() { return this.isDisguised() && !this.isDummy(); }
    public float getTransformTime() { return clientTransformContext.transformTime; }
    public BlockState getClientBlock() { return clientTransformContext.transformTo; }

    public BlockState getDisguiseBlock() { return this.getEntityData().get(DISGUISED_BLOCK); }
    public void setDisguiseBlock(BlockState blockState) { this.getEntityData().set(DISGUISED_BLOCK, blockState); }

    private int onGroundTicks = 0;
    public int getGroundTicks() { return onGroundTicks; }
    public boolean isDisguised() { return !this.getDisguiseBlock().isAir(); }
    public void clearDisguise() {
        this.getEntityData().set(DISGUISED_BLOCK, Blocks.AIR.defaultBlockState());
        this.onGroundTicks = 0;
    }

    private static final String DUMMY_NAME = "Lizzy"; // callie said I could have the easter egg :)
    public boolean isDummy() { return this.getEntityData().get(IS_DUMMY); }
    private void setDummy(boolean isDummy) { this.getEntityData().set(IS_DUMMY, isDummy); }

    @Override protected float getJumpPower() { return super.getJumpPower(1.05f); }
    @Override public @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return super.getDefaultDimensions(pose);
    }

    private static final EntityDataAccessor<BlockState> DISGUISED_BLOCK =
        SynchedEntityData.defineId(Mindmould.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Boolean> IS_DUMMY =
        SynchedEntityData.defineId(Mindmould.class, EntityDataSerializers.BOOLEAN);
    @Override protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DISGUISED_BLOCK, Blocks.AIR.defaultBlockState());
        builder.define(IS_DUMMY, false);
    }

    public static boolean isValidDisguiseBlock(ServerLevel serverLevel, BlockPos blockPos) {
        BlockState blockState = serverLevel.getBlockState(blockPos);
        if (blockState.is(AMBlockTags.MINDMOULD_FACADE_BLACKLIST)) return false;
        return blockState.isCollisionShapeFullBlock(serverLevel, blockPos);
    }

    // for future sable support maybe... it might require more rewriting than just this
    public BlockPos attemptDisguiseBlockPosition() {
        return this.blockPosition().below();
    }

    @Override public float getSoundVolume() {
        return super.getSoundVolume();
    }

    private static boolean canAlign(ServerLevel level, Mindmould mindmould, BlockPos feetPosition) {
        Vec3 center = Vec3.atBottomCenterOf(feetPosition);
        AABB bounds = mindmould.getBoundingBox().expandTowards(center.subtract(mindmould.position()));
        return level.noCollision(mindmould, bounds);
    }

    public static boolean canDisguiseOn(ServerLevel serverLevel, Mindmould mindmould, BlockPos blockPos) {
        if (!isValidDisguiseBlock(serverLevel, blockPos)) return false;
        AABB bounds = mindmould.getDimensions(mindmould.getPose())
            .makeBoundingBox(Vec3.atBottomCenterOf(blockPos.above()));
        return serverLevel.noCollision(mindmould, bounds);
    }

    public static @Nullable BlockState attemptDisguise(ServerLevel serverLevel, Mindmould mindmould, BlockPos blockPos) {
        if (!canDisguiseOn(serverLevel, mindmould, blockPos)) return null;
        if (!canAlign(serverLevel, mindmould, blockPos.above())) return null;
        return serverLevel.getBlockState(blockPos);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (super.hurt(source, amount)) {
            this.clearDisguise();
            this.stopRiding();
            return true;
        }
        return false;
    }

    // stolen from harpy but i dont want to change its code and also it should really be
    // in like its own common / util class if you are reading this thanks :)
    private void sendPassengers(ServerPlayer serverPlayer) {
        serverPlayer.connection.send(new ClientboundSetPassengersPacket(serverPlayer));
    }

    @Override public boolean startRiding(@NotNull Entity vehicle, boolean force) {
        boolean riding = super.startRiding(vehicle, force);
        if (riding && vehicle instanceof ServerPlayer serverPlayer)
            this.sendPassengers(serverPlayer);
        return riding;
    }

    @Override public void stopRiding() {
        Entity vehicle = this.getVehicle();
        super.stopRiding();

        if (vehicle instanceof ServerPlayer serverPlayer)
            this.sendPassengers(serverPlayer);
    }

    @Override
    public void remove(Entity.@NotNull RemovalReason reason) {
        if (!this.isDeadOrDying() || this.isBaby() || this.level().isClientSide) {
            super.remove(reason);
            return;
        }

        Component nameComponent = this.getCustomName();
        for (int i = 0; i < this.random.nextIntBetweenInclusive(1, 3); i++) {
            Mindmould mindmould = (Mindmould) this.getType().create(this.level());
            if (mindmould == null) continue;

            mindmould.setCustomName(nameComponent);
            mindmould.setNoAi(this.isNoAi());
            mindmould.setInvisible(this.isInvulnerable());
            mindmould.setBaby(true);

            if (this.isPersistenceRequired()) mindmould.setPersistenceRequired();

            double distanceMultiplier = 0.25;
            mindmould.moveTo(this.position().add(
                this.random.nextDouble() * distanceMultiplier, 0.5D,
                this.random.nextDouble() * distanceMultiplier
            ));

            this.level().addFreshEntity(mindmould);
        }
        super.remove(reason);
    }

    @Override protected boolean shouldDropLoot() { return true; }

    private int mergeCooldown = 120;
    public int getMergeCooldown() { return mergeCooldown; }

    private @Nullable Mindmould mergePartner;
    public @Nullable Mindmould getMergePartner() { return mergePartner; }
    public void setMergePartner(@Nullable Mindmould mergePartner) { this.mergePartner = mergePartner; }

    @Override public void push(@NotNull Entity entity) {
        if (entity == this.mergePartner
            || entity instanceof Mindmould potentialPartner
            && potentialPartner.getMergePartner() == this
        ) return;
        super.push(entity);
    }

    @Override
    public void tick() {
//        this.squish += (this.targetSquish - this.squish) * 0.5F;
//        this.oSquish = this.squish;
        super.tick();

        if (this.isDummy() && this.isDisguised()) this.clearDisguise();

        if (this.onGround()) {
            if (this.onGroundTicks == 0) {
                this.spawnParticles();

                this.playSound(
                    this.isBaby() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP,
                    this.getSoundVolume(), ((this.random.nextFloat()
                        - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F
                );
//            this.targetSquish = -0.5F;
            }
            this.onGroundTicks = Math.min(this.onGroundTicks + 1, 20);
        } else {
            // mace :)
//            this.clearDisguise();

//            this.targetSquish = 1.0F;
            if (this.onGroundTicks > 0) this.onGroundTicks = -1;
        }
        mergeCooldown = Math.max(mergeCooldown - 1, 0);
//        this.decreaseSquish();
    }

    private void spawnParticles() {
        float spread = 1f;
        int particleCount = 10;
        if (this.isBaby()) {
            particleCount = 6;
            spread /= 2f;
        }

        for (int i = 0; i < particleCount; ++i) {
            float f2 = this.random.nextFloat() * ((float) Math.PI * 2F);
            float f3 = ((this.random.nextFloat() * 0.5F) + 0.5F) * spread;
            float f4 = Mth.sin(f2) * f3;
            float f5 = Mth.cos(f2) * f3;
            this.level().addParticle(
                this.getParticleType(), this.getX() + f4,
                this.getY(), this.getZ() + f5,
                0.0F, 0.0F, 0.0F
            );
        }
    }

    @Override
    public void baseTick() {
        this.setAirSupply(this.getMaxAirSupply());
        super.baseTick();
    }

    private void toCardinalDirection() {
        float yaw = Mth.wrapDegrees(this.getYRot());
        float snappedYaw = Mth.wrapDegrees(Math.round(yaw / 90.0f) * 90.0f);

        this.setYRot(snappedYaw);
        this.setYBodyRot(snappedYaw);
        this.setYHeadRot(snappedYaw);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
    ) {
        this.toCardinalDirection();
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Mindmould babyMindmould = AMEntityTypes.MINDMOULD.get().create(serverLevel);
        if (babyMindmould != null) babyMindmould.toCardinalDirection();
        return babyMindmould;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.put("MindmouldFacadeState", NbtUtils.writeBlockState(this.getDisguiseBlock()));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setDisguiseBlock(NbtUtils.readBlockState(
            this.level().holderLookup(Registries.BLOCK),
            compoundTag.getCompound("MindmouldFacadeState")
        ));
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.setDummy(name != null && name.getString().equalsIgnoreCase(DUMMY_NAME));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.5D)
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            .add(Attributes.ARMOR, 2.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
}
