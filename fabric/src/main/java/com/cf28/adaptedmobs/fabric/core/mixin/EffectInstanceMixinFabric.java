package com.cf28.adaptedmobs.fabric.core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

// https://github.com/jamieohdev/the-bygone/blob/3.0/fabric/src/main/java/com/jamiedev/bygone/core/mixin/client/EffectInstanceMixinFabric.java
@Mixin(EffectInstance.class)
public class EffectInstanceMixinFabric {
    @Unique private final static String SHADER_LOCATION = "shaders/program/";

    @Unique private static ResourceLocation wrapMinecraftLocation(String location, String extension) {
        String trimmedLocation = location.substring(SHADER_LOCATION.length(), location.length() - extension.length());
        ResourceLocation shaderLocation = ResourceLocation.tryParse(trimmedLocation);
        if (shaderLocation == null) return null;
        return ResourceLocation.fromNamespaceAndPath(
            shaderLocation.getNamespace(), SHADER_LOCATION
                + shaderLocation.getPath() + extension
        );
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"
    ))
    private ResourceLocation adaptedMobs$fixPostChainNamespaceParse(String location, Operation<ResourceLocation> original) {
        ResourceLocation wrappedLocation = wrapMinecraftLocation(location,  ".json");
        if (wrappedLocation != null) return wrappedLocation;
        return original.call(location);
    }

    @WrapOperation(method = "getOrCreate", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"
    ))
    private static ResourceLocation adaptedMobs$fixShaderNamespaceParse(
        String location, Operation<ResourceLocation> original,
        @Local(argsOnly = true) Program.Type type
    ) {
        ResourceLocation wrappedLocation = wrapMinecraftLocation(location,  type.getExtension());
        if (wrappedLocation != null) return wrappedLocation;
        return original.call(location);
    }
}
