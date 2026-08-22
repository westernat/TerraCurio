package org.confluence.terra_curio.common.item.curio.combat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.confluence.lib.common.LibEffects;
import org.confluence.lib.util.LibEntityUtils;
import org.confluence.terra_curio.common.item.curio.BaseCurioItem;
import org.confluence.terra_curio.util.CuriosUtils;
import top.theillusivec4.curios.api.SlotContext;

public class PaladinsShield extends BaseCurioItem {
    public PaladinsShield(Builder builder) {
        super(builder);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof ServerPlayer sp && sp.level().getGameTime() % 200 == 0) {
            Object team = LibEntityUtils.getTeam(sp);
            for (Player player : sp.level().players()) {
                if (LibEntityUtils.getTeam(player) != team) continue;
                player.addEffect(new MobEffectInstance(LibEffects.PALADINS_SHIELD.get(), 600, player == sp ? 1 : 0));
            }
        }
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return entity instanceof LivingEntity living && CuriosUtils.noSameCurio(living, PaladinsShield.class);
    }
}
