package com.cf28.adaptedmobs.common.registry;

import com.blackgear.platform.core.helper.BlockRegistry;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class AMBlocks {
    public static final BlockRegistry BLOCKS = BlockRegistry.create(AdaptedMobs.MOD_ID);

    public static final Supplier<Block> FESTIVE_TNT = BLOCKS.registerNoItem(
        "festive_tnt",
        BlockBehaviour.Properties.of(Material.WOOL)
    );
}