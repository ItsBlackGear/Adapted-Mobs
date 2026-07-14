package com.cf28.adaptedmobs.common.level.entity;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrimedFestiveTnt extends Entity {
    private static final EntityDataAccessor<Integer> DATA_FUSE = SynchedEntityData.defineId(PrimedFestiveTnt.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_CHARGED = SynchedEntityData.defineId(PrimedFestiveTnt.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SMALL = SynchedEntityData.defineId(PrimedFestiveTnt.class, EntityDataSerializers.BOOLEAN);
    @Nullable
    private LivingEntity owner;

    public PrimedFestiveTnt(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public PrimedFestiveTnt(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(AMEntityTypes.FESTIVE_TNT.get(), level);
        this.setPos(x, y, z);
        double offset = level.random.nextDouble() * (float) (Math.PI * 2);
        this.setDeltaMovement(-Math.sin(offset) * 0.02, 0.2F, -Math.cos(offset) * 0.02);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_FUSE, 80);
        builder.define(DATA_CHARGED, false);
        builder.define(DATA_SMALL, false);
    }

    @Override
    protected @NotNull MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
        }

        Vec3 delta = this.getDeltaMovement();
        double horizontalDistance = delta.horizontalDistance();
        if (horizontalDistance > 1.0E-4 || Math.abs(delta.y) > 1.0E-4) {
            this.xRotO = this.getXRot();
            this.setXRot((float) (Mth.atan2(delta.y, horizontalDistance) * (180.0 / Math.PI)));
        }

        int fuse = this.getFuse() - 1;
        this.setFuse(fuse);
        if (fuse <= 0) {
            this.discard();
            if (!this.level().isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level().isClientSide) {
                if (this.isSmall()) {
                    this.level().addParticle(AMParticles.FESTIVE_TNT_PARTICLETRAIL.get(), this.getX(), this.getY() + 0.25, this.getZ(), 0.0, 0.0, 0.0);
                } else {
                    this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    private void explode() {
        Level.ExplosionInteraction interaction = Level.ExplosionInteraction.NONE;
        float radius = this.isSmall() ? 1.5F : 3.0F;
        this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), radius * (this.isCharged() ? 2.0F : 1.0F), interaction);

        if (!this.level().isClientSide && TolerableCreepersCompat.isLoaded()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            TolerableCreepersIntegration.spawnParticleRing(serverLevel, AMParticles.FESTIVE_SPORES.get(), this.position().add(0.0, 0.1, 0.0), radius * 0.7, 24);
            TolerableCreepersIntegration.spawnParticleCircle(serverLevel, AMParticles.FESTIVE_SPORES.get(), this.random, this.position().add(0.0, 0.1, 0.0), radius * 0.7, 30);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        tag.putShort("Fuse", (short) this.getFuse());
        tag.putBoolean("Charged", this.isCharged());
        tag.putBoolean("Small", this.isSmall());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        this.setFuse(tag.getShort("Fuse"));
        this.setCharged(tag.getBoolean("Charged"));
        this.setSmall(tag.getBoolean("Small"));
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE);
    }

    public void setFuse(int life) {
        this.entityData.set(DATA_FUSE, life);
    }

    public boolean isCharged() {
        return this.entityData.get(DATA_CHARGED);
    }

    public void setCharged(boolean charged) {
        this.entityData.set(DATA_CHARGED, charged);
    }

    public boolean isSmall() {
        return this.entityData.get(DATA_SMALL);
    }

    public void setSmall(boolean small) {
        this.entityData.set(DATA_SMALL, small);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity);
    }
}