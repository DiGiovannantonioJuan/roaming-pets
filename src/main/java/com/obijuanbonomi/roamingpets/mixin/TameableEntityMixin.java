package com.obijuanbonomi.roamingpets.mixin;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.obijuanbonomi.roamingpets.api.RoamingTameable;

/**
 * Añade la bandera "roaming" + posición hogar a perros y gatos domesticados.
 */
@Mixin(TameableEntity.class)
public abstract class TameableEntityMixin implements RoamingTameable {

    /** Clave global para el data-tracker */
    private static final TrackedData<Boolean> ROAMING = DataTracker.registerData(TameableEntity.class,
            TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private BlockPos roamingHome = null;

    /*------------------------------------------------------------
     *  1 · Registrar el dato en el nuevo Builder (1.21+)
     *-----------------------------------------------------------*/
    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void rp$registerTrackedData(Builder builder, CallbackInfo ci) {
        builder.add(ROAMING, false); // equivalente a startTracking(...)
    }

    /*------------------------------------------------------------
     *  2 · Getters / setters auxiliares
     *-----------------------------------------------------------*/
    public boolean rp$isRoaming() {
        return ((TameableEntity) (Object) this).getDataTracker().get(ROAMING);
    }

    public void rp$setRoaming(boolean value, BlockPos home) {
        ((TameableEntity) (Object) this).getDataTracker().set(ROAMING, value);
        this.roamingHome = home;
    }

    public BlockPos rp$getRoamingHome() {
        return roamingHome;
    }

    /*------------------------------------------------------------
     *  3 · Persistencia NBT (nuevos getters con default)
     *-----------------------------------------------------------*/
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void rp$write(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("Roaming", rp$isRoaming());
        if (roamingHome != null)
            nbt.putLong("RoamingHome", roamingHome.asLong());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void rp$read(NbtCompound nbt, CallbackInfo ci) {
        boolean roaming = nbt.getBoolean("Roaming", false);
        long homeLong = nbt.getLong("RoamingHome", 0L);

        BlockPos home = homeLong != 0L ? BlockPos.fromLong(homeLong)
                : ((TameableEntity) (Object) this).getBlockPos();
        rp$setRoaming(roaming, home);
    }
}
