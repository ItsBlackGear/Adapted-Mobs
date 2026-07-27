package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.helper.BlockRegistry;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.level.block.*;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import com.cf28.adaptedmobs.common.level.block.HarpyEggBlock;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class AMBlocks {
    public static final BlockRegistry REGISTRIES = BlockRegistry.create(AdaptedMobs.MOD_ID);

    public static final Supplier<Block> FESTIVE_TNT = REGISTRIES.registerNoItem("festive_tnt",
            FestiveTntBlock::new,
            Properties.ofFullCopy(Blocks.TNT)
                    .noOcclusion()
                    .noLootTable());

    public static final Supplier<Block> HARPY_EGG = REGISTRIES.registerNoItem("harpy_egg",
            HarpyEggBlock::new,
            Properties.ofFullCopy(Blocks.SNIFFER_EGG));

    public static final Pair<Supplier<Block>, Supplier<Block>> FESTIVE_CREEPER_HEAD = registerSkull("festive_creeper", SkullTypes.FESTIVE_CREEPER,
            FestiveCreeperSkullBlock::new,
            WallFestiveCreeperSkullBlock::new);
    public static final Pair<Supplier<Block>, Supplier<Block>> SUPPORT_CREEPER_HEAD = registerSkull("support_creeper", SkullTypes.SUPPORT_CREEPER,
            SupportCreeperSkullBlock::new,
            WallSupportCreeperSkullBlock::new);
    public static final Pair<Supplier<Block>, Supplier<Block>> ROCKET_CREEPER_HEAD = registerSkull("rocket_creeper", SkullTypes.ROCKET_CREEPER,
            RocketCreeperSkullBlock::new,
            WallRocketCreeperSkullBlock::new);
    public static final Pair<Supplier<Block>, Supplier<Block>> PEEPER_CREEPER_HEAD = registerSkull("peeper_creeper", SkullTypes.PEEPER_CREEPER,
            SupportCreeperSkullBlock::new,
            WallSupportCreeperSkullBlock::new);

    public static final Supplier<Block> FESTIVE_SPORE_BARREL = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.registerNoItem("festive_spore_barrel",
                    properties -> new AMSporeBarrelBlock(AMSporeBarrelBlock.SporeType.FESTIVE, properties),
                    Properties.ofFullCopy(Blocks.BARREL).noOcclusion())
            : null;
    public static final Supplier<Block> ROCKET_SPORE_BARREL = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.registerNoItem("rocket_spore_barrel",
                    properties -> new AMSporeBarrelBlock(AMSporeBarrelBlock.SporeType.ROCKET, properties),
                    Properties.ofFullCopy(Blocks.BARREL).noOcclusion())
            : null;
    public static final Supplier<Block> SUPPORT_SPORE_BARREL = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.registerNoItem("support_spore_barrel",
                    properties -> new AMSporeBarrelBlock(AMSporeBarrelBlock.SporeType.SUPPORT, properties),
                    Properties.ofFullCopy(Blocks.BARREL).noOcclusion())
            : null;

    public static final Supplier<Block> POTTED_FESTIVE_CREEPER_SPORES_PLANT = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.registerNoItem("potted_festive_creeper_spores_plant",
                    properties -> new AMPottedSporeBlock(AMItems.FESTIVE_CREEPER_SPORES, properties),
                    Properties.of().instabreak().noOcclusion())
            : null;
    public static final Supplier<Block> POTTED_ROCKET_CREEPER_SPORES_PLANT = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.registerNoItem("potted_rocket_creeper_spores_plant",
                    properties -> new AMPottedSporeBlock(AMItems.ROCKET_CREEPER_SPORES, properties),
                    Properties.of().instabreak().noOcclusion())
            : null;
    public static final Supplier<Block> POTTED_SUPPORT_CREEPER_SPORES_PLANT = TolerableCreepersCompat.isLoaded()
            ? REGISTRIES.registerNoItem("potted_support_creeper_spores_plant",
                    properties -> new AMPottedSporeBlock(AMItems.SUPPORT_CREEPER_SPORES, properties),
                    Properties.of().instabreak().noOcclusion())
            : null;

    static {
        if (TolerableCreepersCompat.isLoaded()) {
            REGISTRIES.registerItem("festive_spore_barrel", () -> new BlockItem(FESTIVE_SPORE_BARREL.get(), new Item.Properties()));
            REGISTRIES.registerItem("rocket_spore_barrel", () -> new BlockItem(ROCKET_SPORE_BARREL.get(), new Item.Properties()));
            REGISTRIES.registerItem("support_spore_barrel", () -> new BlockItem(SUPPORT_SPORE_BARREL.get(), new Item.Properties()));
        }
    }

    public static Pair<Supplier<Block>, Supplier<Block>> registerSkull(String name, SkullBlock.Type type, BiFunction<SkullBlock.Type, Properties, Block> base, BiFunction<SkullBlock.Type, Properties, Block> wall) {
        Supplier<Block> head = REGISTRIES.registerNoItem(name + "_head", () -> base.apply(type, Properties.ofFullCopy(Blocks.CREEPER_HEAD)));
        Supplier<Block> wall_head = REGISTRIES.registerNoItem(name + "_wall_head", () -> wall.apply(type, Properties.ofFullCopy(Blocks.CREEPER_WALL_HEAD)));
        REGISTRIES.registerItem(name + "_head", () -> new StandingAndWallBlockItem(head.get(), wall_head.get(), new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN));
        return new Pair<>(head, wall_head);
    }
}