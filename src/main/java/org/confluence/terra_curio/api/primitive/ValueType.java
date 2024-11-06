package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import org.confluence.terra_curio.TerraCurio;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ValueType<T, V extends PrimitiveValue<T>> {
    public static final Map<ResourceLocation, Codec<PrimitiveValue<?>>> CODECS = new Hashtable<>();
    public static final Map<ResourceLocation, ValueType<?, ? extends PrimitiveValue<?>>> TYPES = new Hashtable<>();
    // client side info_check
    public static final ValueType<Unit, UnitValue> FULL$INFORMATION = ofUnit("full_information");
    public static final ValueType<Unit, UnitValue> HOUR$WATCH = ofUnit("hour_watch");
    public static final ValueType<Unit, UnitValue> HALF$HOUR$WATCH = ofUnit("half_hour_watch");
    public static final ValueType<Unit, UnitValue> MINUTE$WATCH = ofUnit("minute_watch");
    public static final ValueType<Unit, UnitValue> WEATHER$RADIO = ofUnit("weather_radio");
    public static final ValueType<Unit, UnitValue> SEXTANT = ofUnit("sextant");
    public static final ValueType<Unit, UnitValue> FISHERMANS$POCKET$GUIDE = ofUnit("fishermans_pocket_guide");
    public static final ValueType<Unit, UnitValue> METAL$DETECTOR = ofUnit("metal_detector");
    public static final ValueType<Unit, UnitValue> LIFE$FORM$ANALYZER = ofUnit("life_form_analyzer");
    public static final ValueType<Unit, UnitValue> RADAR = ofUnit("radar");
    public static final ValueType<Unit, UnitValue> TALLY$COUNTER = ofUnit("tally_counter");
    public static final ValueType<Unit, UnitValue> DPS$METER = ofUnit("dps_meter");
    public static final ValueType<Unit, UnitValue> STOPWATCH = ofUnit("stopwatch");
    public static final ValueType<Unit, UnitValue> COMPASS = ofUnit("compass");
    public static final ValueType<Unit, UnitValue> DEPTH$METER = ofUnit("depth_meter");
    // client side curio_exits
    public static final ValueType<Unit, UnitValue> AUTO$ATTACK = ofUnit("auto_attack");
    public static final ValueType<Unit, UnitValue> SHIELD$OF$CTHULHU = ofUnit("shield_of_cthulhu");
    public static final ValueType<Unit, UnitValue> SPRINTING = ofUnit("sprinting");
    public static final ValueType<Unit, UnitValue> SCOPE = ofUnit("scope");
    public static final ValueType<Unit, UnitValue> GRAVITY$GLOBE = ofUnit("gravity_globe");
    public static final ValueType<Unit, UnitValue> MAGILUMINESCENCE = ofUnit("magiluminescence");
    // require updates
    public static final ValueType<Unit, UnitValue> FIRE$ATTACK = ofUnit("fire_attack");
    public static final ValueType<Unit, UnitValue> BRAIN$OF$CONFUSION = ofUnit("brain_of_confusion");
    public static final ValueType<Unit, UnitValue> HIVE$PACK = ofUnit("hive_pack");
    public static final ValueType<Unit, UnitValue> STAR$CLOCK = ofUnit("star_clock");
    public static final ValueType<Unit, UnitValue> HONEY$COMB = ofUnit("honey_comb");
    public static final ValueType<Unit, UnitValue> MAGIC$QUIVER = ofUnit("magic_quiver");
    public static final ValueType<Unit, UnitValue> IGNITE$ARROW = ofUnit("ignite_arrow");
    public static final ValueType<Unit, UnitValue> FROZEN$TURTLE$SHELL = ofUnit("frozen_turtle_shell");
    public static final ValueType<Unit, UnitValue> FIRE$IMMUNE = ofUnit("fire_immune");
    public static final ValueType<Unit, UnitValue> FLOWER$BOOTS = ofUnit("flower_boots");
    public static final ValueType<Unit, UnitValue> FROZEN$IMMUNE = ofUnit("frozen_immune");
    public static final ValueType<Unit, UnitValue> ICE$SPEED = ofUnit("ice_speed");

    public static final ValueType<Float, FloatValue> FISHING$POWER = ofFloat("fishing_power", FloatValue.ADDITION, 0.0F); // todo
    public static final ValueType<Float, FloatValue> INJURY$FREE = ofFloat("injury_free", FloatValue.ADDITION_WITHIN_0_TO_1, 0.0F);
    public static final ValueType<Float, FloatValue> INVULNERABLE$TICKS$MULTIPLIER = ofFloat("invulnerable_ticks_multiplier", FloatValue.GET_MAX, 1.0F);
    public static final ValueType<Float, FloatValue> LAVA$HURT$REDUCE = ofFloat("lava_hurt_reduce", FloatValue.GET_MAX_WITHIN_0_TO_1, 0.0F);
    public static final ValueType<Integer, IntegerValue> LAVA$IMMUNE$TICKS = ofInteger("lava_immune_ticks", IntegerValue.GET_MAX, 0);
    public static final ValueType<Integer, IntegerValue> RIGHT$CLICK$DELAY$SUBSTRACTOR = ofInteger("right_click_delay_substractor", IntegerValue.GET_MAX, 0);
    public static final ValueType<List<EntityType<?>>, EntityTypesValue> MOB$IGNORE = create("mob_ignore", EntityTypesValue.EXPANSION, EntityTypesValue.CODEC, List.of(), EntityTypesValue::new);
    public static final ValueType<List<TagKey<Fluid>>, FluidTagsValue> FLUID$WALK = create("fluid_walk", FluidTagsValue.EXPANSION, FluidTagsValue.CODEC, List.of(), FluidTagsValue::new);
    public static final ValueType<Byte, ByteValue> WALL$CLIMB = create("wall_climb", ByteValue.ADDITION_WITHIN_0_TO_2, ByteValue.CODEC, (byte) 0, ByteValue::new);
    public static final ValueType<Float, FloatValue> FART = ofFloat("fart", FloatValue.GET_SELF, 0.0F);
    public static final ValueType<Tuple<Float, Integer>, FloatAndIntegerValue> SAND$STORM = ofFloatAndInteger("sand_storm", FloatAndIntegerValue.GET_SELF, new Tuple<>(0.0F, 0));
    public static final ValueType<Tuple<Float, Integer>, FloatAndIntegerValue> BLIZZARD = ofFloatAndInteger("blizzard", FloatAndIntegerValue.GET_SELF, new Tuple<>(0.0F, 0));
    public static final ValueType<Float, FloatValue> TSUNAMI = ofFloat("tsunami", FloatValue.GET_SELF, 0.0F);
    public static final ValueType<Float, FloatValue> CLOUD = ofFloat("cloud", FloatValue.GET_SELF, 0.0F);
    public static final ValueType<Tuple<Float, Integer>, FloatAndIntegerValue> MAY$FLY = ofFloatAndInteger("may_fly", FloatAndIntegerValue.GET_SELF, new Tuple<>(0.0F, 0));

    private final ResourceLocation key;
    private final CombineRule<T, V> combineRule;
    private final T defaultValue;
    private final Function<T, V> factory;

    public ValueType(ResourceLocation key, CombineRule<T, V> combineRule, T defaultValue, Function<T, V> factory) {
        this.key = key;
        this.combineRule = combineRule;
        this.defaultValue = defaultValue;
        this.factory = factory;
    }

    private static <T, V extends PrimitiveValue<T>> void registerCodec(ResourceLocation id, Codec<V> codec) {
        if (codec instanceof Codec<? extends PrimitiveValue<?>> codec1) {
            CODECS.put(id, (Codec<PrimitiveValue<?>>) codec1);
        }
    }

    public static <T, V extends PrimitiveValue<T>> ValueType<T, V> create(String path, CombineRule<T, V> combineRule, Codec<V> codec, T defaultValue, Function<T, V> factory) {
        ResourceLocation id = TerraCurio.asResource(path);
        registerCodec(id, codec);
        ValueType<T, V> type = new ValueType<>(id, combineRule, defaultValue, factory);
        TYPES.put(id, type);
        return type;
    }

    public static ValueType<Unit, UnitValue> ofUnit(String path) {
        return create(path, UnitValue.GET_SELF, UnitValue.CODEC, Unit.INSTANCE, UnitValue.UNIT_2_VALUE);
    }

    public static ValueType<Integer, IntegerValue> ofInteger(String path, CombineRule<Integer, IntegerValue> combineRule, int defaultValue) {
        return create(path, combineRule, IntegerValue.CODEC, defaultValue, IntegerValue::new);
    }

    public static ValueType<Float, FloatValue> ofFloat(String path, CombineRule<Float, FloatValue> combineRule, float defaultValue) {
        return create(path, combineRule, FloatValue.CODEC, defaultValue, FloatValue::new);
    }

    public static ValueType<Tuple<Float, Integer>, FloatAndIntegerValue> ofFloatAndInteger(String path, CombineRule<Tuple<Float, Integer>, FloatAndIntegerValue> combineRule, Tuple<Float, Integer> defaultValue) {
        return create(path, combineRule, FloatAndIntegerValue.CODEC, defaultValue, FloatAndIntegerValue::new);
    }

    public ResourceLocation key() {
        return key;
    }

    public CombineRule<T, V> combineRule() {
        return combineRule;
    }

    public T defaultValue() {
        return defaultValue;
    }

    public V newInstance(T t) {
        return factory.apply(t);
    }

    @Override
    public String toString() {
        return "Type{" +
                "key=" + key +
                ", combineRule=" + combineRule +
                ", defaultValue=" + defaultValue +
                ", factory=" + factory +
                '}';
    }
}
