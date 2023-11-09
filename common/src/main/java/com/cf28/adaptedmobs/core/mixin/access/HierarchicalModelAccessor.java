package com.cf28.adaptedmobs.core.mixin.access;

import com.mojang.math.Vector3f;
import net.minecraft.client.model.HierarchicalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HierarchicalModel.class)
public interface HierarchicalModelAccessor {
    @Accessor
    static Vector3f getANIMATION_VECTOR_CACHE() {
        throw new UnsupportedOperationException();
    }
}
