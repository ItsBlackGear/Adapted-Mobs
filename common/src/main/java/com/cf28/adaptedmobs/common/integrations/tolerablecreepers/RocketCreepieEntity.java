package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.cf28.adaptedmobs.common.util.RocketFlightMath;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RocketCreepieEntity extends Creepie {
    private static final EntityDataAccessor<Boolean> ROCKETING =
            SynchedEntityData.defineId(RocketCreepieEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> LAUNCH_SPEED =
            SynchedEntityData.defineId(RocketCreepieEntity.class, EntityDataSerializers.FLOAT);
    private static final float ROTATION_TICKS = 30.0F;
    private static final int LANDING_DELAY = 4;

    public final AnimationState launchAnimationState = new AnimationState();

    private final int launchDelay;
    private int ticksAlive;
    private int landingTimer = -1;
    private int flightTicksRemaining = -1;

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
        builder.define(LAUNCH_SPEED, 1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public boolean canFight() {
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
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.isRocketing()) {
                this.launchAnimationState.startIfStopped(this.tickCount);
            } else {
                this.launchAnimationState.stop();
            }
        } else {
            if (this.landingTimer >= 0) {
                if (this.landingTimer-- == 0) {
                    this.explodeCustom();
                }
                return;
            }
            if (this.isRocketing()) {
                this.hasImpulse = true;
                if (--this.flightTicksRemaining <= 0) {
                    this.startLanding();
                }
            } else if (this.onGround() && this.ticksAlive >= this.launchDelay) {
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
        int predictedFlightTicks = RocketFlightMath.predictFlightTicks(verticalVelocity);
        this.flightTicksRemaining = predictedFlightTicks;
        this.setLaunchSpeed(ROTATION_TICKS / predictedFlightTicks);
        this.setYRot(yaw);
        this.setYBodyRot(yaw);
    }

    @Override
    public void setSwellDir(int swellDir) {
        super.setSwellDir(-1);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        if (this.isRocketing()) {
            this.startLanding();
            return false;
        }
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    private void startLanding() {
        this.setDeltaMovement(Vec3.ZERO);
        this.landingTimer = LANDING_DELAY;
    }

    @Override
    protected void explodeCustom() {
        if (this.level().isClientSide()) return;
        this.setRocketing(false);
        this.dead = true;
        this.level().explode(
                this,
                null,
                null,
                this.getX(), this.getY(), this.getZ(),
                1.0F,
                false,
                Level.ExplosionInteraction.NONE,
                ParticleTypes.EXPLOSION,
                ParticleTypes.EXPLOSION_EMITTER,
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.FIREWORK_ROCKET_TWINKLE)
        );
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

    public float getLaunchSpeed() {
        return this.entityData.get(LAUNCH_SPEED);
    }

    public void setLaunchSpeed(float speed) {
        this.entityData.set(LAUNCH_SPEED, speed);
    }
}
