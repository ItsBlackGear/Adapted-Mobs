package com.cf28.adaptedmobs.common.level.entity;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.level.block.AMSporeBarrelBlock;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AMPrimedSporeBarrel extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(AMPrimedSporeBarrel.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_TYPE_ID = SynchedEntityData.defineId(AMPrimedSporeBarrel.class, EntityDataSerializers.STRING);

    @Nullable
    private LivingEntity owner;

    public AMPrimedSporeBarrel(EntityType<? extends AMPrimedSporeBarrel> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public AMPrimedSporeBarrel(Level level, double x, double y, double z, @Nullable LivingEntity owner, AMSporeBarrelBlock.SporeType type) {
        this(AMEntityTypes.PRIMED_SPORE_BARREL.get(), level);
        this.setPos(x, y, z);
        double d = level.random.nextDouble() * (float) (Math.PI * 2);
        this.setDeltaMovement(-Math.sin(d) * 0.02, 0.2, -Math.cos(d) * 0.02);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
        this.setSporeType(type);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_FUSE_ID, 80);
        builder.define(DATA_TYPE_ID, AMSporeBarrelBlock.SporeType.SUPPORT.name());
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
        AMSporeBarrelBlock.SporeType type = this.getSporeType();
        float radius = type == AMSporeBarrelBlock.SporeType.FESTIVE ? 4.0F : 2.5F;
        this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), radius, Level.ExplosionInteraction.NONE);

        if (!this.level().isClientSide() && TolerableCreepersCompat.isLoaded()) {
            if (type == AMSporeBarrelBlock.SporeType.SUPPORT) {
                Entity spores = TolerableCreepersIntegration.createSupportSpores(this.level(), this.getX(), this.getY() + 0.01, this.getZ(), 2, false);
                if (this.owner != null) {
                    TolerableCreepersIntegration.setSporesOwner(spores, this.owner);
                }
                this.level().addFreshEntity(spores);
            } else if (type == AMSporeBarrelBlock.SporeType.ROCKET) {
                Entity spores = TolerableCreepersIntegration.createRocketSpores(this.level(), this.getX(), this.getY() + 0.01, this.getZ(), 2, false);
                if (this.owner != null) {
                    TolerableCreepersIntegration.setSporesOwner(spores, this.owner);
                }
                this.level().addFreshEntity(spores);
            } else if (type == AMSporeBarrelBlock.SporeType.FESTIVE) {
                Entity spores = TolerableCreepersIntegration.createFestiveSpores(this.level(), this.getX(), this.getY() + 0.01, this.getZ(), 2);
                if (this.owner != null) {
                    TolerableCreepersIntegration.setSporesOwner(spores, this.owner);
                }
                this.level().addFreshEntity(spores);
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setFuse(compoundTag.getShort("Fuse"));
        if (compoundTag.contains("SporeType")) {
            try {
                this.setSporeType(AMSporeBarrelBlock.SporeType.valueOf(compoundTag.getString("SporeType")));
            } catch (IllegalArgumentException e) {
                this.setSporeType(AMSporeBarrelBlock.SporeType.SUPPORT);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putShort("Fuse", (short) this.getFuse());
        compoundTag.putString("SporeType", this.getSporeType().name());
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        return this.owner;
    }

    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    public void setFuse(int i) {
        this.entityData.set(DATA_FUSE_ID, i);
    }

    public AMSporeBarrelBlock.SporeType getSporeType() {
        try {
            return AMSporeBarrelBlock.SporeType.valueOf(this.entityData.get(DATA_TYPE_ID));
        } catch (IllegalArgumentException e) {
            return AMSporeBarrelBlock.SporeType.SUPPORT;
        }
    }

    public void setSporeType(AMSporeBarrelBlock.SporeType type) {
        this.entityData.set(DATA_TYPE_ID, type.name());
    }
}
