package com.cf28.adaptedmobs.common.level.entity.mob;

import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.tags.AMBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public class Mindmould extends AgeableMob implements Enemy {
    @SuppressWarnings("deprecation")
    public Mindmould(EntityType<? extends AgeableMob> entityType, Level level) {
        super(entityType, level);
        this.fixupDimensions();
    }

    protected @NotNull ParticleOptions getParticleType() { return AMParticles.BRAIN_GOO.get(); }

    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class,
            10, true, false, (p_352812_) -> Math.abs(p_352812_.getY() - this.getY()) <= (double)4.0F));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
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
        else if (clientTransformContext.transformTime > 0f || !this.canDisguise()) {
            clientTransformContext.undisguise(partialTick);
            return;
        }
        clientTransformContext = new TransformContext(disguiseBlock);
    }

    public float getTransformTime() { return clientTransformContext.transformTime; }
    public BlockState getClientBlock() { return clientTransformContext.transformTo; }
    public BlockState getDisguiseBlock() { return this.getEntityData().get(DISGUISED_BLOCK); }
    void setDisguiseBlock(BlockState blockState) { this.getEntityData().set(DISGUISED_BLOCK, blockState); }
    void clearDisguise() { this.getEntityData().set(DISGUISED_BLOCK, Blocks.AIR.defaultBlockState());}

    private static final String DUMMY_NAME = "Lizzy"; // callie said I could have the easter egg :)
    private boolean isDummy() { return this.getEntityData().get(IS_DUMMY); }
    private void setDummy(boolean isDummy) { this.getEntityData().set(IS_DUMMY, isDummy); }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean canDisguise() { return !this.getDisguiseBlock().isAir() && !this.isDummy(); }

    @Override protected float getJumpPower() { return super.getJumpPower(1.15f); }
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

    private void attemptDisguise() {
        if (this.level() instanceof ServerLevel serverLevel) {
            // mace :)
            if (this.isDummy() || !this.onGround()) {
                this.clearDisguise();
                return;
            }

            BlockPos blockPos = this.blockPosition().below();
            BlockState blockState = serverLevel.getBlockState(blockPos);
            if (this.getDisguiseBlock().is(blockState.getBlock())) return;

            if (blockState.is(AMBlockTags.MINDMOULD_FACADE_BLACKLIST)) return;
            if (!blockState.isCollisionShapeFullBlock(serverLevel, blockPos)) return;

            this.setDisguiseBlock(blockState);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (super.hurt(source, amount)) {
            this.clearDisguise();
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        this.attemptDisguise();
//        AdaptedMobs.LOGGER.info("{}", this.getDisguiseBlock());
        super.tick();
    }

    private void toCardinalDirection() {
        float yaw = this.getYRot();
        while (yaw < 0f) yaw += 360f;
        yaw = yaw % 360;

        float snappedYaw = Math.round(yaw / 90.0f) * 90.0f;
        while (snappedYaw <= 360.0f) snappedYaw += 360.0f;
        snappedYaw = snappedYaw % 360;

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
            .add(Attributes.MOVEMENT_SPEED, 0.28D)
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            .add(Attributes.ARMOR, 2.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
}
