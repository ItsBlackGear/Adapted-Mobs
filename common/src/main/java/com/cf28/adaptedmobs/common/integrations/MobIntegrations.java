package com.cf28.adaptedmobs.common.integrations;

import com.cf28.adaptedmobs.common.level.entity.mob.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.RocketCreeper;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.SupportCreeper;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.TamableCreeper;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

import static com.blackgear.platform.common.integration.MobIntegration.*;

public class MobIntegrations {
    public static void setupMobAttributes(Event event) {
        event.registerAttributes(AMEntityTypes.FESTIVE_CREEPER, FestiveCreeper::createAttributes);
        event.registerAttributes(AMEntityTypes.SUPPORT_CREEPER, SupportCreeper::createAttributes);
        event.registerAttributes(AMEntityTypes.ROCKET_CREEPER, RocketCreeper::createAttributes);
        event.registerAttributes(AMEntityTypes.CREEPER, TamableCreeper::createAttributes);
    }
    
    public static void setupSpawnPlacements(Event event) {
        event.registerPlacement(AMEntityTypes.FESTIVE_CREEPER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(AMEntityTypes.SUPPORT_CREEPER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
        event.registerPlacement(AMEntityTypes.ROCKET_CREEPER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
    }
}