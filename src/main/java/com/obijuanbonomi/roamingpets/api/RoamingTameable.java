package com.obijuanbonomi.roamingpets.api;

import net.minecraft.util.math.BlockPos;

/** Métodos expuestos por el mixin para que otras clases los usen. */
public interface RoamingTameable {
    boolean rp$isRoaming();

    void rp$setRoaming(boolean roaming, BlockPos home);

    BlockPos rp$getRoamingHome();
}
