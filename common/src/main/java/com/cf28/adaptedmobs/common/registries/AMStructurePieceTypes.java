package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.cf28.adaptedmobs.common.level.levelgen.structure.HarpyNestPiece;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.function.Supplier;

public class AMStructurePieceTypes {
    public static final CoreRegistry<StructurePieceType> REGISTRIES = CoreRegistry.create(Registries.STRUCTURE_PIECE, AdaptedMobs.MOD_ID);

    public static final Supplier<StructurePieceType> HARPY_NEST = REGISTRIES.register("harpy_nest", () -> (StructurePieceType.StructureTemplateType) HarpyNestPiece::new);
}
