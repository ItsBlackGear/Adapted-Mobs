package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.CreeperSpores;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import com.evandev.tolerable_creepers.core.registry.TCParticles;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SupportSporesCloud extends CreeperSpores {
    private final boolean friendly;

    @SuppressWarnings("unchecked")
    public SupportSporesCloud(EntityType<?> type, Level level, boolean friendly) {
        super((EntityType<? extends CreeperSpores>) type, level);
        this.friendly = friendly;
    }

    public void setCloudSizeDirect(int size) {
        this.setCloudSize(size);
    }

    @Override
    protected SimpleParticleType getSporeParticleType() {
        if (this.random.nextFloat() > 0.2F) {
            return TCParticles.CREEPER_SPORES.get();
        }
        return friendly
                ? (this.random.nextBoolean() ? AMParticles.SUPPORTED_BLUE.get() : AMParticles.SUPPORTED_RED.get())
                : (this.random.nextBoolean() ? AMParticles.SUPPORTED_BLUE.get() : AMParticles.SUPPORTED_GREY.get());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Creepie createCreepie(Vec3 pos) {
        SupportCreepieEntity creepie = new SupportCreepieEntity(
                (EntityType<? extends Creepie>) (EntityType<?>) AMEntityTypes.SUPPORT_CREEPIE.get(), this.level());
        this.initializeCreepie(creepie);
        if (friendly) {
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
