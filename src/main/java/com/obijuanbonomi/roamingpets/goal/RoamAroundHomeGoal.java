package com.obijuanbonomi.roamingpets.goal;

import com.obijuanbonomi.roamingpets.api.RoamingTameable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import java.util.EnumSet;
import net.minecraft.util.math.random.Random;

/**
 * Mueve a la mascota dentro de un radio fijo alrededor de su posición "hogar".
 */
public class RoamAroundHomeGoal extends Goal {

    private final TameableEntity pet;
    private final double speed;
    private final int radius;
    private final Random rand;
    /* NEW: cooldown so the pet pauses briefly between walks */
    private int waitTicks = 0;

    public RoamAroundHomeGoal(TameableEntity pet, double speed, int radius) {
        this.pet = pet;
        this.speed = speed;
        this.radius = radius;
        this.rand = pet.getRandom();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    /* ----------- when can the goal (re)start? ----------- */
    @Override
    public boolean canStart() {
        return ((RoamingTameable) pet).rp$isRoaming()
                && pet.getNavigation().isIdle()
                && waitTicks == 0; // respect cooldown
    }

    /* ----------- perform one “hop” ----------- */
    @Override
    public void start() {
        BlockPos home = ((RoamingTameable) pet).rp$getRoamingHome();
        Vec3d dest = getRandomPosWithin(home);
        pet.getNavigation().startMovingTo(dest.x, dest.y, dest.z, speed);

        // NEW: pick a short random pause (40-80 ticks) after we arrive
        waitTicks = 40 + rand.nextInt(40);
    }

    /* ----------- tick() runs every game tick ----------- */
    @Override
    public void tick() {
        // count down the cooldown timer
        if (waitTicks > 0 && pet.getNavigation().isIdle()) {
            waitTicks--;
        }
    }

    /* ----------- helper to pick a random point ----------- */
    private Vec3d getRandomPosWithin(BlockPos center) {
        double angle = rand.nextDouble() * Math.PI * 2;
        double dist = rand.nextDouble() * radius;
        int dx = MathHelper.floor(dist * Math.cos(angle));
        int dz = MathHelper.floor(dist * Math.sin(angle));
        return Vec3d.ofCenter(center.add(dx, 0, dz));
    }
}
