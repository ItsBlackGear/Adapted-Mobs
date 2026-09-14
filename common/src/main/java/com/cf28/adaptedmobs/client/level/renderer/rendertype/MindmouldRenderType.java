package com.cf28.adaptedmobs.client.level.renderer.rendertype;

import com.cf28.adaptedmobs.client.registries.AMShadersRegistry;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public final class MindmouldRenderType extends RenderType {

    private MindmouldRenderType(
        String name, VertexFormat format,
        VertexFormat.Mode mode, int bufferSize,
        boolean affectsCrumbling, boolean sortOnUpload,
        Runnable setupState, Runnable clearState
    ) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType mindmould() {
        return RenderType.create(
            "adaptedmobs_rendertype_mindmould_facade",
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
            1536, true, true,
            RenderType.CompositeState.builder()
                .setShaderState(
                    new RenderStateShard.ShaderStateShard(AMShadersRegistry::getMindmouldShader)
                ).setLightmapState(LIGHTMAP)
                .setTextureState(BLOCK_SHEET_MIPPED)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setOutputState(TRANSLUCENT_TARGET)
                .createCompositeState(true)
        );
    }
}
