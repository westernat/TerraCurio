package org.confluence.terra_curio.common.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import org.confluence.terra_curio.TerraCurio;
import org.confluence.terra_curio.common.component.primitive.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Hashtable;
import java.util.Map;

@SuppressWarnings("unchecked")
public record AccessoriesComponent(Map<Type<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> types) implements DataComponentType<AccessoriesComponent> {
    public static final Map<ResourceLocation, Codec<PrimitiveValue<?>>> CODECS = new Hashtable<>();
    public static final Map<ResourceLocation, Type<?, ? extends PrimitiveValue<?>>> ENTRIES = new Hashtable<>();

    public static final Type<Unit, UnitValue> FULL_INFORMATION = ofUnit("full_information"),
            HOUR_WATCH = ofUnit("hour_watch"),
            HALF_HOUR_WATCH = ofUnit("half_hour_watch"),
            MINUTE_WATCH = ofUnit("minute_watch"),
            WEATHER_RADIO = ofUnit("weather_radio"),
            SEXTANT = ofUnit("sextant"),
            FISHERMANS_POCKET_GUIDE = ofUnit("fishermans_pocket_guide"),
            METAL_DETECTOR = ofUnit("metal_detector"),
            LIFE_FORM_ANALYZER = ofUnit("life_form_analyzer"),
            RADAR = ofUnit("radar"),
            TALLY_COUNTER = ofUnit("tally_counter"),
            DPS_METER = ofUnit("dps_meter"),
            STOPWATCH = ofUnit("stopwatch"),
            COMPASS = ofUnit("compass"),
            DEPTH_METER = ofUnit("depth_meter"),

    AUTO_ATTACK = ofUnit("auto_attack"),
            CTHULHU = ofUnit("shield_of_cthulhu"), // todo
            TABI = ofUnit("tabi"), // todo
            SCOPE = ofUnit("scope"), // todo
            GRAVITY = ofUnit("gravity_globe"), // todo
            FIRE_ATTACK = ofUnit("fire_attack"),
            BRAIN = ofUnit("brain_of_confusion"), // todo
            HIVE = ofUnit("hive_pack"); // todo
    public static final Type<Integer, IntegerValue> STOOL = ofInteger("stool", CombineRule.INTEGER_ADDITION); // todo
    public static final Type<Float, FloatValue> FISHING_POWER = ofFloat("fishing_power", CombineRule.FLOAT_ADDITION), // todo
            INJURY_FREE = ofFloat("injury_free", CombineRule.FLOAT_ADDITION); // todo
    public static final Type<EntityType<?>, EntityTypeValue> LIVING_IGNORE = ofEntityType("living_ignore"); // todo

    public static final Codec<AccessoriesComponent> CODEC = Codec.dispatchedMap(ResourceLocation.CODEC, CODECS::get)
            .xmap(map -> {
                Map<Type<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> table = new Hashtable<>();
                map.forEach((key, value) -> table.put(ENTRIES.get(key), value));
                return new AccessoriesComponent(table);
            }, component -> {
                Map<ResourceLocation, PrimitiveValue<?>> table = new Hashtable<>();
                component.types.forEach((type, value) -> table.put(type.key, value));
                return table;
            });
    public static final StreamCodec<FriendlyByteBuf, AccessoriesComponent> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(FriendlyByteBuf buffer, @NotNull AccessoriesComponent value) {
            buffer.writeJsonWithCodec(CODEC, value);
        }

        @Override
        @NotNull
        public AccessoriesComponent decode(FriendlyByteBuf buffer) {
            return buffer.readJsonWithCodec(CODEC);
        }
    };

    public static <T, V extends PrimitiveValue<T>> AccessoriesComponent of(Type<T, V> type, V value) {
        return new AccessoriesComponent(Map.of(type, value));
    }

    public static AccessoriesComponent units(Type<Unit, UnitValue>... entries) {
        Hashtable<Type<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> table = new Hashtable<>();
        for (Type<Unit, UnitValue> type : entries) {
            table.put(type, UnitValue.INSTANCE);
        }
        return new AccessoriesComponent(table);
    }

    public <T, V extends PrimitiveValue<T>> boolean contains(Type<T, V> type) {
        return types.containsKey(type);
    }

    public <T, V extends PrimitiveValue<T>> @Nullable V get(Type<T, V> type) {
        return (V) types.get(type);
    }

    public <T, V extends PrimitiveValue<T>> void put(Type<T, V> type, V value) {
        if (value instanceof PrimitiveValue<?> value1) {
            types.put(type, value1);
        }
    }

    @Override
    public @Nullable Codec<AccessoriesComponent> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<FriendlyByteBuf, AccessoriesComponent> streamCodec() {
        return STREAM_CODEC;
    }

    private static <T, V extends PrimitiveValue<T>> void registerCodec(ResourceLocation id, Codec<V> codec) {
        if (codec instanceof Codec<? extends PrimitiveValue<?>> codec1) {
            CODECS.put(id, (Codec<PrimitiveValue<?>>) codec1);
        }
    }

    public static <T, V extends PrimitiveValue<T>> Type<T, V> create(String path, CombineRule<T, V> combineRule, Codec<V> codec) {
        ResourceLocation id = TerraCurio.asResource(path);
        registerCodec(id, codec);
        Type<T, V> type = new Type<>(id, combineRule);
        ENTRIES.put(id, type);
        return type;
    }

    public static Type<Unit, UnitValue> ofUnit(String path) {
        ResourceLocation id = TerraCurio.asResource(path);
        registerCodec(id, UnitValue.CODEC);
        Type<Unit, UnitValue> type = new Type<>(id, CombineRule.UNIT_GET_SELF);
        ENTRIES.put(id, type);
        return type;
    }

    public static Type<Integer, IntegerValue> ofInteger(String path, CombineRule<Integer, IntegerValue> combineRule) {
        ResourceLocation id = TerraCurio.asResource(path);
        registerCodec(id, IntegerValue.CODEC);
        Type<Integer, IntegerValue> type = new Type<>(id, combineRule);
        ENTRIES.put(id, type);
        return type;
    }

    public static Type<Float, FloatValue> ofFloat(String path, CombineRule<Float, FloatValue> combineRule) {
        ResourceLocation id = TerraCurio.asResource(path);
        registerCodec(id, FloatValue.CODEC);
        Type<Float, FloatValue> type = new Type<>(id, combineRule);
        ENTRIES.put(id, type);
        return type;
    }

    public static Type<EntityType<?>, EntityTypeValue> ofEntityType(String path) {
        ResourceLocation id = TerraCurio.asResource(path);
        registerCodec(id, EntityTypeValue.CODEC);
        Type<EntityType<?>, EntityTypeValue> type = new Type<>(id, CombineRule.ENTITY_TYPE_GET_SELF);
        ENTRIES.put(id, type);
        return type;
    }

    public static class Type<T, V extends PrimitiveValue<T>> {
        private final ResourceLocation key;
        private final CombineRule<T, V> combineRule;

        public Type(ResourceLocation key, CombineRule<T, V> combineRule) {
            this.key = key;
            this.combineRule = combineRule;
        }

        public ResourceLocation key() {
            return key;
        }

        public CombineRule<T, V> rule() {
            return combineRule;
        }

        @Override
        public String toString() {
            return "Entry[" +
                    "key=" + key + ", " +
                    "combineRule=" + combineRule + ']';
        }
    }
}
