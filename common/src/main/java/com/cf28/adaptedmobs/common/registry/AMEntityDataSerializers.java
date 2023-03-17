package com.cf28.adaptedmobs.common.registry;

import com.cf28.adaptedmobs.common.entity.FestiveCreeper;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class AMEntityDataSerializers {
    public static final EntityDataSerializer<FestiveCreeper.State> FESTIVE_CREEPER_STATE = EntityDataSerializer.simpleEnum(FestiveCreeper.State.class);

    public static void register() {
        EntityDataSerializers.registerSerializer(FESTIVE_CREEPER_STATE);
    }
}