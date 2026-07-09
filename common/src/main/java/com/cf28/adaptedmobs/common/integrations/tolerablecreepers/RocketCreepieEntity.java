package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RocketCreepieEntity extends Creepie {
    private static final EntityDataAccessor<Boolean> ROCKETING =
            SynchedEntityData.defineId(RocketCreepieEntity.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState launchAnimationState = new AnimationState();

    private final int launchDelay;
    private int ticksAlive;

    public RocketCreepieEntity(EntityType<? extends Creepie> type, Level level) {
        super(type, level);
        this.setAge(-24000);
        this.setFuseTime(100);
        this.launchDelay = this.random.nextInt(21);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ROCKETING, false);
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public void setAge(int age) {
        if (!this.level().isClientSide() && age >= 0) {
            this.convertTo(AMEntityTypes.ROCKET_CREEPER.get(), false);
            return;
        }
        super.setAge(age);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        this.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE);
        this.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.isRocketing()) {
                this.launchAnimationState.startIfStopped(this.tickCount);
            } else {
                this.launchAnimationState.stop();
            }
        } else {
            if (!this.isRocketing() && this.onGround() && this.ticksAlive >= this.launchDelay) {
                this.launch();
            }
            this.ticksAlive++;
        }
    }

    private void launch() {
        float yaw = this.random.nextFloat() * 360.0F;
        double height = Mth.nextDouble(this.random, 1.0, 4.0);
        double verticalVelocity = Math.sqrt(2 * 0.08 * height);
        this.setDeltaMovement(-Mth.sin(yaw * Mth.DEG_TO_RAD) * 0.3, verticalVelocity, Mth.cos(yaw * Mth.DEG_TO_RAD) * 0.3);
        this.hasImpulse = true;
        this.fallDistance = 0.0F;
        this.setRocketing(true);
        this.setYRot(yaw);
        this.setYBodyRot(yaw);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        if (this.isRocketing()) {
            this.setRocketing(false);
            this.explodeCustom();
            return false;
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.setRocketing(false);
        this.dead = true;
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, Level.ExplosionInteraction.NONE);
        this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST, 1.0F, 1.0F);
        ServerLevel sl = (ServerLevel) this.level();
        TolerableCreepersIntegration.spawnParticleRing(sl, AMParticles.ROCKET_SPORES.get(), this.position().add(0.0, 0.1, 0.0), 0.6, 16);
        this.discard();
    }

    public boolean isRocketing() {
        return this.entityData.get(ROCKETING);
    }

    public void setRocketing(boolean rocketing) {
        this.entityData.set(ROCKETING, rocketing);
    }
}
