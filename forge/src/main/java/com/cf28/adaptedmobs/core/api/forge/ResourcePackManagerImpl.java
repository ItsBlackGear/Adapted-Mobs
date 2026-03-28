package com.cf28.adaptedmobs.core.api.forge;

import com.cf28.adaptedmobs.core.api.ResourcePackManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;

import java.nio.file.Path;
import java.util.function.Consumer;

public class ResourcePackManagerImpl {
    public static void registerPack(Consumer<ResourcePackManager.Event> listener) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((AddPackFindersEvent event) -> {
            listener.accept((type, source) -> {
                if (event.getPackType() == type) {
                    event.addRepositorySource(source);
                }
            });
        });
    }

    public static void registerBuiltInResourcePack(ResourceLocation packId, String modId, String packName) {
        IModFile file = ModList.get().getModFileById(modId).getFile();
        registerPack(event -> {
            event.register(PackType.CLIENT_RESOURCES, (consumer, factory) -> {
                Pack.create(
                    packId.toString(),
                    false,
                    () -> new ModFilePackResources(packName, file, "resourcepacks/" + packId.getPath()),
                    factory,
                    Pack.Position.TOP,
                    PackSource.DEFAULT
                );
            });
        });
    }

    private static class ModFilePackResources extends PathPackResources {
        private final IModFile modFile;
        private final String sourcePath;

        public ModFilePackResources(String packName, IModFile modFile, String sourcePath) {
            super(packName, modFile.findResource(sourcePath));
            this.modFile = modFile;
            this.sourcePath = sourcePath;
        }

        @Override
        protected Path resolve(String... paths) {
            String[] allPaths = new String[paths.length + 1];
            allPaths[0] = this.sourcePath;
            System.arraycopy(paths, 0, allPaths, 1, paths.length);
            return this.modFile.findResource(allPaths);
        }
    }
}