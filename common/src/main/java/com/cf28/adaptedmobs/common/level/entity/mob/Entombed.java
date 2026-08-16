package com.cf28.adaptedmobs.common.level.entity.mob;

import com.cf28.adaptedmobs.common.integrations.LambDynamicLightsIntegration;
import com.cf28.adaptedmobs.common.level.entity.ai.EntombedAvoidLightGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.EntombedStalkTargetGoal;
import com.cf28.adaptedmobs.common.level.item.mask.MaskVariant;
import com.cf28.adaptedmobs.common.registries.AMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class Entombed extends Monster {
    public static final int MAX_COMFORT_LIGHT = 4;
    public static final int BURNING_LIGHT = 14;

    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(Entombed.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_STALKING = SynchedEntityData.defineId(Entombed.class, EntityDataSerializers.BOOLEAN);

    public Entombed(EntityType<? extends Entombed> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 6;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    public static boolean checkEntombedSpawnRules(EntityType<Entombed> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (pos.getY() > 0) {
            return false;
        }
        if (level.getRawBrightness(pos, 0) > MAX_COMFORT_LIGHT) {
            return false;
        }
        return checkMonsterSpawnRules(entityType, (ServerLevelAccessor) level, spawnType, pos, random);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT_ID, MaskVariant.BLANK.getId());
        builder.define(DATA_STALKING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EntombedAvoidLightGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new EntombedStalkTargetGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public MaskVariant getVariant() {
        return MaskVariant.byId(this.entityData.get(DATA_VARIANT_ID));
    }

    public void setVariant(MaskVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, variant.getId());
    }

    public boolean isStalking() {
        return this.entityData.get(DATA_STALKING);
    }

    public void setStalking(boolean stalking) {
        this.entityData.set(DATA_STALKING, stalking);
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
        MaskVariant[] variants = MaskVariant.values();
        MaskVariant chosen = variants[random.nextInt(variants.length)];
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
            int light = this.level().getMaxLocalRawBrightness(this.blockPosition());
            if (light >= BURNING_LIGHT && !this.isInWaterOrRain()) {
                this.igniteForSeconds(8);
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurt = super.doHurtTarget(entity);
        if (hurt && entity instanceof LivingEntity living) {
            float chance = switch (this.level().getDifficulty()) {
                case HARD -> 0.8F;
                case NORMAL -> 0.4F;
                default -> 0.2F;
            };
            if (this.random.nextFloat() < chance) {
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 140, 0), this);
            }
        }
        return hurt;
    }

    public int getLightLevelAt(BlockPos pos) {
        return this.level().getMaxLocalRawBrightness(pos);
    }

    public boolean isLit() {
        return this.getLightLevelAt(this.blockPosition()) > MAX_COMFORT_LIGHT;
    }

    public boolean isTargetInLightOrHoldingLight(LivingEntity target) {
        if (target == null) {
            return false;
        }
        if (this.getLightLevelAt(target.blockPosition()) > MAX_COMFORT_LIGHT) {
            return true;
        }
        return LambDynamicLightsIntegration.getLivingEntityLuminance(target) > 0;
    }

    public boolean isPositionInTargetLight(BlockPos pos, LivingEntity target) {
        if (target == null) {
            return false;
        }
        int luminance = LambDynamicLightsIntegration.getLivingEntityLuminance(target);
        if (luminance > 0) {
            double distanceSq = target.blockPosition().distSqr(pos);
            int safeRadius = Math.max(1, luminance - MAX_COMFORT_LIGHT);
            return distanceSq <= (double) (safeRadius * safeRadius);
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
}
