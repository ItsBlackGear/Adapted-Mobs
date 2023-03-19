package com.cf28.adaptedmobs.core.mixin;

import com.cf28.adaptedmobs.common.entity.PrimedFestiveTnt;
import com.cf28.adaptedmobs.common.entity.creeper.FestiveCreeper;
import com.cf28.adaptedmobs.common.entity.creeper.TamableCreeper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract void readAdditionalSaveData(CompoundTag compound);

    private final LivingEntity instance = LivingEntity.class.cast(this);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        Entity entity = source.getEntity();
//        if (source.getDirectEntity() instanceof PrimedFestiveTnt tnt && source.isExplosion()) {
//            if (tnt.getOwner() instanceof FestiveCreeper creeper) {
//                if (this.instance instanceof TamableAnimal tame) {
//                    return creeper.isTame() && creeper.getOwner() == tame.getOwner();
//                }
//
//                return creeper.isOwnedBy(this.instance);
//            }
//        } else if (entity instanceof TamableCreeper creeper) {
//            return creeper.isOwnedBy(this.instance);
//        }

//        if (source.getDirectEntity() instanceof PrimedFestiveTnt) {
//            return true;
//        } else if (source.getEntity() instanceof PrimedFestiveTnt) {
//            return true;
//        }

        return super.isInvulnerableTo(source);
    }

    @Inject(method = "hurt", at = @At("TAIL"), cancellable = true)
    private void am$hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
//        if (source.getDirectEntity() instanceof PrimedFestiveTnt && source.isExplosion()) {
//            cir.setReturnValue(false);
//        } else if (source.getEntity() instanceof PrimedFestiveTnt && source.isExplosion()) {
//            cir.setReturnValue(false);
//        }
//
//        if (source.isExplosion()) {
//            cir.setReturnValue(false);
//        }

//        Entity entity = source.getEntity();
//
//
//        if (source.getDirectEntity() instanceof PrimedFestiveTnt) {
//            cir.setReturnValue(false);
//        } else if (entity instanceof TamableCreeper creeper) {
//            cir.setReturnValue(!creeper.isOwnedBy(this.instance));
//        } else {
//            cir.setReturnValue(cir.getReturnValue());
//        }
    }
}