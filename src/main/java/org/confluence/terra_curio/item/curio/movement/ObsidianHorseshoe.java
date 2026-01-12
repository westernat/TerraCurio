package org.confluence.terra_curio.item.curio.movement;

import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.confluence.terra_curio.item.curio.BaseCurioItem;
import org.confluence.terra_curio.item.curio.combat.IFireImmune;
import org.confluence.terra_curio.misc.ModConfigs;
import org.confluence.terra_curio.misc.ModRarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ObsidianHorseshoe extends BaseCurioItem implements IFallResistance, IFireImmune {
    public ObsidianHorseshoe() {
        super(ModRarity.LIGHT_RED);
    }

    @Override
    public int getFallResistance() {
        return ModConfigs.OBSIDIAN_HORSESHOE_FALL_RESISTANCE.get();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return LuckyHorseshoe.getOrCreateAttributes();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(IFallResistance.TOOLTIP);
        list.add(IFireImmune.TOOLTIP);
    }
}
