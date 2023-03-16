package com.cf28.adaptedmobs.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

/**
 * FEATURES
 * - they have a specific zone [x and z, y doesn't matter] that will try to get closer or away from the player
 * - if the player gets closer, it will back up, if the player gets away it will get closer
 * - it's not very fst, so by sprint jumping you can catch up to it before it reaches its desired position
 * - once it reaches its position, it will begin to swell, which takes half a second longer to do than a vanilla creeper
 * - then, it will pint the tip of its head to go in the most optimal straight line towards the player
 * - it will shoot in a straight line, and it cannot deviate from this path
 * - if it hits a mob or block in its way, or if it flies straight for 15 seconds, it will explode, causing red and white creeper face fireworks to go off
 * - it will try to ensure that it wont hit a block when shooting before swelling, but the player can build to stop it
 * - this deals as much as tnt with damage, but doesn't grief blocks
 * - it only spawns on certain widely celebrated holidays, and it can be configured to spawn at any certain date / all year
 * - during this time, their spawn rate is extremely high, so you will be siege-d by many of them during the night of these holidays
 * - when charged they can break blocks by exploding
 * LOOT:
 * - gunpowder [similar to creepers]
 * - red/white creeper charge fireworks [rare]
 * - a rare egg from them [very rare]
 */
public class FireworkCreeper extends Creeper {
    public FireworkCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }
}