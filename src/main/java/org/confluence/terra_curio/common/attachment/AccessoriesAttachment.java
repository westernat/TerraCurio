package org.confluence.terra_curio.common.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.ModLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_curio.api.event.AfterAccessoryAbilitiesFlushedEvent;
import org.confluence.terra_curio.api.event.RegisterAccessoriesComponentUpdateEvent;
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.UnitValue;
import org.confluence.terra_curio.api.primitive.ValueType;
import org.confluence.terra_curio.common.component.AccessoriesComponent;
import org.confluence.terra_curio.common.init.TCItems;
import org.confluence.terra_curio.common.init.TCTags;
import org.confluence.terra_curio.common.item.curio.combat.PanicNecklace;
import org.confluence.terra_curio.util.TCUtils;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;

import static org.confluence.terra_curio.util.TCUtils.tryCast;

@javax.annotation.ParametersAreNonnullByDefault
@net.minecraft.MethodsReturnNonnullByDefault
@SuppressWarnings("unchecked")
public class AccessoriesAttachment implements INBTSerializable<CompoundTag> {
    public static final Set<ValueType<Unit, UnitValue>> UNITS_REQUIRE_UPDATE = Util.make(new LinkedHashSet<>(), list -> {
        list.add(TCItems.FLOAT$ON$LIQUID$SURFACE);
        list.add(TCItems.FIRE$ATTACK);
        list.add(TCItems.BRAIN$OF$CONFUSION);
        list.add(TCItems.HIVE$PACK);
        list.add(TCItems.HONEY$COMB);
        list.add(TCItems.MAGIC$QUIVER);
        list.add(TCItems.IGNITE$ARROW);
        list.add(TCItems.FROZEN$TURTLE$SHELL);
        list.add(TCItems.FIRE$IMMUNE);
        list.add(TCItems.FLOWER$BOOTS);
        list.add(TCItems.FROZEN$IMMUNE);
        list.add(TCItems.ICE$SPEED);
        list.add(TCItems.DIVING);
        list.add(TCItems.INFINITE$FLIGHT);
        list.add(TCItems.ICE$SAFE);
        list.add(TCItems.SHIELD$OF$CTHULHU);
        ModLoader.postEvent(new RegisterAccessoriesComponentUpdateEvent.UnitType(list));
    });
    public static final Set<ValueType<?, ? extends PrimitiveValue<?>>> OTHER_REQUIRE_UPDATE = Util.make(new LinkedHashSet<>(), list -> {
        list.add(TCItems.NEPTUNES$SHELL);
        list.add(TCItems.STAR$CLOCK);
        list.add(TCItems.INJURY$FREE);
        list.add(TCItems.INVULNERABLE$TICKS$MULTIPLIER);
        list.add(TCItems.LAVA$HURT$REDUCE);
        list.add(TCItems.LAVA$IMMUNE$TICKS);
        list.add(TCItems.RIGHT$CLICK$DELAY$SUBSTRACTOR);
        list.add(TCItems.MOB$IGNORE);
        list.add(TCItems.WALL$CLIMB);
        list.add(TCItems.FART);
        list.add(TCItems.SAND$STORM);
        list.add(TCItems.BLIZZARD);
        list.add(TCItems.TSUNAMI);
        list.add(TCItems.CLOUD);
        list.add(TCItems.MAY$FLY);
        list.add(TCItems.EFFECT$IMMUNITIES);
        list.add(TCItems.TOTEM$WITH$COOLDOWN);
        list.add(TCItems.LUMINANCE);
        ModLoader.postEvent(new RegisterAccessoriesComponentUpdateEvent.OtherType(list));
    });
    private final Map<ValueType<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> valueMap = new HashMap<>();
    private boolean panicNecklace;
    private transient int remainLavaImmuneTicks;
    private transient int totalLavaImmuneTicks;

    public AccessoriesAttachment() {
        setToDefaultValue();
    }

    public void setToDefaultValue() {
        this.valueMap.clear();
        this.panicNecklace = false;
        this.remainLavaImmuneTicks = 0;
        this.totalLavaImmuneTicks = 0;
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
        if (remainLavaImmuneTicks < totalLavaImmuneTicks) {
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
                    LibUtils.forMixin$Inject();
                }
            }
            TagKey<EntityType<?>> ignores = getValue(TCItems.MOB$IGNORE);
            if (!TCTags.NOTHING.equals(ignores)) {
                living.level().getEntitiesOfClass(Mob.class, new AABB(living.getOnPos()).inflate(31.5), mob -> mob.getType().is(ignores)).forEach(mob -> {
                    if (mob.getTarget() == living) mob.setTarget(null);
                });
            }
            this.totalLavaImmuneTicks = getValue(TCItems.LAVA$IMMUNE$TICKS);
        });
        NeoForge.EVENT_BUS.post(new AfterAccessoryAbilitiesFlushedEvent(living));
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
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
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
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.valueMap.clear();
        ListTag listTag = nbt.getList("valueMap", Tag.TAG_COMPOUND);
        for (Tag tag : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag;
            String key = compoundTag.getAllKeys().stream().findFirst().orElse(null);
            if (key == null) continue;
            ValueType<?, ? extends PrimitiveValue<?>> type = ValueType.TYPES.get(ResourceLocation.tryParse(key));
            if (type == null) continue;
            Codec<PrimitiveValue<?>> codec = ValueType.VALUE_CODECS.get(type);
            if (codec == null) continue;
            RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
            codec.parse(ops, compoundTag.get(key)).result().ifPresent(value -> valueMap.put(type, value));
        }
        this.panicNecklace = nbt.getBoolean("panicNecklace");
    }
}
