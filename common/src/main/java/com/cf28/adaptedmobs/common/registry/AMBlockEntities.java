package com.cf28.adaptedmobs.common.registry;

import com.blackgear.platform.core.CoreRegistry;
import com.cf28.adaptedmobs.common.blockentity.PlatformSkullBlockEntity;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.datafixers.types.Type;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class AMBlockEntities {
    public static final CoreRegistry<BlockEntityType<?>> BLOCK_ENTITIES = CoreRegistry.create(Registry.BLOCK_ENTITY_TYPE, AdaptedMobs.MOD_ID);

    public static final Supplier<BlockEntityType<PlatformSkullBlockEntity>> SKULL = register(
        "extra_skull",
        BlockEntityTypeBuilder.create(
            PlatformSkullBlockEntity::new,
            AMBlocks.FESTIVE_CREEPER_HEAD,
            AMBlocks.FESTIVE_CREEPER_WALL_HEAD,
            AMBlocks.SUPPORT_CREEPER_HEAD,
            AMBlocks.SUPPORT_CREEPER_WALL_HEAD,
            AMBlocks.PEEPER_CREEPER_HEAD,
            AMBlocks.PEEPER_CREEPER_WALL_HEAD,
            AMBlocks.ROCKET_CREEPER_HEAD,
            AMBlocks.ROCKET_CREEPER_WALL_HEAD
        )
    );

    private static <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BlockEntityTypeBuilder<T> builder) {
        return BLOCK_ENTITIES.register(name, builder::build);
    }

    private record BlockEntityTypeBuilder<T extends BlockEntity>(BlockEntityTypeBuilder.Factory<? extends T> factory, List<Supplier<Block>> blocks) {
        @SafeVarargs
        public static <T extends BlockEntity> BlockEntityTypeBuilder<T> create(Factory<? extends T> factory, Supplier<Block>... blocks) {
            List<Supplier<Block>> entries = new ArrayList<>(blocks.length);
            Collections.addAll(entries, blocks);
            return new BlockEntityTypeBuilder<>(factory, entries);
        }

        public BlockEntityTypeBuilder<T> add(Supplier<Block> block) {
            this.blocks.add(block);
            return this;
        }

        @SafeVarargs
        public final BlockEntityTypeBuilder<T> add(Supplier<Block>... block) {
            Collections.addAll(this.blocks, block);
            return this;
        }

        public BlockEntityType<T> build() {
            return build(null);
        }

        public BlockEntityType<T> build(Type<?> type) {
            return BlockEntityType.Builder.<T>of(this.factory::create, this.blocks.stream().map(Supplier::get).toArray(Block[]::new)).build(type);
        }

        public interface Factory<T extends BlockEntity> {
            T create(BlockPos origin, BlockState state);
        }
    }
}