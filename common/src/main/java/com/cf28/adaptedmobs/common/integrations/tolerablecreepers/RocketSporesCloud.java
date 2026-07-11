package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.CreeperSpores;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RocketSporesCloud extends CreeperSpores {

    @SuppressWarnings("unchecked")
    public RocketSporesCloud(EntityType<?> type, Level level) {
        super((EntityType<? extends CreeperSpores>) type, level);
    }

    public void setCloudSizeDirect(int size) {
        this.setCloudSize(size);
    }

    @Override
    protected SimpleParticleType getSporeParticleType() {
        return AMParticles.ROCKET_SPORES.get();
    }

    @Override
    protected float getVisualCloudSize() {
        return 2.0F;
    }

    @Override
    protected int getCreepieSpawnIntervalTicks() {
        return 10;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Creepie createCreepie(Vec3 pos) {
        RocketCreepieEntity creepie = new RocketCreepieEntity(
                (EntityType<? extends Creepie>) (EntityType<?>) AMEntityTypes.ROCKET_CREEPIE.get(), this.level());
        this.initializeCreepie(creepie);
        creepie.setPos(pos);
        return creepie;
    }
}
