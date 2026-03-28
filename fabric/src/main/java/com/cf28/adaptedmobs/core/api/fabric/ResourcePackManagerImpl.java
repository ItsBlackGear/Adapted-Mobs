package com.cf28.adaptedmobs.core.api.fabric;

import com.cf28.adaptedmobs.core.api.ResourcePackManager;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ResourcePackManagerImpl {
    public static void registerPack(Consumer<ResourcePackManager.Event> listener) {
        //NO-OP
    }

    public static void registerBuiltInResourcePack(ResourceLocation packId, String modId, String packName) {
        ResourceManagerHelper.registerBuiltinResourcePack(
            packId,
            FabricLoader.getInstance().getModContainer(modId).orElseThrow(),
            packName,
            ResourcePackActivationType.NORMAL
        );
    }
}
