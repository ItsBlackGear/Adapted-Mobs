package com.cf28.adaptedmobs.common.level.entity.mob;

import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public BlockState getDisguiseBlock() { return this.getEntityData().get(DISGUISED_BLOCK); }
    void setDisguiseBlock(BlockState blockState) { this.getEntityData().set(DISGUISED_BLOCK, blockState); }

    @Override protected float getJumpPower() { return this.getJumpPower(1.15f); }
    @Override public @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return super.getDefaultDimensions(pose);
    }

    private static final EntityDataAccessor<BlockState> DISGUISED_BLOCK =
        SynchedEntityData.defineId(Mindmould.class, EntityDataSerializers.BLOCK_STATE);
    @Override protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DISGUISED_BLOCK, Blocks.AIR.defaultBlockState());
    }

    @Override
    public void tick() {
        // disguised block
        if (this.level() instanceof ServerLevel serverLevel) {
            BlockState blockState = serverLevel.getBlockState(this.blockPosition().below());
            if (!blockState.is(Blocks.AIR)) this.setDisguiseBlock(blockState);
        }
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
    public @Nullable SpawnGroupData finalizeSpawn(
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

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.28D)
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            .add(Attributes.ARMOR, 2.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
}
