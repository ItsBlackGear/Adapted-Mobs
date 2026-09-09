package com.cf28.adaptedmobs.common.level.entity.mob;

import com.cf28.adaptedmobs.common.registries.AMParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Mindmould extends Slime {
    public Mindmould(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    protected @NotNull ParticleOptions getParticleType() { return AMParticles.BRAIN_GOO.get(); }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData
    ) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
//        this.setSize(1, false);
        return data;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.28D)
            .add(Attributes.ATTACK_DAMAGE, 5.0D)
            .add(Attributes.ARMOR, 2.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }
}
