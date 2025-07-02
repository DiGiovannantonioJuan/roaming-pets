package com.obijuanbonomi.roamingpets;

import com.obijuanbonomi.roamingpets.api.RoamingTameable;
import com.obijuanbonomi.roamingpets.goal.RoamAroundHomeGoal;
import com.obijuanbonomi.roamingpets.mixin.accessor.MobEntityAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class RoamingPets implements ModInitializer {

    private static final int RADIUS = 16;

    @Override
    public void onInitialize() {

        UseEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
            if (!(entity instanceof TameableEntity pet) || !pet.isOwner(player) || !player.isSneaking())
                return ActionResult.PASS;

            RoamingTameable rt = (RoamingTameable) pet;
            boolean nowRoaming = !rt.rp$isRoaming();
            GoalSelector goals = ((MobEntityAccessor) pet).roamingpets$getGoalSelector();

            if (nowRoaming) {
                rt.rp$setRoaming(true, pet.getBlockPos());
                removeGoal(goals, FollowOwnerGoal.class);
                goals.add(5, new RoamAroundHomeGoal(pet, 0.7D, RADIUS));
                pet.setSitting(false);
                player.sendMessage(Text.translatable("msg.roamingpets.enabled"), true);
            } else {
                rt.rp$setRoaming(false, null);
                removeGoal(goals, RoamAroundHomeGoal.class);
                goals.add(5, new FollowOwnerGoal(pet, 1.0D, 10.0F, 2.0F)); // ← firma nueva
                player.sendMessage(Text.translatable("msg.roamingpets.disabled"), true);
            }
            return ActionResult.SUCCESS;
        });
    }

    private static void removeGoal(GoalSelector selector, Class<?> clazz) {
        selector.getGoals().removeIf(w -> clazz.isAssignableFrom(w.getGoal().getClass()));
    }
}
