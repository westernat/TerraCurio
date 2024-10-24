package org.confluence.mod.terra_curio.common.event;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.misc.ModAttributes;

@EventBusSubscriber(modid = TerraCurio.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EntityHurtEvent {

    @SubscribeEvent
    public static void ImmuneEvent(EntityInvulnerabilityCheckEvent event) {
        var damageSource = event.getSource();
        if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            event.setInvulnerable(false);
            return;
        }
        if (event.getEntity() instanceof Player living) {
            if (
//                    ModAttributes.applyDodge(living) ||
                    living.getAttribute(ModAttributes.IMMUNE_TIME_REMAIN).getBaseValue()> 0 ||
                    living.getAttribute(ModAttributes.FIRE_IMMUNE).getValue()> 0 &&
                            (damageSource.is(DamageTypes.IN_FIRE) ||
                            damageSource.is(DamageTypes.ON_FIRE) ||
                            damageSource.is(DamageTypes.HOT_FLOOR) ||
                            damageSource.is(DamageTypes.UNATTRIBUTED_FIREBALL) ||
                            damageSource.is(DamageTypes.FIREBALL))||
                    living.getAttribute(ModAttributes.LAVA_IMMUNE_TIME_REMAIN).getValue()> 0 &&
                            damageSource.is(DamageTypes.LAVA)
            ){
                event.setInvulnerable(true);
                // 重置无敌帧
                if(living.getAttribute(ModAttributes.IMMUNE_TIME_REMAIN).getBaseValue()< 0){
                    living.getAttribute(ModAttributes.IMMUNE_TIME_REMAIN).setBaseValue(living.getAttribute(ModAttributes.IMMUNE_TIME).getValue());
                }
                return;
            }
        }
        event.setInvulnerable(false);
    }
    @SubscribeEvent
    public static void TickEvent(EntityTickEvent.Pre event) {

        if( event.getEntity() instanceof LivingEntity living && !living.level().isClientSide){
            var att = living.getAttribute(ModAttributes.LAVA_IMMUNE_TIME_REMAIN);
            var attMax = living.getAttribute(ModAttributes.LAVA_IMMUNE_TIME);
            if(attMax.getValue() > 0){
                if (living.isInLava()) {
                    att.setBaseValue(att.getBaseValue()-1);
                    System.out.println(att.getBaseValue());
                }else{
                    att.setBaseValue(attMax.getValue());
                }
            }

            att = living.getAttribute(ModAttributes.IMMUNE_TIME_REMAIN);
            attMax = living.getAttribute(ModAttributes.IMMUNE_TIME);
            if(attMax.getValue() > 0){
                att.setBaseValue(att.getBaseValue()-1);
                //System.out.println(att.getBaseValue());
            }else{
                att.setBaseValue(attMax.getValue());
            }



        }
    }
}

