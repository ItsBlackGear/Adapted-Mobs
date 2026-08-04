package com.cf28.adaptedmobs.common.level.levelgen.structure;

import com.cf28.adaptedmobs.common.level.entity.mob.Harpy;
import com.cf28.adaptedmobs.common.registries.AMEntityTypes;
import com.cf28.adaptedmobs.common.registries.AMStructurePieceTypes;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class HarpyNestPiece extends TemplateStructurePiece {
    public static final ResourceLocation TEMPLATE = AdaptedMobs.resource("harpy_nest");

    private static final int MIN_HARPIES = 2;
    private static final int MAX_HARPIES = 3;
    private static final int SCATTER_RADIUS = 2;

    private boolean spawnedHarpies;

    public HarpyNestPiece(StructureTemplateManager templateManager, BlockPos pos, Rotation rotation) {
        super(AMStructurePieceTypes.HARPY_NEST.get(), 0, templateManager, TEMPLATE, TEMPLATE.toString(), makeSettings(rotation), pos);
    }

    public HarpyNestPiece(StructureTemplateManager templateManager, CompoundTag tag) {
        super(AMStructurePieceTypes.HARPY_NEST.get(), tag, templateManager, location -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
        this.spawnedHarpies = tag.getBoolean("SpawnedHarpies");
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(true)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putString("Rot", this.placeSettings.getRotation().name());
        tag.putBoolean("SpawnedHarpies", this.spawnedHarpies);
    }

    @Override
    protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        super.postProcess(level, structureManager, generator, random, box, chunkPos, pos);
        this.spawnHarpies(level, box);
    }

    private void spawnHarpies(WorldGenLevel level, BoundingBox box) {
        if (this.spawnedHarpies) {
            return;
        }

        BlockPos nest = this.templatePosition;
        if (!box.isInside(nest)) {
            return;
        }

        this.spawnedHarpies = true;
        RandomSource random = level.getRandom();
        int count = MIN_HARPIES + random.nextInt(MAX_HARPIES - MIN_HARPIES + 1);

        for (int i = 0; i < count; i++) {
            Harpy harpy = AMEntityTypes.HARPY.get().create(level.getLevel());
            if (harpy == null) {
                continue;
            }

            double x = nest.getX() + 0.5D + random.nextInt(SCATTER_RADIUS * 2 + 1) - SCATTER_RADIUS;
            double z = nest.getZ() + 0.5D + random.nextInt(SCATTER_RADIUS * 2 + 1) - SCATTER_RADIUS;
            double y = nest.getY() + 1 + random.nextInt(3);

            harpy.setPersistenceRequired();
            harpy.moveTo(x, y, z, random.nextFloat() * 360.0F, 0.0F);
            harpy.finalizeSpawn(level, level.getCurrentDifficultyAt(nest), MobSpawnType.STRUCTURE, null);
            level.addFreshEntityWithPassengers(harpy);
        }
    }
}
