package com.cf28.adaptedmobs.common.integrations.tolerablecreepers;

import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMParticles;
import com.evandev.tolerable_creepers.common.entity.CreeperSpores;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FestiveSporesCloud extends CreeperSpores {

    @SuppressWarnings("unchecked")
    public FestiveSporesCloud(EntityType<?> type, Level level) {
        super((EntityType<? extends CreeperSpores>) type, level);
    }

    public void setCloudSizeDirect(int size) {
        this.setCloudSize(size);
    }

    @Override
    protected SimpleParticleType getSporeParticleType() {
        return AMParticles.FESTIVE_SPORES.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Creepie createCreepie(Vec3 pos) {
        FestiveCreepieEntity creepie = new FestiveCreepieEntity(
                (EntityType<? extends Creepie>) (EntityType<?>) AMEntityTypes.FESTIVE_CREEPIE.get(), this.level());
        this.initializeCreepie(creepie);
        creepie.setPos(pos);
        return creepie;
    }
}
