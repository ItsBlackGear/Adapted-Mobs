package com.cf28.adaptedmobs.common.entity;

import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
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
        double offset = level.random.nextDouble() * (float)(Math.PI * 2);
        this.setDeltaMovement(-Math.sin(offset) * 0.02D, 0.2F, -Math.cos(offset) * 0.02D);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_FUSE, 80);
        this.entityData.define(DATA_CHARGED, false);
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        int fuse = this.getFuse() - 1;
        this.setFuse(fuse);
        if (fuse <= 0) {
            this.discard();
            if (!this.level.isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    private void explode() {
        Explosion.BlockInteraction interaction = this.isCharged() && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE;
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), this.isCharged() ? 4.0F : 2.0F, interaction);
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

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.15F;
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
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}