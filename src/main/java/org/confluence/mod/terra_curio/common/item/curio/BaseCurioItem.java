package org.confluence.mod.terra_curio.common.item.curio;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.mod.terra_curio.client.component.EffectImmunities;
import org.confluence.mod.terra_curio.common.init.ModDataComponentTypes;
import org.confluence.mod.terra_curio.common.misc.ModRarity;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BaseCurioItem extends Item implements ICurioItem {

    public static final Codec<BaseCurioItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EffectImmunities.CODEC.fieldOf("immunities").forGetter(BaseCurioItem::getEffectImmunities),
            Codec.INT.fieldOf("jei_information_count").forGetter(BaseCurioItem::getJeiInformationCount)
    ).apply(instance, BaseCurioItem::new));

    public EffectImmunities effectImmunities;
    private int JeiInformationCount;


    public BaseCurioItem(List<Holder<MobEffect>> effectImmunities, int JeiInformationCount, ModRarity rarity) {
        super(new Properties().fireResistant().component(ModDataComponentTypes.MOD_RARITY,rarity).component(ModDataComponentTypes.EFFECT_IMMUNITIES,EffectImmunities.of(effectImmunities)).stacksTo(1));
        this.JeiInformationCount = JeiInformationCount;
    }

    public BaseCurioItem(EffectImmunities effectImmunities, int JeiInformationCount,ModRarity rarity) {
        super(new Properties().fireResistant().component(ModDataComponentTypes.MOD_RARITY,rarity).component(ModDataComponentTypes.EFFECT_IMMUNITIES,effectImmunities).stacksTo(1));
        this.JeiInformationCount = JeiInformationCount;
    }

    public BaseCurioItem(EffectImmunities effectImmunities, Integer integer) {
        super(new Properties().fireResistant().component(ModDataComponentTypes.EFFECT_IMMUNITIES,effectImmunities).stacksTo(1));
    }

    public BaseCurioItem(ModRarity rarity) {
        super(new Properties().fireResistant().component(ModDataComponentTypes.MOD_RARITY,rarity).component(ModDataComponentTypes.EFFECT_IMMUNITIES,EffectImmunities.EMPTY).stacksTo(1)); }

    public BaseCurioItem() {
        super(new Properties().fireResistant().component(ModDataComponentTypes.EFFECT_IMMUNITIES,EffectImmunities.EMPTY).stacksTo(1)); }




    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId()));
    }

    public EffectImmunities getEffectImmunities() {
        return effectImmunities;
    }

    public int getJeiInformationCount() {
        return this.JeiInformationCount;
    }
}
