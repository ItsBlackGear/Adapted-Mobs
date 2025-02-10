package com.cf28.adaptedmobs.common.level;

import com.blackgear.platform.common.entity.SpawnPlacement;
import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeManager;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.cf28.adaptedmobs.common.registry.AMEntityTypes;
import com.cf28.adaptedmobs.core.tags.AMBiomeTags;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;

public class WorldGeneration {
    public static void bootstrap() {
        SpawnPlacement.register(AMEntityTypes.FESTIVE_CREEPER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacement.register(AMEntityTypes.SUPPORT_CREEPER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        SpawnPlacement.register(AMEntityTypes.ROCKET_CREEPER, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);

        BiomeManager.add(WorldGeneration::populateCreeperVariants);
    }

    private static void populateCreeperVariants(BiomeWriter writer, BiomeContext context) {
        if (context.hasEntity(() -> EntityType.CREEPER)) {
            if (AdaptedMobs.CONFIG.spawnFestiveCreepers.get()) {
                int weight = context.is(AMBiomeTags.SPAWN_EXTRA_FESTIVE_CREEPER) ? AdaptedMobs.CONFIG.festiveCreeperExtraSpawnWeight.get() : AdaptedMobs.CONFIG.festiveCreeperSpawnWeight.get();
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.FESTIVE_CREEPER.get(), weight, 1, 1));
            }

            if (AdaptedMobs.CONFIG.spawnSupportCreepers.get()) {
                int weight = context.is(AMBiomeTags.SPAWN_EXTRA_SUPPORT_CREEPER) ? AdaptedMobs.CONFIG.supportCreeperExtraSpawnWeight.get() : AdaptedMobs.CONFIG.supportCreeperSpawnWeight.get();
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.SUPPORT_CREEPER.get(), weight, 1, 2));
            }

            if (AdaptedMobs.CONFIG.spawnRocketCreepers.get()) {
                int weight = context.is(AMBiomeTags.SPAWN_EXTRA_ROCKET_CREEPER) ? AdaptedMobs.CONFIG.rocketCreeperExtraSpawnWeight.get() : AdaptedMobs.CONFIG.rocketCreeperSpawnWeight.get();
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.ROCKET_CREEPER.get(), weight, 1, 3));
            }
        }
    }
}