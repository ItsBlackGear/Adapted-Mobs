package com.cf28.adaptedmobs.common.integrations;

import com.cf28.adaptedmobs.common.integrations.tolerablecreepers.*;
import com.cf28.adaptedmobs.common.level.block.AMSporeBarrelBlock;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.evandev.tolerable_creepers.common.entity.CreeperSpores;
import com.evandev.tolerable_creepers.common.entity.Creepie;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class TolerableCreepersIntegration {

    public static int calculateSporeCount(Level level, BlockPos pos, RandomSource random,
                                          int dayBase, int dayRandom, int nightBase, int nightRandom) {
        boolean day = level.getBrightness(LightLayer.SKY, pos) > 10 && level.isDay();
        int baseCount = day ? dayBase : nightBase;
        int randomBound = day ? dayRandom : nightRandom;
        int randomAdd = randomBound > 0 ? random.nextInt(randomBound) : 0;
        return baseCount + randomAdd;
    }

    public static Item createSupportSporesItem(Item.Properties properties) {
        return new SupportSporesItem(properties);
    }

    public static Item createRocketSporesItem(Item.Properties properties) {
        return new RocketSporesItem(properties);
    }

    public static Item createFestiveSporesItem(Item.Properties properties) {
        return new FestiveSporesItem(properties);
    }

    @SuppressWarnings("unchecked")
    public static Entity createSupportCreepie(EntityType<?> type, Level level) {
        return new SupportCreepieEntity((EntityType<? extends Creepie>) type, level);
    }

    @SuppressWarnings("unchecked")
    public static Entity createRocketCreepie(EntityType<?> type, Level level) {
        return new RocketCreepieEntity((EntityType<? extends Creepie>) type, level);
    }

    @SuppressWarnings("unchecked")
    public static Entity createFestiveCreepie(EntityType<?> type, Level level) {
        return new FestiveCreepieEntity((EntityType<? extends Creepie>) type, level);
    }

    @SuppressWarnings("unchecked")
    public static Entity createFestiveCreepie(Level level, double x, double y, double z) {
        FestiveCreepieEntity creepie = new FestiveCreepieEntity(
                (EntityType<? extends Creepie>) (EntityType<?>) AMEntityTypes.FESTIVE_CREEPIE.get(), level);
        creepie.setPos(x, y, z);
        return creepie;
    }

    public static Entity createSupportSpores(EntityType<?> type, Level level) {
        return new SupportSporesCloud(type, level, false);
    }

    public static Entity createRocketSpores(EntityType<?> type, Level level) {
        return new RocketSporesCloud(type, level);
    }

    public static Entity createFestiveSpores(EntityType<?> type, Level level) {
        return new FestiveSporesCloud(type, level);
    }

    public static Entity createSupportSpores(Level level, double x, double y, double z, int cloudSize, boolean friendly) {
        SupportSporesCloud spores = new SupportSporesCloud(AMEntityTypes.SUPPORT_SPORES.get(), level, friendly);
        spores.setPos(x, y, z);
        spores.setCloudSizeDirect(cloudSize);
        return spores;
    }

    public static Entity createRocketSpores(Level level, double x, double y, double z, int cloudSize, boolean friendly) {
        RocketSporesCloud spores = new RocketSporesCloud(AMEntityTypes.ROCKET_SPORES.get(), level);
        spores.setPos(x, y, z);
        spores.setCloudSizeDirect(cloudSize);
        return spores;
    }

    public static Entity createFestiveSpores(Level level, double x, double y, double z, int cloudSize) {
        FestiveSporesCloud spores = new FestiveSporesCloud(AMEntityTypes.FESTIVE_SPORES.get(), level);
        spores.setPos(x, y, z);
        spores.setCloudSizeDirect(cloudSize);
        return spores;
    }

    public static Entity createSupportSporesFromCreeper(Level level, Creeper parent) {
        SupportSporesCloud spores = new SupportSporesCloud(AMEntityTypes.SUPPORT_SPORES.get(), level, false);
        spores.setPos(parent.getX(), parent.getY(), parent.getZ());
        spores.setOwner(parent);
        int count = calculateSporeCount(level, parent.blockPosition(), parent.getRandom(),
                AdaptedMobs.CONFIG.supportSporeCountDayBase.get(), AdaptedMobs.CONFIG.supportSporeCountDayRandom.get(),
                AdaptedMobs.CONFIG.supportSporeCountNightBase.get(), AdaptedMobs.CONFIG.supportSporeCountNightRandom.get());
        spores.setCloudSizeDirect(Math.round(count * parent.getHealth() / parent.getMaxHealth()));
        return spores;
    }

    public static Entity createRocketSporesFromCreeper(Level level, Creeper parent) {
        RocketSporesCloud spores = new RocketSporesCloud(AMEntityTypes.ROCKET_SPORES.get(), level);
        spores.setPos(parent.getX(), parent.getY(), parent.getZ());
        spores.setOwner(parent);
        int count = calculateSporeCount(level, parent.blockPosition(), parent.getRandom(),
                AdaptedMobs.CONFIG.rocketSporeCountDayBase.get(), AdaptedMobs.CONFIG.rocketSporeCountDayRandom.get(),
                AdaptedMobs.CONFIG.rocketSporeCountNightBase.get(), AdaptedMobs.CONFIG.rocketSporeCountNightRandom.get());
        spores.setCloudSizeDirect(Math.round(count * parent.getHealth() / parent.getMaxHealth()));
        return spores;
    }

    public static Entity createSporeItemProjectile(AMSporeBarrelBlock.SporeType type, Player player, Level level) {
        RandomSource random = level.getRandom();
        int cloudSize = random.nextFloat() > 0.25F ? 2 : random.nextFloat() > 0.01F ? 1 : 0;
        if (type == AMSporeBarrelBlock.SporeType.SUPPORT) {
            SupportSporesCloud spores = new SupportSporesCloud(AMEntityTypes.SUPPORT_SPORES.get(), level, true);
            spores.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            spores.setOwner(player);
            spores.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            spores.setCloudSizeDirect(cloudSize);
            return spores;
        } else {
            RocketSporesCloud spores = new RocketSporesCloud(AMEntityTypes.ROCKET_SPORES.get(), level);
            spores.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            spores.setOwner(player);
            spores.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            spores.setCloudSizeDirect(cloudSize);
            return spores;
        }
    }

    public static void setSporesOwner(Entity spores, LivingEntity owner) {
        if (spores instanceof CreeperSpores cs) {
            cs.setOwner(owner);
        }
    }
}
