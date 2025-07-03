package com.obijuanbonomi.swordblock.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity_Knockback {
    @Redirect(method = "applyKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;knockBack(FDD)V"))
    private void onKnockback(LivingEntity entity, float strength, double x, double z) {
        if (entity.isUsingItem() && entity.getActiveItem().getUseAnimation() == UseAnim.BLOCK) {
            entity.knockBack(strength * 0.5f, x, z);
        } else {
            entity.knockBack(strength, x, z);
        }
    }
}