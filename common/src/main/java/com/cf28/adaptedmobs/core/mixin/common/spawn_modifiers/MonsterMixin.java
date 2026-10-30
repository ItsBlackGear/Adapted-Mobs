package com.cf28.adaptedmobs.core.mixin.common.spawn_modifiers;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Monster.class)
public class MonsterMixin {
    @Inject(method = "checkMonsterSpawnRules", at = @At("RETURN"), cancellable = true)
    private static void am$applyMonsterSpawnModifier(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || spawnType != MobSpawnType.NATURAL) return;

        if (type == EntityType.SKELETON && random.nextFloat() < AdaptedMobs.CONFIG.skeletonSpawnModifierChance.get()) {
            cir.setReturnValue(false);
        }
    }
}