package org.confluence.mod.terra_curio.common.capability.strategy;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AttackEntityStrategy {
    public static Map<String, BiConsumer<Player, LivingEntity>> RECORDERS = new HashMap<>();
    public static String FIRE_ATTACK = register("fire_attack", (player, entity) -> {
        float f = player.getRandom().nextFloat();
        int time;
        if (f < 0.25F) {
            time = 120;
        } else if (f < 0.375F) {
            time = 80;
        } else {
            time = 40;
        }
        entity.igniteForTicks(time);
    });

    public static String LuckyCoin;

    public static String Test = register("shine", (player, entity) -> {

        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING,100));
    });



    public static String register(String key, BiConsumer<Player, LivingEntity> effect){
        RECORDERS.put(key, effect);
        return key;
    }

    public static BiConsumer<Player, LivingEntity> getEffect(String key){
        return RECORDERS.get(key);
    }

    public static void execute(Set<String > keys, Player player, LivingEntity entity){
        for(String key : keys){
            BiConsumer<Player, LivingEntity> effect = getEffect(key);
            if(effect != null){
                effect.accept(player, entity);
            }
        }
    }
}
