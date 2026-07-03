package org.confluence.terra_curio.common.datagen;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes.PortAttributesExtension;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.confluence.lib.common.LibAttributes;
import org.confluence.terra_curio.api.primitive.*;
import org.confluence.terra_curio.common.component.PrimitiveValueComponent;
import org.confluence.terra_curio.common.init.TCDataMaps;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.init.TCTags;
import org.mesdag.portlib.datamap.PortDataMapProvider;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TCDataMapProvider extends PortDataMapProvider {
    private final AdvancedBuilder<PrimitiveValueComponent, Item, PrimitiveValueComponent.Remover> builder = builder(TCDataMaps.ACCESSORIES);

    protected TCDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        add(TCItems.ICE_SKATES, helper -> helper.unit(TCItems.ICE$SPEED));
        add(TCItems.ANGLER_EARRING, helper -> {
            helper.entry(TCItems.ATTRIBUTES, AttributeModifiersValue.simple(Attributes.LUCK, helper.asId(), 10, PortAttributeModifier.Operation.ADD_VALUE));
            helper.of(TCItems.COMPONENTS, List.of());
        });
        add(TCItems.MAGMA_STONE, helper -> helper.unit(TCItems.FIRE$ATTACK));
        add(TCItems.SHACKLE, helper -> helper.entry(TCItems.ATTRIBUTES, AttributeModifiersValue.simple(Attributes.ARMOR, helper.asId(), 1, PortAttributeModifier.Operation.ADD_VALUE)));
        add(TCItems.BASE_POINT, helper -> {
            helper.unit(TCItems.FIRE$IMMUNE);
            helper.unit(TCItems.ICE$SPEED);
            helper.of(TCItems.MAY$FLY, MayFlyAbilityValue.of(helper.asKey(), 1.1F, 70, true, true));
            helper.of(TCItems.FLUID$WALK, Set.of(TCTags.WATER_LIKE_WALK, TCTags.LAVA_LIKE_WALK));
            helper.of(TCItems.LAVA$IMMUNE$TICKS, 200);
            helper.of(TCItems.LAVA$HURT$REDUCE, 0.75F);
            ResourceLocation id = helper.asId();
            helper.entry(TCItems.ATTRIBUTES, AttributeModifiersValue.builder()
                    .add(Attributes.MOVEMENT_SPEED, id, 0.16, PortAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .add(PortAttributesExtension.fallDamageMultiplier().value(), id, -100, PortAttributeModifier.Operation.ADD_VALUE)
                    .add(PortAttributesExtension.jumpStrength().value(), id, 1, PortAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .add(PortAttributesExtension.stepHeight().value(), id, 0.5, PortAttributeModifier.Operation.ADD_VALUE)
                    .build());
        });
        add(TCItems.EVERLASTING, helper -> {
            helper.unit(TCItems.FROZEN$IMMUNE);
            helper.unit(TCItems.FIRE$IMMUNE);
            helper.unit(TCItems.SHIELD$OF$CTHULHU);
            helper.of(TCItems.EFFECT$IMMUNITIES, Set.of(
                    MobEffects.POISON,
                    MobEffects.WITHER,
                    MobEffects.WEAKNESS,
                    MobEffects.HUNGER,
                    MobEffects.BLINDNESS,
                    MobEffects.DARKNESS,
                    MobEffects.MOVEMENT_SLOWDOWN,
                    MobEffects.CONFUSION,
                    MobEffects.DIG_SLOWDOWN,
                    MobEffects.LEVITATION
            ));
            helper.of(TCItems.TOTEM$WITH$COOLDOWN, 2400);
            helper.of(TCItems.INVULNERABLE$TICKS$MULTIPLIER, 2.0F);
            helper.entry(TCItems.ATTRIBUTES, AttributeModifiersValue.simple(LibAttributes.getDodgeChance().value(), helper.asId(), 0.1, PortAttributeModifier.Operation.ADD_VALUE));
        });
    }

    @SuppressWarnings("deprecation")
    protected void add(Item item, Consumer<Helper> consumer) {
        Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> map = new Reference2ObjectOpenHashMap<>();
        consumer.accept(new Helper() {
            @Override
            public void unit(ValueType<Unit, ? extends UnitValue> type) {
                map.put(type, type.newInstance(Unit.INSTANCE));
            }

            @Override
            public <T1, V1 extends PrimitiveValue<T1>> void of(ValueType<T1, V1> type, T1 value) {
                map.put(type, type.newInstance(value));
            }

            @Override
            public <T1, V1 extends PrimitiveValue<T1>> void entry(ValueType<T1, V1> type, V1 value) {
                map.put(type, value);
            }

            @Override
            public ResourceLocation asId() {
                return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
            }

            @Override
            public ResourceKey<Item> asKey() {
                return item.builtInRegistryHolder().key();
            }
        });
        builder.add(item.builtInRegistryHolder().key(), new PrimitiveValueComponent(map), false);
    }

    protected void add(ItemLike item, Consumer<Helper> consumer) {
        add(item.asItem(), consumer);
    }

    public interface Helper {
        void unit(ValueType<Unit, ? extends UnitValue> type);

        <T, V extends PrimitiveValue<T>> void of(ValueType<T, V> type, T value);

        <T, V extends PrimitiveValue<T>> void entry(ValueType<T, V> type, V value);

        ResourceLocation asId();

        ResourceKey<Item> asKey();
    }
}
