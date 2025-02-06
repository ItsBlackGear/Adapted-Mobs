package com.cf28.adaptedmobs.core;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class CommonConfig {
    public final ConfigBuilder.ConfigValue<Boolean> spawnFestiveCreepers;
    public final ConfigBuilder.ConfigValue<Integer> festiveCreeperSpawnWeight;
    public final ConfigBuilder.ConfigValue<Integer> festiveCreeperExtraSpawnWeight;

    public final ConfigBuilder.ConfigValue<Boolean> spawnSupportCreepers;
    public final ConfigBuilder.ConfigValue<Integer> supportCreeperSpawnWeight;
    public final ConfigBuilder.ConfigValue<Integer> supportCreeperExtraSpawnWeight;

    public final ConfigBuilder.ConfigValue<Boolean> spawnRocketCreepers;
    public final ConfigBuilder.ConfigValue<Integer> rocketCreeperSpawnWeight;
    public final ConfigBuilder.ConfigValue<Integer> rocketCreeperExtraSpawnWeight;

    public CommonConfig(ConfigBuilder builder) {
        builder.push("mobs");
            builder.push("Festive Creeper");
            this.spawnFestiveCreepers = builder.comment("Determines if Festive Creepers should spawn").define("Spawn Festive Creepers", true);
            this.festiveCreeperSpawnWeight = builder.comment("Determines how often do Festive Creepers spawn").defineInRange("Festive Creeper Spawn Weight", 25, 0, 100);
            this.festiveCreeperExtraSpawnWeight = builder.comment("Determines how often do Festive Creepers spawn in extra biomes").defineInRange("Festive Creeper Extra Spawn Weight", 50, 0, 100);
            builder.pop();
            builder.push("Support Creeper");
            this.spawnSupportCreepers = builder.comment("Determines if Support Creepers should spawn").define("Spawn Support Creepers", true);
            this.supportCreeperSpawnWeight = builder.comment("Determines how often do Support Creepers spawn").defineInRange("Support Creeper Spawn Weight", 25, 0, 100);
            this.supportCreeperExtraSpawnWeight = builder.comment("Determines how often do Support Creepers spawn in extra biomes").defineInRange("Support Creeper Extra Spawn Weight", 50, 0, 100);
            builder.pop();
            builder.push("Rocket Creeper");
            this.spawnRocketCreepers = builder.comment("Determines if Rocket Creepers should spawn").define("Spawn Rocket Creepers", true);
            this.rocketCreeperSpawnWeight = builder.comment("Determines how often do Rocket Creepers spawn").defineInRange("Rocket Creeper Spawn Weight", 25, 0, 100);
            this.rocketCreeperExtraSpawnWeight = builder.comment("Determines how often do Rocket Creepers spawn in extra biomes").defineInRange("Rocket Creeper Extra Spawn Weight", 50, 0, 100);
            builder.pop();
        builder.pop();
    }
}