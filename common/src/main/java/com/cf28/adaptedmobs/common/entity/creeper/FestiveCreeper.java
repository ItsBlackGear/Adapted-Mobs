package com.cf28.adaptedmobs.common.entity.creeper;

import com.cf28.adaptedmobs.common.entity.creeper.ai.StrafeGoal;
import com.cf28.adaptedmobs.common.entity.creeper.ai.ThrowTntToTargetGoal;
import com.cf28.adaptedmobs.common.entity.resource.CreeperState;
import com.cf28.adaptedmobs.common.registry.AMBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * FEATURES:
 * - they throw TNT at you and are very destructive! *
 * - when trying to approach a festive creeper, it will back away *
 * - when charged, their TNT is stronger *
 * - they have a rare chance of dropping a red Mystery Egg that hatches a tame baby Festive Creeper *
 * - baby Festive Creepers are.. quite useless... *
 * - but once they grow up, they will fight alongside you! *
 * - If they get hurt, you can heal them with gunpowder *
 * ADDITIONAL CHANGES:
 * - tries to keep a distance from the player by walking backwards while shooting festive TNT *
 * - festive TNT is unobtainable and does not grief blocks, but deals 25 damage at point-blank distance *
 * - when charged, the TNT they shoot is regular TNT that destroy blocks, and deal the usual amount of damage *
 * LOOT:
 * - gunpowder [similar to creepers]
 * - TNT [rare]
 * - red mystery egg [very rare]
 */
public class FestiveCreeper extends TamableCreeper {
    public FestiveCreeper(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ThrowTntToTargetGoal(this, UniformInt.of(10, 20)));
        this.goalSelector.addGoal(3, new StrafeGoal(this, 5.5, 1.25));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
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
        return new ItemStack(AMBlocks.FESTIVE_CREEPER_HEAD.get());
    }

    @Override
    public boolean canDropMobsSkull() {
        return false;
    }

    @Override
    public boolean detonateOnInteraction() {
        return false;
    }

    @Override
    public boolean shouldSwell() {
        return false;
    }

    @Override
    public ClothType getClothType() {
        return ClothType.FESTIVE;
    }
}