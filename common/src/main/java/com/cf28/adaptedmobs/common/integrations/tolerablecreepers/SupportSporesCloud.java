package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.CreeperSpores;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import com.evandev.tolerable_creepers.core.registry.TCParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SupportSporesCloud extends CreeperSpores {
    private static final EntityDataAccessor<Boolean> FRIENDLY =
            SynchedEntityData.defineId(SupportSporesCloud.class, EntityDataSerializers.BOOLEAN);

    @SuppressWarnings("unchecked")
    public SupportSporesCloud(EntityType<?> type, Level level, boolean friendly) {
        super((EntityType<? extends CreeperSpores>) type, level);
        this.entityData.set(FRIENDLY, friendly);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FRIENDLY, false);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Friendly", this.isFriendly());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(FRIENDLY, nbt.getBoolean("Friendly"));
    }

    public boolean isFriendly() {
        return this.entityData.get(FRIENDLY);
    }

    public void setCloudSizeDirect(int size) {
        this.setCloudSize(size);
    }

    @Override
    protected SimpleParticleType getSporeParticleType() {
        if (this.random.nextFloat() > 0.2F) {
            return TCParticles.CREEPER_SPORES.get();
        }
        return this.isFriendly()
                ? (this.random.nextBoolean() ? AMParticles.SUPPORTED_YELLOW.get() : AMParticles.SUPPORTED_RED.get())
                : (this.random.nextBoolean() ? AMParticles.SUPPORTED_BLUE.get() : AMParticles.SUPPORTED_GREY.get());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Creepie createCreepie(Vec3 pos) {
        SupportCreepieEntity creepie = new SupportCreepieEntity(
                (EntityType<? extends Creepie>) (EntityType<?>) AMEntityTypes.SUPPORT_CREEPIE.get(), this.level());
        this.initializeCreepie(creepie);
        if (this.isFriendly()) {
            creepie.setOwner(this.getOwner());
            creepie.setVariant(this.random.nextBoolean()
                    ? SupportCreepieEntity.Variant.SPEED : SupportCreepieEntity.Variant.STRENGTH);
        } else {
            creepie.setVariant(this.random.nextBoolean()
                    ? SupportCreepieEntity.Variant.SLOWNESS : SupportCreepieEntity.Variant.WEAKNESS);
        }
        creepie.setPos(pos);
        return creepie;
    }
}
