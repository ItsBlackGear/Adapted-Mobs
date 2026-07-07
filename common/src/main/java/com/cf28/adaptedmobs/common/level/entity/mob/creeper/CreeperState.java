package com.cf28.adaptedmobs.common.level.entity.mob.creeper;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum CreeperState implements StringRepresentable {
    IDLING("idling"),
    SITTING("sitting"),
    STANDING("standing"),
    ATTACKING("attacking");

    private static final IntFunction<CreeperState> BY_ID = ByIdMap.continuous(Enum::ordinal, CreeperState.values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
    public static final StreamCodec<ByteBuf, CreeperState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
    private final String name;
    
    CreeperState(String name) {
        this.name = name;
    }
    
    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
    
    public boolean is(CreeperState state) {
        return state == this;
    }
}