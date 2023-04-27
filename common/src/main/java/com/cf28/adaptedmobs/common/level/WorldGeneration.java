package com.cf28.adaptedmobs.common.level;

import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.core.mixin.access.SpawnPlacementsAccessor;
import com.cf28.adaptedmobs.core.platform.common.BiomeManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;

public class WorldGeneration {
    public static void bootstrap() {
        SpawnPlacementsAccessor.callRegister(AMEntityTypes.FESTIVE_CREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacementsAccessor.callRegister(AMEntityTypes.SUPPORT_CREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacementsAccessor.callRegister(AMEntityTypes.ROCKET_CREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);

        BiomeManager.add((writer, context) -> {
            if (context.hasEntity(EntityType.CREEPER)) {
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.FESTIVE_CREEPER.get(), 100, 4, 4));
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.SUPPORT_CREEPER.get(), 100, 4, 4));
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.ROCKET_CREEPER.get(), 100, 4, 4));
            }
        });
    }
}