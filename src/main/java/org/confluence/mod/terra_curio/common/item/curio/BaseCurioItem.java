package org.confluence.mod.terra_curio.common.item.curio;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.confluence.mod.terra_curio.TerraCurio;
import org.confluence.mod.terra_curio.common.component.EffectImmunities;
import org.confluence.mod.terra_curio.common.component.ModRarity;
import org.confluence.mod.terra_curio.common.init.ModDataComponentTypes;
import org.confluence.mod.terra_curio.common.misc.ModAttributes;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BaseCurioItem extends Item implements ICurioItem {

    private int JeiInformationCount;
    Multimap<Holder<Attribute>, AttributeModifier> attrs = ImmutableMultimap.of();
    List<String> additionTip = new ArrayList<>();

    public BaseCurioItem(String name,Function<Builder,Builder> properties) {
        super(properties.apply(new Builder(name)).properties);
        Builder builder = properties.apply(new Builder(name));
        attrs = builder.attrs;
        additionTip = builder.additionTip;
    }

    public BaseCurioItem(Properties properties) {
        super(properties);
    }


    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        return attrs;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip." + stack.getDescriptionId()));
        for (String s : additionTip) {
            tooltipComponents.add(Component.translatable("tooltip." + s));
        }

    }

    public int getJeiInformationCount() {
        return this.JeiInformationCount;
    }


    public static class Builder {

        private EffectImmunities effectImmunities;
        private ModRarity rarity;
        private final Multimap<Holder<Attribute>, AttributeModifier> attrs = ArrayListMultimap.create();

        private final Properties properties = new Properties();
        private final List<String> additionTip = new ArrayList<>();

        private final String name;
        Builder(String name){this.name = name;}

        public Builder addAttr(Holder<Attribute> attribute, String path, float amount, AttributeModifier.Operation operation) {
            this.attrs.put(attribute,new AttributeModifier(TerraCurio.asResource(name+"_"+path),amount,operation));
            return this;
        }

        public Builder armor(float amount){
            this.attrs.put(Attributes.ARMOR,new AttributeModifier(TerraCurio.asResource(name+"_builtin_armor"),amount, AttributeModifier.Operation.ADD_VALUE));
            return this;
        }

        public Builder damage( float amount, AttributeModifier.Operation operation){
            this.attrs.put(Attributes.ATTACK_DAMAGE,new AttributeModifier(TerraCurio.asResource(name+"_builtin_damage"),amount,operation));
            return this;
        }

        public Builder fallResistance( float amount, AttributeModifier.Operation operation){
            this.attrs.put(Attributes.FALL_DAMAGE_MULTIPLIER,new AttributeModifier(TerraCurio.asResource(name+"_builtin_fall_resistance"),amount,operation));
            return this;
        }

        public Builder fallImmune( float amount, AttributeModifier.Operation operation){
            this.attrs.put(Attributes.SAFE_FALL_DISTANCE,new AttributeModifier(TerraCurio.asResource(name+"_builtin_fall_immune"),amount,operation));
            return this;
        }

        public Builder jumpStrength( float amount){
            this.attrs.put(Attributes.JUMP_STRENGTH,new AttributeModifier(TerraCurio.asResource(name+"_builtin_jump_strength"),amount, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            return this;
        }

        public Builder fireImmune(){
            this.attrs.put(ModAttributes.FIRE_IMMUNE,new AttributeModifier(TerraCurio.asResource(name+"_fire_immune"),1, AttributeModifier.Operation.ADD_VALUE));
            return this;
        }

        public Builder lavaImmune( float amount){
            this.attrs.put(ModAttributes.LAVA_IMMUNE_TIME,new AttributeModifier(TerraCurio.asResource(name+"_lava_immune"),amount, AttributeModifier.Operation.ADD_VALUE));
            return this;
        }



        @SafeVarargs
        public final Builder effectImmunities(Holder<MobEffect>... effectImmunities) {
            this.effectImmunities = EffectImmunities.of(List.of(effectImmunities));
            return this;
        }


        public Builder rarity(ModRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder tip(String str) {
            this.additionTip.add(str);
            return this;
        }


        public Builder  build() {
            properties.component(ModDataComponentTypes.MOD_RARITY,rarity)
                    .component(ModDataComponentTypes.EFFECT_IMMUNITIES, effectImmunities)
                    .stacksTo(1);
            return this;
        }
    }

    public static class CuriosProperties extends Item.Properties {
        public CuriosProperties() {
            super();
        }

        public CuriosProperties rarity(ModRarity rarity) {
            this.component(ModDataComponentTypes.MOD_RARITY, rarity);
            return this;
        }
    }

}
