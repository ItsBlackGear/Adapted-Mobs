package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.helper.DataSerializerRegistry;
import com.cf28.adaptedmobs.common.level.entity.mob.creeper.CreeperState;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.function.Supplier;

public class AMEntityDataSerializers {
    public static final DataSerializerRegistry REGISTRIES = DataSerializerRegistry.create(AdaptedMobs.MOD_ID);
    
    public static final Supplier<EntityDataSerializer<CreeperState>> CREEPER_STATE = REGISTRIES.register("creeper_state", CreeperState.STREAM_CODEC);
}