package com.cf28.adaptedmobs.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

/**
 * FEATURES:
 * - they throw TNT at you and are very destructive!
 * - there should be an option to disable block damage in the config file
 * - when trying to approach a festive creeper, it will back away
 * - they will set fires underneath them to try and make giving chase to them more hazardous
 * - they are immune to fire and lava
 * - when charged, their TNT is stronger
 * - they have a rare chance of dropping a red Mystery Egg that hatches a tame baby Festive Creeper
 * - baby Festive Creepers are.. quite useless...
 * - but once they grow up, they will fight alongside you!
 * - If they get hurt, you can heal them with gunpowder
 * ADDITIONAL CHANGES:
 * - tries to keep a distance from the player by walking backwards while shooting festive TNT
 * - festive TNT is unobtainable and does not grief blocks, but deals 25 damage at point-blank distance
 * - when charged, the TNT they shoot is regular TNT that destroy blocks, and deal the usual amount of damage
 * LOOT:
 * - gunpowder [similar to creepers]
 * - TNT [rare]
 * - red mystery egg [very rare]
 */
public class FestiveCreeper extends Creeper {
    public FestiveCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }
}