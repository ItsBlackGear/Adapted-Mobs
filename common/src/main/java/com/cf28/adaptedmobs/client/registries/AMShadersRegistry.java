package com.cf28.adaptedmobs.client.registries;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AMShadersRegistry {
    private static ShaderInstance MINDMOULD_SHADER;
    public static ShaderInstance getMindmouldShader() { return MINDMOULD_SHADER; }

    @FunctionalInterface
    public interface AMShaderRegisterEvent {
        void register(ResourceLocation location, VertexFormat vertexFormat, Consumer<ShaderInstance> consumer) throws IOException;
    }

    public static void bootstrap(final AMShaderRegisterEvent event) throws IOException {
        event.register(
            AdaptedMobs.resource("rendertype_mindmould_facade"),
            DefaultVertexFormat.NEW_ENTITY,
            (shader) -> MINDMOULD_SHADER = shader
        );
    }
}
