package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.helper.BlockEntityRegistry;
import com.blackgear.platform.core.helper.BlockEntityTypeBuilder;
import com.cf28.adaptedmobs.common.level.block_entity.AMSkullBlockEntity;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class AMBlockEntityTypes {
    public static final BlockEntityRegistry REGISTRIES = BlockEntityRegistry.create(AdaptedMobs.MOD_ID);
    
    public static final Supplier<BlockEntityType<AMSkullBlockEntity>> SKULL = REGISTRIES.register("creeper_skull",
        BlockEntityTypeBuilder.create(
            AMSkullBlockEntity::new,
            AMBlocks.FESTIVE_CREEPER_HEAD.getFirst(), AMBlocks.FESTIVE_CREEPER_HEAD.getSecond(),
            AMBlocks.SUPPORT_CREEPER_HEAD.getFirst(), AMBlocks.SUPPORT_CREEPER_HEAD.getSecond(),
            AMBlocks.ROCKET_CREEPER_HEAD.getFirst(), AMBlocks.ROCKET_CREEPER_HEAD.getSecond(),
            AMBlocks.PEEPER_CREEPER_HEAD.getFirst(), AMBlocks.PEEPER_CREEPER_HEAD.getSecond()
        ));
}