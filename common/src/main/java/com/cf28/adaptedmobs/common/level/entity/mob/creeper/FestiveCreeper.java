package com.cf28.adaptedmobs.common.level.entity.mob.creeper;

import com.cf28.adaptedmobs.common.integrations.TolerableCreepersCompat;
import com.cf28.adaptedmobs.common.integrations.TolerableCreepersIntegration;
import com.cf28.adaptedmobs.common.level.entity.ai.goal.BackUpIfTooCloseGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.goal.ThrowTntToTargetGoal;
import com.cf28.adaptedmobs.common.level.entity.ai.pathfinding.move_control.BackUpMoveControl;
import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

public class FestiveCreeper extends TamableCreeper {
    private static final ResourceKey<LootTable> AM_EXPLODE_LOOT_TABLE =
            ResourceKey.create(Registries.LOOT_TABLE,
                    ResourceLocation.fromNamespaceAndPath(AdaptedMobs.MOD_ID, "entities/festive_creeper_explode"));

    public FestiveCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new BackUpMoveControl(this);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ThrowTntToTargetGoal(this, UniformInt.of(18, 20), UniformInt.of(15, 18)));
        this.goalSelector.addGoal(3, new BackUpIfTooCloseGoal(this, 8, 2.25F));
    }

    @Override
    public void tick() {
        this.setupAnimations();
        super.tick();
    }

    @Override
    public void setState(CreeperState state) {
        if (state.is(CreeperState.ATTACKING)) {
            this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 1.0F);
            super.setState(CreeperState.ATTACKING);
        } else {
            super.setState(state);
        }
    }

    @Override
    protected ItemStack getSkull() {
        return new ItemStack(AMBlocks.FESTIVE_CREEPER_HEAD.getFirst().get());
    }

    @Override
    protected void explodeCreeper() {
        if (this.shouldSwell() && !this.isTame() && TolerableCreepersCompat.isLoaded() && AdaptedMobs.CONFIG.preventFestiveCreeperBlockDamage.get()) {
            if (!this.level().isClientSide()) {
                float explosionMultiplier = this.isPowered() ? 2.0F : 1.0F;
                this.explodeWithoutBlockDamage((float) this.explosionRadius * explosionMultiplier);
                this.discard();
                this.postExplosion();
            }
        } else {
            super.explodeCreeper();
        }
    }

    @Override
    protected ResourceKey<LootTable> getExplosionLootTable() {
        return TolerableCreepersCompat.isLoaded() ? AM_EXPLODE_LOOT_TABLE : null;
    }

    @Override
    public boolean canDropMobsSkull() {
        return false;
    }

    @Override
    protected void postExplosion() {
        super.postExplosion();
        if (!this.level().isClientSide() && !this.isTame() && TolerableCreepersCompat.isLoaded()) {
            Entity spores = TolerableCreepersIntegration.createFestiveSporesFromCreeper(this.level(), this);
            this.level().addFreshEntity(spores);
        }
    }

    @Override
    public ClothType getClothType() {
        return ClothType.FESTIVE;
    }
}