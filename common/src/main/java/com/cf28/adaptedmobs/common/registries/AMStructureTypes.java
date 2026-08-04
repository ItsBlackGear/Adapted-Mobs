package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.cf28.adaptedmobs.common.level.levelgen.structure.HarpyNestStructure;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.function.Supplier;

public class AMStructureTypes {
    public static final CoreRegistry<StructureType<?>> REGISTRIES = CoreRegistry.create(Registries.STRUCTURE_TYPE, AdaptedMobs.MOD_ID);

    public static final Supplier<StructureType<HarpyNestStructure>> HARPY_NEST = REGISTRIES.register("harpy_nest", () -> typeOf(HarpyNestStructure.CODEC));

    public static final ResourceKey<Structure> HARPY_NEST_STRUCTURE = ResourceKey.create(Registries.STRUCTURE, AdaptedMobs.resource("harpy_nest"));

    private static <S extends Structure> StructureType<S> typeOf(MapCodec<S> codec) {
        return () -> codec;
    }
}
