package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import com.evandev.tolerable_creepers.core.registry.TCParticles;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SupportCreepieEntity extends Creepie {
    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(SupportCreepieEntity.class, EntityDataSerializers.INT);

    private static final double BUFF_RANGE_SQR = 2.25;
    private static final double BUFF_SEEK_RANGE = 16.0;
    private static final float RING_SPORE_CHANCE = 0.35F;
    private static final double HOSTILE_SWELL_RANGE = 2.0;
    private static final int IGNITED_FUSE_TIME = 24;
    private static final int BUFF_DURATION = 240;
    private static final int DEBUFF_DURATION = 160;
    private static final float BUFF_CLOUD_RADIUS = 1.0F;
    private static final float DEBUFF_CLOUD_RADIUS = 1.5F;
    private static final int LINGERING_CLOUD_DURATION = 50;

    public SupportCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.setFuseTime(24);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, Variant.SPEED.ordinal());
    }

    public Variant getVariant() {
        return Variant.values()[this.entityData.get(DATA_VARIANT)];
    }

    public void setVariant(Variant v) {
        this.entityData.set(DATA_VARIANT, v.ordinal());
        if (v == Variant.SPEED || v == Variant.STRENGTH) {
            this.setFuseTime(IGNITED_FUSE_TIME);
            this.ignite();
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
                                        @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setVariant(Util.getRandom(Variant.values(), this.random));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        Variant variant = this.getVariant();
        if (variant != Variant.SPEED && variant != Variant.STRENGTH) {
            return;
        }

        Entity owner = this.getOwner();
        if (owner == null) {
            owner = this.level().getNearestPlayer(this, BUFF_SEEK_RANGE);
        }

        if (owner == null) {
            return;
        } else if (this.distanceToSqr(owner) <= BUFF_RANGE_SQR) {
            this.explodeCustom();
        } else if (this.getNavigation().isDone()) {
            this.getNavigation().moveTo(owner, 1.2);
        }
    }

    @Override
    public double getSwellRange() {
        return HOSTILE_SWELL_RANGE;
    }

    @Override
    public void setAge(int age) {
        if (!this.level().isClientSide() && age >= 0) {
            this.convertTo(AMEntityTypes.SUPPORT_CREEPER.get(), false);
            return;
        }
        super.setAge(age);
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.dead = true;
        ServerLevel sl = (ServerLevel) this.level();
        Variant variant = this.getVariant();

        if (variant == Variant.SPEED || variant == Variant.STRENGTH) {
            this.playBuffBurstEffects(sl);
            Holder<MobEffect> effect = variant == Variant.SPEED ? MobEffects.MOVEMENT_SPEED : MobEffects.DAMAGE_BOOST;
            for (Player p : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(2.5))) {
                p.addEffect(new MobEffectInstance(effect, BUFF_DURATION, 0, false, false, true));
            }
            SimpleParticleType particle = variant == Variant.SPEED
                    ? AMParticles.SUPPORTED_YELLOW.get() : AMParticles.SUPPORTED_RED.get();
            TolerableCreepersIntegration.spawnParticleRing(sl, this.random, this.position().add(0.0, 0.1, 0.0), 0.6, 16,
                    particle, TCParticles.CREEPER_SPORES.get(), RING_SPORE_CHANCE);
            this.spawnLingeringCloud(sl, particle, BUFF_CLOUD_RADIUS);
        } else {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.5F, Level.ExplosionInteraction.NONE);
            Holder<MobEffect> debuff = variant == Variant.SLOWNESS ? MobEffects.MOVEMENT_SLOWDOWN : MobEffects.WEAKNESS;
            for (Player p : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(3.0))) {
                p.addEffect(new MobEffectInstance(debuff, DEBUFF_DURATION, 0, false, false, true));
            }
            SimpleParticleType particle = variant == Variant.SLOWNESS
                    ? AMParticles.SUPPORTED_BLUE.get() : AMParticles.SUPPORTED_GREY.get();
            TolerableCreepersIntegration.spawnParticleRing(sl, this.random, this.position().add(0.0, 0.1, 0.0), 0.6, 16,
                    particle, TCParticles.CREEPER_SPORES.get(), RING_SPORE_CHANCE);
            this.spawnLingeringCloud(sl, particle, DEBUFF_CLOUD_RADIUS);
        }

        this.discard();
    }

    private void spawnLingeringCloud(ServerLevel level, SimpleParticleType particle, float radius) {
        AreaEffectCloud cloud = new AreaEffectCloud(level, this.getX(), this.getY() + 0.05, this.getZ());
        cloud.setRadius(radius);
        cloud.setWaitTime(0);
        cloud.setDuration(LINGERING_CLOUD_DURATION);
        cloud.setRadiusPerTick(-radius / (float) LINGERING_CLOUD_DURATION);
        cloud.setParticle(particle);
        level.addFreshEntity(cloud);
    }

    private void playBuffBurstEffects(ServerLevel level) {
        level.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0, 0.0, 0.0, 0.0, 0.0);
        level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE,
                SoundSource.NEUTRAL, 2.0F, (1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F) * 0.7F);
    }

    public enum Variant {
        SPEED, STRENGTH, SLOWNESS, WEAKNESS;

        public ResourceLocation getTexture() {
            return ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID,
                    "textures/entity/tolerable_creepers/support_creepie_" + this.name().toLowerCase() + ".png");
        }
    }
}
