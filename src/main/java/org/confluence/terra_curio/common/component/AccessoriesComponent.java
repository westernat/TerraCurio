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
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public record AccessoriesComponent(Map<Type<?, ? extends PrimitiveValue<?>>, PrimitiveValue<?>> types) implements DataComponentType<AccessoriesComponent> {
    public static final Map<ResourceLocation, Codec<PrimitiveValue<?>>> CODECS = new Hashtable<>();
    public static final Map<ResourceLocation, Type<?, ? extends PrimitiveValue<?>>> ENTRIES = new Hashtable<>();

    public static final Type<Unit, UnitValue> FULL$INFORMATION = ofUnit("full_information"),
            HOUR$WATCH = ofUnit("hour_watch"),
            HALF$HOUR$WATCH = ofUnit("half_hour_watch"),
            MINUTE$WATCH = ofUnit("minute_watch"),
            WEATHER$RADIO = ofUnit("weather_radio"),
            SEXTANT = ofUnit("sextant"),
            FISHERMANS$POCKET$GUIDE = ofUnit("fishermans_pocket_guide"),
            METAL$DETECTOR = ofUnit("metal_detector"),
            LIFE$FORM$ANALYZER = ofUnit("life_form_analyzer"),
            RADAR = ofUnit("radar"),
            TALLY$COUNTER = ofUnit("tally_counter"),
            DPS$METER = ofUnit("dps_meter"),
            STOPWATCH = ofUnit("stopwatch"),
            COMPASS = ofUnit("compass"),
            DEPTH$METER = ofUnit("depth_meter"),

    AUTO_ATTACK = ofUnit("auto_attack"),
            SHIELD$OF$CTHULHU = ofUnit("shield_of_cthulhu"), // todo
            SPRINTING = ofUnit("sprinting"),
            SCOPE = ofUnit("scope"),
            GRAVITY$GLOBE = ofUnit("gravity_globe"), // todo
            FIRE$ATTACK = ofUnit("fire_attack"),
            BRAIN$OF$CONFUSION = ofUnit("brain_of_confusion"),
            HIVE$PACK = ofUnit("hive_pack"),
            STAR$CLOCK = ofUnit("star_clock"),
            HONEY$COMB = ofUnit("honey_comb"),
            MAGIC$QUIVER = ofUnit("magic_quiver"),
            IGNITE$ARROW = ofUnit("ignite_arrow"),
            FROZEN$TURTLE$SHELL = ofUnit("frozen_turtle_shell"),
            STEP$STOOL = ofUnit("step_stool"),
            OBSIDIAN$ROSE = ofUnit("obsidian_rose");
    public static final Type<Float, FloatValue> FISHING$POWER = ofFloat("fishing_power", CombineRule.FLOAT_ADDITION), // todo
            INJURY$FREE = ofFloat("injury_free", CombineRule.FLOAT_ADDITION),
            INVULNERABLE$TICKS$MULTIPLIER = ofFloat("invulnerable_ticks_multiplier", CombineRule.FLOAT_GET_MAX);
    public static final Type<List<EntityType<?>>, EntityTypesValue> MOB$IGNORE = ofEntityTypes("mob_ignore", CombineRule.ENTITY_TYPES_EXPANSION);

    public static final Codec<AccessoriesComponent> CODEC = Codec.dispatchedMap(ResourceLocation.CODEC, CODECS::get).xmap(map -> {
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

    public static Type<List<EntityType<?>>, EntityTypesValue> ofEntityTypes(String path, CombineRule<List<EntityType<?>>, EntityTypesValue> combineRule) {
        ResourceLocation id = TerraCurio.asResource(path);
        registerCodec(id, EntityTypesValue.CODEC);
        Type<List<EntityType<?>>, EntityTypesValue> type = new Type<>(id, combineRule);
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
