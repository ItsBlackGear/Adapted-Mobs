package com.cf28.adaptedmobs.common.entity.creeper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

/**
 * FEATURES:
 * - when it spots you, it launches itself up into the air before crashing down on you with an explosion
 * - when there is not enough space to jump, they'll act like regular creepers and explode normally
 * - they have a rare chance of dropping a blue Mystery Egg that hatches a tame baby Rocket Creeper
 * - when it grows up, it will be able to fight alongside you
 * ADDITIONAL CHANGES:
 * - when charged, it has a larger explosion radius / higher damage
 * LOOT:
 * - gunpowder [similar to creepers]
 * - light blue creeper burst firework star [rare]
 * - blue mystery egg [very rare]
 */
public class RocketCreeper extends Creeper {
    public RocketCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }
}