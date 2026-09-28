package com.cf28.adaptedmobs.common.level.entity.mob;

import com.cf28.adaptedmobs.common.integrations.LambDynLightsCompat;
import com.cf28.adaptedmobs.common.level.entity.ai.EntombedAttackGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.EntombedAvoidLightGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.EntombedNavigation;
import com.cf28.adaptedmobs.common.level.item.mask.MaskVariant;
import com.cf28.adaptedmobs.common.registries.AMItems;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Entombed extends Monster {
    public static final int MAX_COMFORT_LIGHT = 4;
    public static final int BURNING_LIGHT = 15;

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(Entombed.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_AFRAID_OF_LIGHT = SynchedEntityData.defineId(Entombed.class, EntityDataSerializers.BOOLEAN);

    public Entombed(EntityType<? extends Entombed> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 6;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    public static boolean checkEntombedSpawnRules(EntityType<Entombed> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (pos.getY() > AdaptedMobs.CONFIG.entombedMaximumSpawnY.get()) {
            return false;
        }
        if (level.getRawBrightness(pos, 0) > MAX_COMFORT_LIGHT) {
            return false;
        }
        return checkMonsterSpawnRules(entityType, (ServerLevelAccessor) level, spawnType, pos, random);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new EntombedNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, MaskVariant.ALCHEMIST.getId());
        builder.define(DATA_AFRAID_OF_LIGHT, false);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            amount *= 2.0F;
        }
        return super.hurt(source, amount);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntombedAvoidLightGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new EntombedAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new EntombedAttackTurtleEggGoal(this, 1.0D, 3));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 5, true, false, target -> !this.isTargetInLightOrHoldingLight(target)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, 5, false, false, target -> !this.isTargetInLightOrHoldingLight(target)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, 5, true, false, target -> !this.isTargetInLightOrHoldingLight(target)));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, target -> !this.isTargetInLightOrHoldingLight(target) && Turtle.BABY_ON_LAND_SELECTOR.test(target)));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target != null && (this.isAfraidOfLight() || this.isTargetInLightOrHoldingLight(target))) {
            return;
        }
        super.setTarget(target);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isAfraidOfLight() || this.isTargetInLightOrHoldingLight(target)) {
            return false;
        }
        return super.canAttack(target);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        int light = level.getMaxLocalRawBrightness(pos);
        if (light > MAX_COMFORT_LIGHT) {
            return -10.0F;
        }
        return 10.0F - (float) light;
    }

    public MaskVariant getVariant() {
        return MaskVariant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariant(MaskVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.getId());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("MaskVariant", this.getVariant().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("MaskVariant")) {
            this.setVariant(MaskVariant.byId(tag.getInt("MaskVariant")));
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        RandomSource random = level.getRandom();
        MaskVariant chosen = MaskVariant.getRandomVariant(random);
        this.setVariant(chosen);

        this.populateDefaultEquipmentSlots(random, difficulty);
        return data;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        ItemStack maskStack = new ItemStack(AMItems.getMaskByVariant(this.getVariant()));
        this.setItemSlot(EquipmentSlot.HEAD, maskStack);
        this.setDropChance(EquipmentSlot.HEAD, 0.085F);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide() && this.isAlive()) {
            LivingEntity target = this.getTarget();
            if (target != null && (this.isAfraidOfLight() || this.isTargetInLightOrHoldingLight(target))) {
                this.setTarget(null);
            }

            BlockPos feetPos = this.blockPosition();
            BlockPos eyePos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
            boolean inBurningLight = this.getLightLevelAt(feetPos) >= BURNING_LIGHT || this.getLightLevelAt(eyePos) >= BURNING_LIGHT;

            if (!inBurningLight && LambDynLightsCompat.isLoaded()) {
                Player nearestLightingPlayer = this.getNearestLightingPlayer(3.0D);
                if (nearestLightingPlayer != null && LambDynLightsCompat.getLivingEntityLuminance(nearestLightingPlayer) >= BURNING_LIGHT) {
                    if (nearestLightingPlayer.distanceToSqr(this) <= 2.25D) {
                        inBurningLight = true;
                    }
                }
            }

            boolean afraid = this.isLit() || this.isOnFire() || this.isPositionInPlayerLight(feetPos) || this.isPositionInPlayerLight(eyePos);
            this.setAfraidOfLight(afraid);

            if (!this.isInWaterOrRain() && inBurningLight) {
                this.igniteForSeconds(8);
            }
        }
    }

    public @Nullable Player getNearestLightingPlayer(double range) {
        if (!LambDynLightsCompat.isLoaded()) {
            return null;
        }
        Player nearest = null;
        double nearestDistSq = range * range;
        for (Player player : this.level().players()) {
            if (player.isAlive() && !player.isSpectator()) {
                double distSq = this.distanceToSqr(player);
                if (distSq <= nearestDistSq) {
                    if (LambDynLightsCompat.getLivingEntityLuminance(player) > MAX_COMFORT_LIGHT) {
                        nearest = player;
                        nearestDistSq = distSq;
                    }
                }
            }
        }
        return nearest;
    }

    public boolean isPositionInPlayerLight(BlockPos pos) {
        if (LambDynLightsCompat.isLoaded()) {
            Player player = this.getNearestLightingPlayer(16.0D);
            if (player != null) {
                int luminance = LambDynLightsCompat.getLivingEntityLuminance(player);
                if (luminance > MAX_COMFORT_LIGHT) {
                    int safeRadius = luminance - MAX_COMFORT_LIGHT;
                    return player.blockPosition().distSqr(pos) <= (double) (safeRadius * safeRadius);
                }
            }
        }
        return false;
    }

    public void setAfraidOfLight(boolean afraid) {
        this.entityData.set(DATA_AFRAID_OF_LIGHT, afraid);
    }

    public boolean isAfraidOfLight() {
        if (this.entityData.get(DATA_AFRAID_OF_LIGHT)) {
            return true;
        }
        if (this.isOnFire()) {
            return true;
        }
        if (this.level().isClientSide()) {
            BlockPos feetPos = this.blockPosition();
            BlockPos eyePos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
            return this.isPositionInPlayerLight(feetPos) || this.isPositionInPlayerLight(eyePos);
        }
        return this.isLit();
    }

    public boolean isDaytime() {
        if (this.level().dimensionType().hasFixedTime()) {
            return false;
        }
        return this.level().isDay();
    }

    public int getLightLevelAt(BlockPos pos) {
        int blockLight = this.level().getBrightness(LightLayer.BLOCK, pos);
        if (this.isDaytime()) {
            int skyLight = this.level().getBrightness(LightLayer.SKY, pos);
            if (this.level().isRaining()) {
                skyLight = Math.max(0, skyLight - 2);
            }
            return Math.max(blockLight, skyLight);
        }
        return blockLight;
    }

    public boolean isPosLit(BlockPos pos) {
        return this.getLightLevelAt(pos) > MAX_COMFORT_LIGHT;
    }

    public boolean isLit() {
        BlockPos feetPos = this.blockPosition();
        BlockPos eyePos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
        return this.isPosLit(feetPos) || this.isPosLit(eyePos);
    }

    public boolean isTargetInLightOrHoldingLight(LivingEntity target) {
        if (target == null) {
            return false;
        }
        BlockPos targetPos = target.blockPosition();
        BlockPos eyePos = BlockPos.containing(target.getX(), target.getEyeY(), target.getZ());
        if (this.isPosLit(targetPos) || this.isPosLit(eyePos)) {
            return true;
        }
        return LambDynLightsCompat.isLoaded() && LambDynLightsCompat.getLivingEntityLuminance(target) > 0;
    }

    public boolean isPositionInTargetLight(BlockPos pos, LivingEntity target) {
        if (target == null || !LambDynLightsCompat.isLoaded()) {
            return false;
        }
        int luminance = LambDynLightsCompat.getLivingEntityLuminance(target);
        if (luminance > MAX_COMFORT_LIGHT) {
            int safeRadius = luminance - MAX_COMFORT_LIGHT;
            return target.blockPosition().distSqr(pos) <= (double) (safeRadius * safeRadius);
        }
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.DEEPSLATE_HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DEEPSLATE_BREAK;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    static class EntombedAttackTurtleEggGoal extends RemoveBlockGoal {
        EntombedAttackTurtleEggGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
            super(Blocks.TURTLE_EGG, mob, speedModifier, verticalSearchRange);
        }

        @Override
        public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + this.mob.getRandom().nextFloat() * 0.2F);
        }

        @Override
        public void playBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        }

        @Override
        public double acceptedDistance() {
            return 1.14D;
        }
    }
}
