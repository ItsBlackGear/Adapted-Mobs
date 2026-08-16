package com.cf28.adaptedmobs.common.integrations;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.platform.common.worldgen.modifier.FeatureManager;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.tags.AMBiomeTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class BiomeIntegrations extends FeatureManager {
    public static void create(BiomeWriter writer, BiomeContext context) {
        new BiomeIntegrations(context, writer).bootstrap();
    }
    
    private BiomeIntegrations(BiomeContext context, BiomeWriter writer) {
        super(context, writer);
    }
    
    @Override
    public void bootstrap() {
        this.addIf(context -> context.hasEntity(() -> EntityType.CREEPER), (context, writer) -> {
            if (AdaptedMobs.CONFIG.spawnFestiveCreepers.get()) {
                int weight = context.is(AMBiomeTags.EXTRA_FESTIVE_CREEPER_SPAWNS)
                    ? AdaptedMobs.CONFIG.festiveCreeperExtraSpawnWeight.get()
                    : AdaptedMobs.CONFIG.festiveCreeperSpawnWeight.get();
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.FESTIVE_CREEPER.get(), weight, 1, 1));
            }
            
            if (AdaptedMobs.CONFIG.spawnSupportCreepers.get()) {
                int weight = context.is(AMBiomeTags.EXTRA_SUPPORT_CREEPER_SPAWNS)
                    ? AdaptedMobs.CONFIG.supportCreeperExtraSpawnWeight.get()
                    : AdaptedMobs.CONFIG.supportCreeperSpawnWeight.get();
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.SUPPORT_CREEPER.get(), weight, 1, 2));
            }
            
            if (AdaptedMobs.CONFIG.spawnRocketCreepers.get()) {
                int weight = context.is(AMBiomeTags.EXTRA_ROCKET_CREEPER_SPAWNS)
                    ? AdaptedMobs.CONFIG.rocketCreeperExtraSpawnWeight.get()
                    : AdaptedMobs.CONFIG.rocketCreeperSpawnWeight.get();
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.ROCKET_CREEPER.get(), weight, 1, 3));
            }
        });
        
        this.addIf(context -> context.is(BiomeTags.IS_MOUNTAIN), (context, writer) -> {
            writer.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(AMEntityTypes.HARPY.get(), 15, 1, 2));
        });

        this.addIf(context -> context.hasEntity(() -> EntityType.ZOMBIE), (context, writer) -> {
            if (AdaptedMobs.CONFIG.spawnEntombed.get()) {
                writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(AMEntityTypes.ENTOMBED.get(), AdaptedMobs.CONFIG.entombedSpawnWeight.get(), 1, 2));
            }
        });

    }
}