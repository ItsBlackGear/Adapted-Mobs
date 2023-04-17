package com.cf28.adaptedmobs.common.registry;

import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class AMEntityDataSerializers {
    public static final EntityDataSerializer<CreeperState> CREEPER_STATE = EntityDataSerializer.simpleEnum(CreeperState.class);

    public static void register() {
        EntityDataSerializers.registerSerializer(CREEPER_STATE);
    }
}