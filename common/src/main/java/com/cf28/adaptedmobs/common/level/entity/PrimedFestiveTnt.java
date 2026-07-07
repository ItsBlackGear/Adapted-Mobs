package com.cf28.adaptedmobs.common.level.entity;

import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrimedFestiveTnt extends Entity {
    private static final EntityDataAccessor<Integer> DATA_FUSE = SynchedEntityData.defineId(PrimedFestiveTnt.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_CHARGED = SynchedEntityData.defineId(PrimedFestiveTnt.class, EntityDataSerializers.BOOLEAN);
    @Nullable private LivingEntity owner;

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
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    private void explode() {
        Level.ExplosionInteraction interaction = Level.ExplosionInteraction.NONE;
        this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), 3 * (this.isCharged() ? 2.0F : 1.0F), interaction);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        tag.putShort("Fuse", (short)this.getFuse());
        tag.putBoolean("Charged", this.isCharged());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        this.setFuse(tag.getShort("Fuse"));
        this.setCharged(tag.getBoolean("Charged"));
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setFuse(int life) {
        this.entityData.set(DATA_FUSE, life);
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE);
    }

    public void setCharged(boolean charged) {
        this.entityData.set(DATA_CHARGED, charged);
    }

    public boolean isCharged() {
        return this.entityData.get(DATA_CHARGED);
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity);
    }
}