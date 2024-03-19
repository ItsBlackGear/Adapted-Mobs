package com.cf28.adaptedmobs.client.registry;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import com.cf28.adaptedmobs.core.platform.CoreRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class AMSoundEvents {
    public static final CoreRegistry<SoundEvent> SOUND_EVENTS = CoreRegistry.create(Registry.SOUND_EVENT, AdaptedMobs.MOD_ID);

    public static final Supplier<SoundEvent> ERRANT_AMBIENT = create("entity.errant.ambient");
    public static final Supplier<SoundEvent> ERRANT_HURT = create("entity.errant.hurt");

    private static Supplier<SoundEvent> create(String key) {
        return SOUND_EVENTS.register(key, () -> new SoundEvent(new ResourceLocation(AdaptedMobs.MOD_ID, key)));
    }
}