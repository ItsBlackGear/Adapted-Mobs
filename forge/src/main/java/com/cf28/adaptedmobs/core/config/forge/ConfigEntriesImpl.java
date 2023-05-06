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

    public static int getSupportCreeperSpawnWeight() {
        return COMMON.supportCreeperSpawnWeight.get();
    }

    public static int getRocketCreeperSpawnWeight() {
        return COMMON.rocketCreeperSpawnWeight.get();
    }

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Integer> festiveCreeperSpawnWeight;
        public final ForgeConfigSpec.ConfigValue<Integer> supportCreeperSpawnWeight;
        public final ForgeConfigSpec.ConfigValue<Integer> rocketCreeperSpawnWeight;

        protected Common(ForgeConfigSpec.Builder builder) {
            builder.push("spawns");
            festiveCreeperSpawnWeight = builder.comment("Determines how often do Festive Creepers spawn").defineInRange("Festive Creeper Spawn Weight", 100, 0, 100);
            supportCreeperSpawnWeight = builder.comment("Determines how often do Support Creepers spawn").defineInRange("Support Creeper Spawn Weight", 100, 0, 100);
            rocketCreeperSpawnWeight = builder.comment("Determines how often do Rocket Creepers spawn").defineInRange("Rocket Creeper Spawn Weight", 100, 0, 100);
            builder.pop();
        }
    }

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
}