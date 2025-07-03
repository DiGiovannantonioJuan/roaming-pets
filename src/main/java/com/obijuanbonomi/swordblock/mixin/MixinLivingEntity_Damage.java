package com.obijuanbonomi.swordblock.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity_Damage {
    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    private void onApplyDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self.isUsingItem() && self.getActiveItem().getUseAnimation() == UseAnim.BLOCK) {
            cir.setReturnValue(amount * 0.5f);
        }
    }
}