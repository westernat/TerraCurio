package org.confluence.terra_curio.common.attachment;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.confluence.terra_curio.api.event.RegisterAccessoriesComponentUpdateEvent;
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.UnitValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.item.curio.combat.PanicNecklace;
import org.confluence.terra_curio.util.MobEntityTypesTest;
import org.confluence.terra_curio.util.TCUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;

import static org.confluence.terra_curio.util.TCUtils.tryCast;

@SuppressWarnings("unchecked")
public class AccessoriesAttachment implements INBTSerializable<CompoundTag> {
    public static final List<ValueType<Unit, UnitValue>> UNITS_REQUIRE_UPDATE = Util.make(new ArrayList<>(), list -> {
        list.add(ValueType.FIRE$ATTACK);
        list.add(ValueType.BRAIN$OF$CONFUSION);
        list.add(ValueType.HIVE$PACK);
        list.add(ValueType.HONEY$COMB);
        list.add(ValueType.MAGIC$QUIVER);
        list.add(ValueType.IGNITE$ARROW);
        list.add(ValueType.FROZEN$TURTLE$SHELL);
        list.add(ValueType.FIRE$IMMUNE);
        list.add(ValueType.FLOWER$BOOTS);
        list.add(ValueType.FROZEN$IMMUNE);
        list.add(ValueType.ICE$SPEED);
        ModLoader.postEvent(new RegisterAccessoriesComponentUpdateEvent.UnitType(list));
    });
    public static final List<ValueType<?, ? extends PrimitiveValue<?>>> OTHER_REQUIRE_UPDATE = Util.make(new ArrayList<>(), list -> {
        list.add(ValueType.STAR$CLOCK);
        list.add(ValueType.INJURY$FREE);
        list.add(ValueType.INVULNERABLE$TICKS$MULTIPLIER);
        list.add(ValueType.LAVA$HURT$REDUCE);
        list.add(ValueType.LAVA$IMMUNE$TICKS);
        list.add(ValueType.RIGHT$CLICK$DELAY$SUBSTRACTOR);
        list.add(ValueType.MOB$IGNORE);
        list.add(ValueType.FLUID$WALK);
        list.add(ValueType.WALL$CLIMB);
        list.add(ValueType.FART);
        list.add(ValueType.SAND$STORM);
        list.add(ValueType.BLIZZARD);
        list.add(ValueType.TSUNAMI);
        list.add(ValueType.CLOUD);
        list.add(ValueType.MAY$FLY);
        list.add(ValueType.EFFECT$IMMUNITIES);
        list.add(ValueType.TOTEM$WITH$COOLDOWN);
        ModLoader.postEvent(new RegisterAccessoriesComponentUpdateEvent.OtherType(list));
    });
    private final Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> valueMap = new HashMap<>();
    private boolean panicNecklace;
    private transient int remainLavaImmuneTicks;

    public AccessoriesAttachment() {
        setToDefaultValue();
    }

    public void setToDefaultValue() {
        this.valueMap.clear();
        this.panicNecklace = false;
        this.remainLavaImmuneTicks = 0;
    }

    public <T, V extends PrimitiveValue<T>> T getValue(ValueType<T, V> type) {
        PrimitiveValue<?> value = valueMap.get(type);
        return value == null ? type.defaultValue() : (T) value.get();
    }

    public <T, V extends PrimitiveValue<T>> List<String> getDescription(ValueType<T, V> type) {
        PrimitiveValue<?> value = valueMap.get(type);
        return value == null ? List.of("NONE") : value.getDescription();
    }

    public <T, V extends PrimitiveValue<T>> boolean contains(ValueType<T, V> type) {
        return valueMap.containsKey(type);
    }

    public boolean hasPanicNecklace() {
        return panicNecklace;
    }

    public void increaseLavaImmuneTicks() {
        if (remainLavaImmuneTicks < getValue(ValueType.LAVA$IMMUNE$TICKS)) {
            this.remainLavaImmuneTicks++;
        }
    }

    public boolean decreaseLavaImmuneTicks() {
        if (remainLavaImmuneTicks > 0) {
            this.remainLavaImmuneTicks--;
            return true;
        }
        return false;
    }

    public void flushAbility(LivingEntity living) {
        setToDefaultValue();
        CuriosApi.getCuriosInventory(living).ifPresent(handler -> {
            for (ICurioStacksHandler curioStacksHandler : handler.getCurios().values()) {
                IDynamicStackHandler stackHandler = curioStacksHandler.getStacks();
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);
                    if (stack.isEmpty()) continue;
                    AccessoriesComponent component = TCUtils.getAccessoriesComponent(stack);
                    if (component == null) continue;
                    Item item = stack.getItem();

                    for (Map.Entry<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> entry : component.types().entrySet()) {
                        putUnitIfPresent(entry.getKey());
                        combineValue(entry.getKey(), tryCast(entry.getValue()));
                    }

                    if (!panicNecklace && item instanceof PanicNecklace) this.panicNecklace = true;
                }
            }
            Set<EntityType<?>> ignores = getValue(ValueType.MOB$IGNORE);
            if (!ignores.isEmpty()) {
                living.level().getEntities(new MobEntityTypesTest(ignores), new AABB(living.getOnPos()).inflate(31.5), mob -> true).forEach(mob -> {
                    if (mob.getTarget() == living) mob.setTarget(null);
                });
            }
        });
    }

    private <T, V extends PrimitiveValue<T>> void putUnitIfPresent(ValueType<T, V> type) {
        if (UNITS_REQUIRE_UPDATE.contains(type)) {
            valueMap.put(type, UnitValue.INSTANCE);
        }
    }

    private <T, V extends PrimitiveValue<T>> void combineValue(ValueType<T, V> type, V value) {
        if (OTHER_REQUIRE_UPDATE.contains(type)) {
            V other = (V) valueMap.get(type);
            if (other == null) {
                valueMap.put(type, value);
            } else {
                T t = value.combine(other, type.combineRule());
                valueMap.put(type, type.newInstance(t));
            }
        }
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        ListTag listTag = new ListTag();
        for (Map.Entry<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> entry : valueMap.entrySet()) {
            PrimitiveValue<?> value = entry.getValue();
            value.codec().encodeStart(NbtOps.INSTANCE, tryCast(value)).result().ifPresent(tag -> {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.put(entry.getKey().key().toString(), tag);
                listTag.add(compoundTag);
            });
        }
        nbt.put("valueMap", listTag);
        nbt.putBoolean("panicNecklace", panicNecklace);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        this.valueMap.clear();
        ListTag listTag = nbt.getList("valueMap", Tag.TAG_COMPOUND);
        for (Tag tag : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag;
            String key = compoundTag.getAllKeys().stream().findFirst().get();
            ResourceLocation location = ResourceLocation.parse(key);
            ValueType.VALUE_CODECS.get(location).parse(NbtOps.INSTANCE, compoundTag.get(key)).result().ifPresent(value -> {
                valueMap.put(ValueType.TYPES.get(location), value);
            });
        }
        this.panicNecklace = nbt.getBoolean("panicNecklace");
    }
}
