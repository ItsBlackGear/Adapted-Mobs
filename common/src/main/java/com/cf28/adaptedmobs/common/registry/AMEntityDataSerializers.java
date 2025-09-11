package com.cf28.adaptedmobs.common.registry;

import com.blackgear.platform.core.helper.DataSerializerRegistry;
import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import net.minecraft.network.syncher.EntityDataSerializer;

public class AMEntityDataSerializers {
    public static final DataSerializerRegistry DATA_SERIALIZERS = DataSerializerRegistry.create();

    public static final EntityDataSerializer<CreeperState> CREEPER_STATE = DATA_SERIALIZERS.register(EntityDataSerializer.simpleEnum(CreeperState.class));
}