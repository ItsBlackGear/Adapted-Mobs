package com.cf28.adaptedmobs.core.mixin.common.potted_spores;

import com.cf28.adaptedmobs.common.level.block.AMPottedSporeBlock;
import com.cf28.adaptedmobs.common.registries.AMBlocks;
import com.cf28.adaptedmobs.common.registries.AMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(FlowerPotBlock.class)
public class FlowerPotBlockMixin {
    @Unique
    private static Supplier<Block> am$pottedPlantFor(ItemStack stack) {
        if (stack.is(AMItems.FESTIVE_CREEPER_SPORES.get())) {
            return AMBlocks.POTTED_FESTIVE_CREEPER_SPORES_PLANT;
        }
        if (stack.is(AMItems.ROCKET_CREEPER_SPORES.get())) {
            return AMBlocks.POTTED_ROCKET_CREEPER_SPORES_PLANT;
        }
        if (stack.is(AMItems.SUPPORT_CREEPER_SPORES.get())) {
            return AMBlocks.POTTED_SUPPORT_CREEPER_SPORES_PLANT;
        }
        return null;
    }

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void am$placeCreeperSpores(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (!state.is(Blocks.FLOWER_POT)) {
            return;
        }

        Supplier<Block> pottedPlant = am$pottedPlantFor(stack);
        if (pottedPlant == null) {
            return;
        }

        BlockState newState = pottedPlant.get().defaultBlockState()
                .setValue(AMPottedSporeBlock.FACING, player.getDirection().getOpposite());
        level.setBlock(pos, newState, 3);
        player.awardStat(Stats.POT_FLOWER);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        cir.setReturnValue(ItemInteractionResult.sidedSuccess(level.isClientSide));
    }
}
