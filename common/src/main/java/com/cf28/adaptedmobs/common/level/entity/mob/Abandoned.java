package com.cf28.adaptedmobs.common.level.entity.mob;

import com.cf28.adaptedmobs.core.AdaptedMobs;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.Nullable;

public class Abandoned extends Skeleton {
    private static final EntityDataAccessor<ItemStack> STOWED_ITEM = SynchedEntityData.defineId(Abandoned.class, EntityDataSerializers.ITEM_STACK);
    
    private static final double BOW_DISTANCE = 10.0 * 10.0;
    private static final double SWORD_DISTANCE = 6.0 * 6.0;
    private static final int WEAKNESS_TICKS = 200;
    
    private int switchCooldown = 0;
    
    public Abandoned(EntityType<? extends Skeleton> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STOWED_ITEM, ItemStack.EMPTY);
    }
    
    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        Item sword = random.nextInt(4) == 0 ? Items.STONE_SWORD : Items.IRON_SWORD;
        
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(sword));
        this.setStowedItem(new ItemStack(Items.BOW));
        
        ItemStack helmet = this.rollHelmet(random);
        if (!helmet.isEmpty()) {
            this.setItemSlot(EquipmentSlot.HEAD, helmet);
        }
    }
    
    private ItemStack rollHelmet(RandomSource random) {
        float roll = random.nextFloat();
        
        return roll < 0.005F
            ? Items.IRON_HELMET.getDefaultInstance()
            : roll < 0.1F
              ? Items.CHAINMAIL_HELMET.getDefaultInstance()
              : ItemStack.EMPTY;
    }
    
    @Override
    protected AbstractArrow getArrow(ItemStack arrowStack, float velocity, @Nullable ItemStack weapon) {
        AbstractArrow arrow = super.getArrow(arrowStack, velocity, weapon);
        
        if (arrow instanceof Arrow tippedArrow) {
            tippedArrow.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, WEAKNESS_TICKS));
        }
        
        return arrow;
    }
    
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        
        LivingEntity target = this.getTarget();
        
        if (this.switchCooldown > 0) {
            this.switchCooldown--;
        } else if (target != null && target.isAlive()) {
            this.updateWeapon(this.distanceToSqr(target));
        }
    }
    
    private void updateWeapon(double distSqr) {
        boolean holdingBow = this.getMainHandItem().is(Items.BOW);
        
        boolean shouldUseBow = holdingBow ? distSqr >= SWORD_DISTANCE : distSqr > BOW_DISTANCE;
        
        if (shouldUseBow != holdingBow) {
            this.swapWeapons();
        }
    }
    
    private void swapWeapons() {
        ItemStack previouslyHeld = this.getMainHandItem().copy();
        
        this.stopUsingItem();
        this.setItemSlot(EquipmentSlot.MAINHAND, this.getStowedItem());
        this.setStowedItem(previouslyHeld);
        this.switchCooldown = 20;
    }
    
    public ItemStack getStowedItem() {
        return this.entityData.get(STOWED_ITEM);
    }
    
    private void setStowedItem(ItemStack stack) {
        this.entityData.set(STOWED_ITEM, stack);
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("StowedItem", this.getStowedItem().save(this.level().registryAccess()));
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ItemStack.parse(this.level().registryAccess(), tag.getCompound("StowedItem")).ifPresent(this::setStowedItem);
    }
    
    public static <T extends Abandoned> boolean checkAbandonedSpawnRules(EntityType<T> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return Monster.checkMonsterSpawnRules(type, level, reason, pos, random) && passesDepthRoll(pos.getY(), random);
    }
    
    private static boolean passesDepthRoll(int y, RandomSource random) {
        int maxSpawnY = AdaptedMobs.CONFIG.abandonedMaximumSpawnY.get();
        return y < maxSpawnY || (y <= maxSpawnY + 15 && random.nextInt(8) == 0);
    }
}