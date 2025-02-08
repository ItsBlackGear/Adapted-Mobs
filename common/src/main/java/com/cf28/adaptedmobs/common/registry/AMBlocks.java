package com.cf28.adaptedmobs.common.registry;

import com.blackgear.platform.core.helper.BlockRegistry;
import com.cf28.adaptedmobs.common.block.PlatformSkullBlock;
import com.cf28.adaptedmobs.common.block.PlatformWallSkullBlock;
import com.cf28.adaptedmobs.common.blockentity.Skulls;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
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

    public static final Supplier<Block> FESTIVE_CREEPER_WALL_HEAD = BLOCKS.registerNoItem(
        "festive_creeper_wall_head",
        properties -> new PlatformWallSkullBlock(Skulls.FESTIVE_CREEPER, properties),
        BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F)
    );
    public static final Supplier<Block> FESTIVE_CREEPER_HEAD = BLOCKS.register(
        "festive_creeper_head",
        properties -> new PlatformSkullBlock(Skulls.FESTIVE_CREEPER, properties),
        BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F),
        "festive_creeper_head",
        (block, properties) -> new StandingAndWallBlockItem(block, FESTIVE_CREEPER_WALL_HEAD.get(), properties),
        new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS).rarity(Rarity.UNCOMMON)
    );

    public static final Supplier<Block> SUPPORT_CREEPER_WALL_HEAD = BLOCKS.registerNoItem(
        "support_creeper_wall_head",
        properties -> new PlatformWallSkullBlock(Skulls.SUPPORT_CREEPER, properties),
        BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F)
    );
    public static final Supplier<Block> SUPPORT_CREEPER_HEAD = BLOCKS.register(
        "support_creeper_head",
        properties -> new PlatformSkullBlock(Skulls.SUPPORT_CREEPER, properties),
        BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F),
        "support_creeper_head",
        (block, properties) -> new StandingAndWallBlockItem(block, SUPPORT_CREEPER_WALL_HEAD.get(), properties),
        new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS).rarity(Rarity.UNCOMMON)
    );

    public static final Supplier<Block> PEEPER_CREEPER_WALL_HEAD = BLOCKS.registerNoItem(
        "peeper_creeper_wall_head",
        properties -> new PlatformWallSkullBlock(Skulls.PEEPER_CREEPER, properties),
        BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F)
    );
    public static final Supplier<Block> PEEPER_CREEPER_HEAD = BLOCKS.register(
        "peeper_creeper_head",
        properties -> new PlatformSkullBlock(Skulls.PEEPER_CREEPER, properties),
        BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F),
        "peeper_creeper_head",
        (block, properties) -> new StandingAndWallBlockItem(block, PEEPER_CREEPER_WALL_HEAD.get(), properties),
        new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS).rarity(Rarity.UNCOMMON)
    );

    public static final Supplier<Block> ROCKET_CREEPER_WALL_HEAD = BLOCKS.registerNoItem(
        "rocket_creeper_wall_head",
        properties -> new PlatformWallSkullBlock(Skulls.ROCKET_CREEPER, properties),
        BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F)
    );
    public static final Supplier<Block> ROCKET_CREEPER_HEAD = BLOCKS.register(
        "rocket_creeper_head",
        properties -> new PlatformSkullBlock(Skulls.ROCKET_CREEPER, properties),
        BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F),
        "rocket_creeper_head",
        (block, properties) -> new StandingAndWallBlockItem(block, ROCKET_CREEPER_WALL_HEAD.get(), properties),
        new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS).rarity(Rarity.UNCOMMON)
    );
}