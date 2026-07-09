package com.cf28.adaptedmobs.common;

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

    public final ConfigBuilder.ConfigValue<Integer> festiveSporeCountDayBase;
    public final ConfigBuilder.ConfigValue<Integer> festiveSporeCountDayRandom;
    public final ConfigBuilder.ConfigValue<Integer> festiveSporeCountNightBase;
    public final ConfigBuilder.ConfigValue<Integer> festiveSporeCountNightRandom;

    public final ConfigBuilder.ConfigValue<Integer> rocketSporeCountDayBase;
    public final ConfigBuilder.ConfigValue<Integer> rocketSporeCountDayRandom;
    public final ConfigBuilder.ConfigValue<Integer> rocketSporeCountNightBase;
    public final ConfigBuilder.ConfigValue<Integer> rocketSporeCountNightRandom;

    public final ConfigBuilder.ConfigValue<Integer> supportSporeCountDayBase;
    public final ConfigBuilder.ConfigValue<Integer> supportSporeCountDayRandom;
    public final ConfigBuilder.ConfigValue<Integer> supportSporeCountNightBase;
    public final ConfigBuilder.ConfigValue<Integer> supportSporeCountNightRandom;

    public CommonConfig(ConfigBuilder builder) {
        builder.push("Festive Creeper");
        this.spawnFestiveCreepers = builder.comment("Determines if Festive Creepers should spawn").define("Spawn Festive Creepers", true);
        this.festiveCreeperSpawnWeight = builder.comment("Determines how often do Festive Creepers spawn").defineInRange("Festive Creeper Spawn Weight", 18, 0, 100);
        this.festiveCreeperExtraSpawnWeight = builder.comment("Determines how often do Festive Creepers spawn in extra biomes").defineInRange("Festive Creeper Extra Spawn Weight", 40, 0, 100);
        builder.pop();
        
        builder.push("Support Creeper");
        this.spawnSupportCreepers = builder.comment("Determines if Support Creepers should spawn").define("Spawn Support Creepers", true);
        this.supportCreeperSpawnWeight = builder.comment("Determines how often do Support Creepers spawn").defineInRange("Support Creeper Spawn Weight", 20, 0, 100);
        this.supportCreeperExtraSpawnWeight = builder.comment("Determines how often do Support Creepers spawn in extra biomes").defineInRange("Support Creeper Extra Spawn Weight", 50, 0, 100);
        builder.pop();
        
        builder.push("Rocket Creeper");
        this.spawnRocketCreepers = builder.comment("Determines if Rocket Creepers should spawn").define("Spawn Rocket Creepers", true);
        this.rocketCreeperSpawnWeight = builder.comment("Determines how often do Rocket Creepers spawn").defineInRange("Rocket Creeper Spawn Weight", 25, 0, 100);
        this.rocketCreeperExtraSpawnWeight = builder.comment("Determines how often do Rocket Creepers spawn in extra biomes").defineInRange("Rocket Creeper Extra Spawn Weight", 50, 0, 100);
        builder.pop();

        builder.push("Festive Creeper Spores");
        this.festiveSporeCountDayBase = builder.comment("Base number of Festive Creepies a Festive Spore Barrel/TNT tries to spawn during the day").defineInRange("Festive Spore Count Day Base", 1, 0, 100);
        this.festiveSporeCountDayRandom = builder.comment("Upper bound (exclusive) of the random bonus added to the day base count").defineInRange("Festive Spore Count Day Random", 2, 0, 100);
        this.festiveSporeCountNightBase = builder.comment("Base number of Festive Creepies a Festive Spore Barrel/TNT tries to spawn at night").defineInRange("Festive Spore Count Night Base", 3, 0, 100);
        this.festiveSporeCountNightRandom = builder.comment("Upper bound (exclusive) of the random bonus added to the night base count").defineInRange("Festive Spore Count Night Random", 4, 0, 100);
        builder.pop();

        builder.push("Rocket Creeper Spores");
        this.rocketSporeCountDayBase = builder.comment("Base number of Rocket Creepies a Rocket Creeper's spore cloud tries to spawn during the day").defineInRange("Rocket Spore Count Day Base", 1, 0, 100);
        this.rocketSporeCountDayRandom = builder.comment("Upper bound (exclusive) of the random bonus added to the day base count").defineInRange("Rocket Spore Count Day Random", 2, 0, 100);
        this.rocketSporeCountNightBase = builder.comment("Base number of Rocket Creepies a Rocket Creeper's spore cloud tries to spawn at night").defineInRange("Rocket Spore Count Night Base", 3, 0, 100);
        this.rocketSporeCountNightRandom = builder.comment("Upper bound (exclusive) of the random bonus added to the night base count").defineInRange("Rocket Spore Count Night Random", 4, 0, 100);
        builder.pop();

        builder.push("Support Creeper Spores");
        this.supportSporeCountDayBase = builder.comment("Base number of Support Creepies a Support Creeper's spore cloud tries to spawn during the day").defineInRange("Support Spore Count Day Base", 1, 0, 100);
        this.supportSporeCountDayRandom = builder.comment("Upper bound (exclusive) of the random bonus added to the day base count").defineInRange("Support Spore Count Day Random", 2, 0, 100);
        this.supportSporeCountNightBase = builder.comment("Base number of Support Creepies a Support Creeper's spore cloud tries to spawn at night").defineInRange("Support Spore Count Night Base", 3, 0, 100);
        this.supportSporeCountNightRandom = builder.comment("Upper bound (exclusive) of the random bonus added to the night base count").defineInRange("Support Spore Count Night Random", 4, 0, 100);
        builder.pop();
    }
}