package org.confluence.mod.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;

import java.util.Hashtable;
import java.util.Map;

public abstract class CombineRule<T, V extends PrimitiveValue<T>> {
    public static final Map<String, CombineRule<?, ?>> RULES = new Hashtable<>();

    public static final CombineRule<Unit, UnitValue> UNIT_GET_SELF = register(new CombineRule<>() {
        @Override
        public Unit combine(PrimitiveValue<Unit> componentA, PrimitiveValue<Unit> componentB) {
            return componentA.get();
        }

        @Override
        public String name() {
            return "unit_get_self";
        }
    });
    public static final CombineRule<EntityType<?>, EntityTypeValue> ENTITY_TYPE_GET_SELF = register(new CombineRule<>() {
        @Override
        public EntityType<?> combine(PrimitiveValue<EntityType<?>> componentA, PrimitiveValue<EntityType<?>> componentB) {
            return componentA.get();
        }

        @Override
        public String name() {
            return "entity_type_get_self";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_GET_SELF = register(new CombineRule<>() {
        @Override
        public Integer combine(PrimitiveValue<Integer> componentA, PrimitiveValue<Integer> componentB) {
            return componentA.get();
        }

        @Override
        public String name() {
            return "integer_get_self";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_ADDITION = register(new CombineRule<>() {
        @Override
        public Integer combine(PrimitiveValue<Integer> componentA, PrimitiveValue<Integer> componentB) {
            return componentA.get() + componentB.get();
        }

        @Override
        public String name() {
            return "integer_addition";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_SUBSTRACTION = register(new CombineRule<>() {
        @Override
        public Integer combine(PrimitiveValue<Integer> componentA, PrimitiveValue<Integer> componentB) {
            return componentA.get() - componentB.get();
        }

        @Override
        public String name() {
            return "integer_substraction";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_MULTIPLICATION = register(new CombineRule<>() {
        @Override
        public Integer combine(PrimitiveValue<Integer> componentA, PrimitiveValue<Integer> componentB) {
            return componentA.get() * componentB.get();
        }

        @Override
        public String name() {
            return "integer_multiplication";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_DIVISION = register(new CombineRule<>() {
        @Override
        public Integer combine(PrimitiveValue<Integer> componentA, PrimitiveValue<Integer> componentB) {
            return componentA.get() * componentB.get();
        }

        @Override
        public String name() {
            return "integer_multiply";
        }
    });
    public static final CombineRule<Float, FloatValue> FLOAT_GET_SELF = register(new CombineRule<>() {
        @Override
        public Float combine(PrimitiveValue<Float> componentA, PrimitiveValue<Float> componentB) {
            return componentA.get();
        }

        @Override
        public String name() {
            return "float_get_self";
        }
    });
    public static final CombineRule<Float, FloatValue> FLOAT_ADDITION = register(new CombineRule<>() {
        @Override
        public Float combine(PrimitiveValue<Float> componentA, PrimitiveValue<Float> componentB) {
            return componentA.get() + componentB.get();
        }

        @Override
        public String name() {
            return "float_addition";
        }
    });

    public static final Codec<CombineRule<?, ?>> CODEC = Codec.STRING.xmap(RULES::get, CombineRule::name);

    public abstract T combine(PrimitiveValue<T> componentA, PrimitiveValue<T> componentB);

    public abstract String name();

    public static <T, V extends PrimitiveValue<T>> CombineRule<T, V> register(CombineRule<T, V> combineRule) {
        RULES.put(combineRule.name(), combineRule);
        return combineRule;
    }
}
