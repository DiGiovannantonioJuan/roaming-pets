package com.obijuanbonomi.swordblock.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.UseAnim;
import net.minecraft.util.Hand;
import net.minecraft.util.InteractionResultHolder;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwordItem.class)
public abstract class MixinSwordItem {
    @Inject(method = "getUseAnimation(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/UseAnim;", at = @At("HEAD"), cancellable = true)
    private void onGetUseAnimation(ItemStack stack, CallbackInfoReturnable<UseAnim> cir) {
        cir.setReturnValue(UseAnim.BLOCK);
    }

    @Inject(method = "getMaxUseTime(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void onGetMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(72000);
    }

    @Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/InteractionResultHolder;", at = @At("HEAD"), cancellable = true)
    private void onUse(World world, PlayerEntity user, Hand hand,
            CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.getItem() instanceof SwordItem) {
            user.setCurrentHand(hand);
            cir.setReturnValue(InteractionResultHolder.consume(stack));
        }
    }
}