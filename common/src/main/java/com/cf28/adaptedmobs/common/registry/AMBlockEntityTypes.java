package com.cf28.adaptedmobs.common.registry;

import com.blackgear.platform.core.helper.BlockEntityRegistry;
import com.blackgear.platform.core.helper.BlockEntityTypeBuilder;
import com.cf28.adaptedmobs.common.blockentities.AMSkullBlockEntity;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class AMBlockEntityTypes {
    public static final BlockEntityRegistry BLOCK_ENTITIES = BlockEntityRegistry.create(AdaptedMobs.MOD_ID);

    public static final Supplier<BlockEntityType<AMSkullBlockEntity>> SKULL = BLOCK_ENTITIES.register(
        "creeper_skull",
        BlockEntityTypeBuilder.create(
            AMSkullBlockEntity::new,
            AMBlocks.FESTIVE_CREEPER_HEAD, AMBlocks.FESTIVE_CREEPER_WALL_HEAD,
            AMBlocks.SUPPORT_CREEPER_HEAD, AMBlocks.SUPPORT_CREEPER_WALL_HEAD,
            AMBlocks.ROCKET_CREEPER_HEAD, AMBlocks.ROCKET_CREEPER_WALL_HEAD,
            AMBlocks.PEEPER_CREEPER_HEAD, AMBlocks.PEEPER_CREEPER_WALL_HEAD
        )
    );
}