package org.confluence.mod.terra_curio.common.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.misc.ModAttributes;

@EventBusSubscriber(modid = TerraCurio.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CreateAttributeEvent {

    @SubscribeEvent
    public static void modify(EntityAttributeModificationEvent event) {
        ModAttributes.readJsonConfig();

        event.getTypes().forEach(type -> {
            if (type.getBaseClass().isAssignableFrom(LivingEntity.class)) {
                if(type == EntityType.PLAYER){
                    event.add(type, ModAttributes.PICKUP_RANGE);
                }

                //tip confirmed
                event.add(type, ModAttributes.IMMUNE_TIME);
                event.add(type, ModAttributes.IMMUNE_TIME_REMAIN);
                event.add(type, ModAttributes.LAVA_IMMUNE_TIME);
                event.add(type, ModAttributes.LAVA_IMMUNE_TIME_REMAIN);
                event.add(type, ModAttributes.FIRE_IMMUNE);


                event.add(type, ModAttributes.RANGED_DAMAGE);
                event.add(type, ModAttributes.RANGED_VELOCITY);
                event.add(type, ModAttributes.DODGE_CHANCE);
                event.add(type, ModAttributes.MINING_SPEED);
                event.add(type, ModAttributes.AGGRO);
                event.add(type, ModAttributes.MAGIC_DAMAGE);
                event.add(type, ModAttributes.ARMOR_PASS);

            }
        });

    }
}
