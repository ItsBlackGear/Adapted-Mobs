package com.cf28.adaptedmobs.common.entity.creeper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

/**
 * FEATURES:
 * - they run away from players and look for other hostile mobs to follow
 * - once following a mob, they buff themselves and the mob with potions such as speed and fire resistance
 * - when they follow creepers, they will turn them into charged creepers
 * - if it happens to be charged, the potion effects that it provides will be more powerful
 * - they have a rare chance of dropping a yellow Mystery Egg that hatches a tame baby Support Creeper
 * - the baby support creeper will start helping you right away and give you several helpful potion effects
 * - [additional note] apparently, owners aren't able to hit tamed creepers...
 * ADDITIONAL CHANGES:
 * - runs quickly like an ocelot away from players
 * - looks for hostile mobs to follow and buff with strength I and speed I
 * - when below 50% health and there is no mob nearby to buff, it will explode like a normal creeper if you get too close
 * - when charged, the buffs it gives are tier II and its explosion when at low health is larger and deals more damage
 * LOOT:
 * - gunpowder [similar to creepers]
 * - yellow mystery egg [very rare]
 */
public class SupportCreeper extends TamableCreeper {
    public SupportCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }
}