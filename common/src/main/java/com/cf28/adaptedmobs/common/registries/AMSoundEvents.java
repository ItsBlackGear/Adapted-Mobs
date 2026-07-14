package com.cf28.adaptedmobs.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

public class AMSoundEvents {
    public static final CoreRegistry<SoundEvent> REGISTRIES = CoreRegistry.create(Registries.SOUND_EVENT, AdaptedMobs.MOD_ID);

    public static final Holder<SoundEvent> SUPPORT_CREEPIE_BLAST = REGISTRIES.holder("support_creepie_blast", () ->
            SoundEvent.createVariableRangeEvent(AdaptedMobs.resource("support_creepie_blast")));
}
