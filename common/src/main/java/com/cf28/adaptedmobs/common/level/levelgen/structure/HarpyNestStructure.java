package com.cf28.adaptedmobs.common.level.levelgen.structure;

import com.cf28.adaptedmobs.common.registries.AMStructureTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class HarpyNestStructure extends Structure {
    public static final MapCodec<HarpyNestStructure> CODEC = simpleCodec(HarpyNestStructure::new);

    private static final int MIN_NEST_HEIGHT = 130;

    public HarpyNestStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();
        int surface = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

        if (surface < MIN_NEST_HEIGHT) {
            return Optional.empty();
        }

        BlockPos start = new BlockPos(x, surface + 1, z);
        Rotation rotation = Rotation.getRandom(context.random());
        return Optional.of(new Structure.GenerationStub(start, builder -> builder.addPiece(new HarpyNestPiece(context.structureTemplateManager(), start, rotation))));
    }

    @Override
    public StructureType<?> type() {
        return AMStructureTypes.HARPY_NEST.get();
    }
}
