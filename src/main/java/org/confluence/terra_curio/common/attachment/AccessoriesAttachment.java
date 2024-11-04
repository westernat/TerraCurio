package org.confluence.terra_curio.common.attachment;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.confluence.terra_curio.api.event.FishingPowerModificationEvent;
import org.confluence.terra_curio.api.event.RegisterAccessoriesComponentUpdateEvent;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.component.primitive.FloatValue;
import org.confluence.terra_curio.common.component.primitive.PrimitiveValue;
import org.confluence.terra_curio.common.component.primitive.UnitValue;
import org.confluence.terra_curio.common.component.primitive.ValueType;
import org.confluence.terra_curio.common.init.TCDataComponentTypes;
import org.confluence.terra_curio.common.item.curio.combat.PanicNecklace;
import org.confluence.terra_curio.util.MobEntityTypesTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;

@SuppressWarnings("unchecked")
public class AccessoriesAttachment implements INBTSerializable<CompoundTag> {
    public static final List<ValueType<Unit, UnitValue>> UNITS_REQUIRE_UPDATE = Util.make(new ArrayList<>(), list -> {
        list.add(ValueType.FIRE$ATTACK);
        list.add(ValueType.BRAIN$OF$CONFUSION);
        list.add(ValueType.HIVE$PACK);
        list.add(ValueType.STAR$CLOCK);
        list.add(ValueType.HONEY$COMB);
        list.add(ValueType.MAGIC$QUIVER);
        list.add(ValueType.IGNITE$ARROW);
        list.add(ValueType.FROZEN$TURTLE$SHELL);
        ModLoader.postEvent(new RegisterAccessoriesComponentUpdateEvent.UnitType(list));
    });
    public static final List<ValueType<?, ? extends PrimitiveValue<?>>> OTHER_REQUIRE_UPDATE = Util.make(new ArrayList<>(), list -> {
        list.add(ValueType.FISHING$POWER);
        list.add(ValueType.INVULNERABLE$TICKS$MULTIPLIER);
        list.add(ValueType.INJURY$FREE);
        list.add(ValueType.MOB$IGNORE);
        list.add(ValueType.LAVA$IMMUNE$TICKS);
        list.add(ValueType.FLUID$WALK);
        ModLoader.postEvent(new RegisterAccessoriesComponentUpdateEvent.OtherType(list));
    });
    private final Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> valueMap = new HashMap<>();
    private final Set<EntityType<?>> ignores = new HashSet<>();
    private boolean panicNecklace;
    private transient int remainLavaImmuneTicks;

    public AccessoriesAttachment() {
        setToDefaultValue();
    }

    public void setToDefaultValue() {
        this.valueMap.clear();
        this.ignores.clear();
        this.panicNecklace = false;
    }

    public <T, V extends PrimitiveValue<T>> T getValue(ValueType<T, V> type) {
        PrimitiveValue<?> value = valueMap.get(type);
        return value == null ? type.defaultValue() : (T) value.get();
    }

    public <T, V extends PrimitiveValue<T>> boolean contains(ValueType<T, V> type) {
        return valueMap.containsKey(type);
    }

    public Set<EntityType<?>> getIgnores() {
        return ignores;
    }

    public boolean hasPanicNecklace() {
        return panicNecklace;
    }

    public void increaseLavaImmuneTicks() {
        if (remainLavaImmuneTicks < getValue(ValueType.LAVA$IMMUNE$TICKS)) {
            remainLavaImmuneTicks++;
        }
    }

    public boolean decreaseLavaImmuneTicks() {
        if (remainLavaImmuneTicks > 0) {
            remainLavaImmuneTicks--;
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
                    AccessoriesComponent component;
                    if (stack.isEmpty() || (component = stack.get(TCDataComponentTypes.ACCESSORIES)) == null) continue;
                    Item item = stack.getItem();

                    for (ValueType<Unit, UnitValue> type : UNITS_REQUIRE_UPDATE) {
                        putUnitIfPresent(component, type);
                    }
                    for (ValueType<?, ? extends PrimitiveValue<?>> type : OTHER_REQUIRE_UPDATE) {
                        combineValue(component, type);
                    }

                    if (!panicNecklace && item instanceof PanicNecklace) this.panicNecklace = true;
                }
            }
            float fishingPower = NeoForge.EVENT_BUS.post(new FishingPowerModificationEvent(living, getValue(ValueType.FISHING$POWER))).getNeoValue();
            valueMap.put(ValueType.FISHING$POWER, new FloatValue(fishingPower));
            List<EntityType<?>> ignores = getValue(ValueType.MOB$IGNORE);
            this.ignores.addAll(ignores);
            if (!ignores.isEmpty()) {
                living.level().getEntities(new MobEntityTypesTest(ignores), new AABB(living.getOnPos()).inflate(31.5), mob -> true).forEach(mob -> {
                    if (mob.getTarget() == living) mob.setTarget(null);
                });
            }
        });
    }

    private <T, V extends PrimitiveValue<T>> void putUnitIfPresent(AccessoriesComponent component, ValueType<T, V> type) {
        if (component.contains(type)) {
            valueMap.put(type, UnitValue.INSTANCE);
        }
    }

    private <T, V extends PrimitiveValue<T>> void combineValue(AccessoriesComponent component, ValueType<T, V> type) {
        V v = component.get(type);
        if (v != null) {
            if (!valueMap.containsKey(type)) {
                valueMap.put(type, v);
            }
            T t = v.combine((V) valueMap.get(type), type.combineRule());
            valueMap.put(type, type.newInstance(t));
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
        ListTag listTag1 = new ListTag();
        for (EntityType<?> ignore : ignores) {
            CompoundTag type = new CompoundTag();
            type.putString("EntityType", BuiltInRegistries.ENTITY_TYPE.getKey(ignore).toString());
            listTag1.add(type);
        }
        nbt.put("ignores", listTag1);
        nbt.putBoolean("panicNecklace", panicNecklace);
        return nbt;
    }

    private static <T, V extends PrimitiveValue<T>> V tryCast(PrimitiveValue<?> primitiveValue) {
        return (V) primitiveValue;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        this.valueMap.clear();
        ListTag listTag = nbt.getList("valueMap", Tag.TAG_COMPOUND);
        for (Tag tag : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag;
            String key = compoundTag.getAllKeys().stream().findFirst().get();
            ResourceLocation location = ResourceLocation.parse(key);
            ValueType.CODECS.get(location).decode(NbtOps.INSTANCE, compoundTag.get(key)).result().ifPresent(pair -> {
                valueMap.put(ValueType.ENTRIES.get(location), pair.getFirst());
            });
        }
        this.ignores.clear();
        ListTag listTag1 = nbt.getList("ignores", Tag.TAG_COMPOUND);
        for (Tag tag : listTag1) {
            String type = ((CompoundTag) tag).getString("EntityType");
            ignores.add(BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(type)));
        }
        this.panicNecklace = nbt.getBoolean("panicNecklace");
    }
}
