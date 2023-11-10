package com.cf28.adaptedmobs.core.config.forge;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = AdaptedMobs.MOD_ID)
public class ConfigEntriesImpl {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ConfigEntriesImpl.Common COMMON;

    public static int getFestiveCreeperSpawnWeight() {
        return COMMON.festiveCreeperSpawnWeight.get();
    }

    public static int getFestiveCreeperExtraSpawnWeight() {
        return COMMON.festiveCreeperExtraSpawnWeight.get();
    }

    public static int getSupportCreeperSpawnWeight() {
        return COMMON.supportCreeperSpawnWeight.get();
    }

    public static int getSupportCreeperExtraSpawnWeight() {
        return COMMON.supportCreeperExtraSpawnWeight.get();
    }

    public static int getRocketCreeperSpawnWeight() {
        return COMMON.rocketCreeperSpawnWeight.get();
    }

    public static int getRocketCreeperExtraSpawnWeight() {
        return COMMON.rocketCreeperExtraSpawnWeight.get();
    }

    public static boolean spawnFestiveCreepers() {
        return COMMON.spawnFestiveCreeper.get();
    }

    public static boolean spawnSupportCreepers() {
        return COMMON.spawnSupportCreeper.get();
    }

    public static boolean spawnRocketCreepers() {
        return COMMON.spawnRocketCreeper.get();
    }

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Integer> festiveCreeperSpawnWeight;
        public final ForgeConfigSpec.ConfigValue<Integer> festiveCreeperExtraSpawnWeight;
        public final ForgeConfigSpec.ConfigValue<Integer> supportCreeperSpawnWeight;
        public final ForgeConfigSpec.ConfigValue<Integer> supportCreeperExtraSpawnWeight;
        public final ForgeConfigSpec.ConfigValue<Integer> rocketCreeperSpawnWeight;
        public final ForgeConfigSpec.ConfigValue<Integer> rocketCreeperExtraSpawnWeight;
        public final ForgeConfigSpec.ConfigValue<Boolean> spawnFestiveCreeper;
        public final ForgeConfigSpec.ConfigValue<Boolean> spawnSupportCreeper;
        public final ForgeConfigSpec.ConfigValue<Boolean> spawnRocketCreeper;

        protected Common(ForgeConfigSpec.Builder builder) {
            builder.push("spawns");
            spawnFestiveCreeper = builder.comment("Determines if Festive Creepers should spawn").define("Spawn Festive Creepers", true);
            festiveCreeperSpawnWeight = builder.comment("Determines how often do Festive Creepers spawn").defineInRange("Festive Creeper Spawn Weight", 25, 0, 100);
            festiveCreeperExtraSpawnWeight = builder.comment("Determines how often do Festive Creepers spawn in extra biomes").defineInRange("Festive Creeper Extra Spawn Weight", 50, 0, 100);
            spawnSupportCreeper = builder.comment("Determines if Support Creepers should spawn").define("Spawn Support Creepers", true);
            supportCreeperSpawnWeight = builder.comment("Determines how often do Support Creepers spawn").defineInRange("Support Creeper Spawn Weight", 25, 0, 100);
            supportCreeperExtraSpawnWeight = builder.comment("Determines how often do Support Creepers spawn in extra biomes").defineInRange("Support Creeper Extra Spawn Weight", 50, 0, 100);
            spawnRocketCreeper = builder.comment("Determines if Rocket Creepers should spawn").define("Spawn Rocket Creepers", true);
            rocketCreeperSpawnWeight = builder.comment("Determines how often do Rocket Creepers spawn").defineInRange("Rocket Creeper Spawn Weight", 25, 0, 100);
            rocketCreeperExtraSpawnWeight = builder.comment("Determines how often do Rocket Creepers spawn in extra biomes").defineInRange("Rocket Creeper Extra Spawn Weight", 50, 0, 100);
            builder.pop();
        }
    }

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}