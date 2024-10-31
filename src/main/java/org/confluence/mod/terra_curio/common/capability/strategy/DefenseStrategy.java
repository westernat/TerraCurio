package org.confluence.mod.terra_curio.common.capability.strategy;

import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.confluence.mod.terra_curio.common.effect.ModEffects;
import org.confluence.mod.terra_curio.common.entity.projectile.BeeProjectile;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class DefenseStrategy {
    public static Map<String, TriConsumer<LivingEntity, LivingEntity,Float>> RECORDERS = new HashMap<>();

    public static final String DEFENSE_TEST = register("reverse", (player, entity, damage) -> {
        player.heal(damage);


    });



    public static String register(String key, TriConsumer<LivingEntity, LivingEntity,Float> effect){
        RECORDERS.put(key, effect);
        return key;
    }
    public static TriConsumer<LivingEntity, LivingEntity,Float> getEffect(String key){
        return RECORDERS.get(key);
    }
    public static void execute(Set<String > keys, Player player, LivingEntity entity, float damage){
        for(String key : keys){
            TriConsumer<LivingEntity, LivingEntity,Float> effect = getEffect(key);
            if(effect != null){
                effect.accept(player, entity,damage);
            }
        }
    }
    public interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}
