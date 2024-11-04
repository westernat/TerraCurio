package org.confluence.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public abstract class CombineRule<T, V extends PrimitiveValue<T>> {
    public static final Map<String, CombineRule<?, ?>> RULES = new Hashtable<>();

    public static final CombineRule<Unit, UnitValue> UNIT_GET_SELF = register(new CombineRule<>() {
        @Override
        public Unit combine(Unit componentA, Unit componentB) {
            return componentA;
        }

        @Override
        public String name() {
            return "unit_get_self";
        }
    });
    public static final CombineRule<List<EntityType<?>>, EntityTypesValue> ENTITY_TYPES_GET_SELF = register(new CombineRule<>() {
        @Override
        public List<EntityType<?>> combine(List<EntityType<?>> componentA, List<EntityType<?>> componentB) {
            return componentA;
        }

        @Override
        public String name() {
            return "entity_types_get_self";
        }
    });
    public static final CombineRule<List<EntityType<?>>, EntityTypesValue> ENTITY_TYPES_EXPANSION = register(new CombineRule<>() {
        @Override
        public List<EntityType<?>> combine(List<EntityType<?>> componentA, List<EntityType<?>> componentB) {
            List<EntityType<?>> combined = new ArrayList<>(componentA);
            combined.addAll(componentB);
            return combined;
        }

        @Override
        public String name() {
            return "entity_types_expansion";
        }
    });
    public static final CombineRule<List<TagKey<Fluid>>, FluidTagsValue> FLUID_TAGS_EXPANSION = register(new CombineRule<>() {
        @Override
        public List<TagKey<Fluid>> combine(List<TagKey<Fluid>> componentA, List<TagKey<Fluid>> componentB) {
            List<TagKey<Fluid>> combined = new ArrayList<>(componentA);
            combined.addAll(componentB);
            return combined;
        }

        @Override
        public String name() {
            return "fluid_tag_expansion";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_GET_SELF = register(new CombineRule<>() {
        @Override
        public Integer combine(Integer componentA, Integer componentB) {
            return componentA;
        }

        @Override
        public String name() {
            return "integer_get_self";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_GET_MAX = register(new CombineRule<>() {
        @Override
        public Integer combine(Integer componentA, Integer componentB) {
            return Math.max(componentA, componentB);
        }

        @Override
        public String name() {
            return "integer_get_max";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_ADDITION = register(new CombineRule<>() {
        @Override
        public Integer combine(Integer componentA, Integer componentB) {
            return componentA + componentB;
        }

        @Override
        public String name() {
            return "integer_addition";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_SUBSTRACTION = register(new CombineRule<>() {
        @Override
        public Integer combine(Integer componentA, Integer componentB) {
            return componentA - componentB;
        }

        @Override
        public String name() {
            return "integer_substraction";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_MULTIPLICATION = register(new CombineRule<>() {
        @Override
        public Integer combine(Integer componentA, Integer componentB) {
            return componentA * componentB;
        }

        @Override
        public String name() {
            return "integer_multiplication";
        }
    });
    public static final CombineRule<Integer, IntegerValue> INTEGER_DIVISION = register(new CombineRule<>() {
        @Override
        public Integer combine(Integer componentA, Integer componentB) {
            return componentA * componentB;
        }

        @Override
        public String name() {
            return "integer_multiply";
        }
    });
    public static final CombineRule<Float, FloatValue> FLOAT_GET_SELF = register(new CombineRule<>() {
        @Override
        public Float combine(Float componentA, Float componentB) {
            return componentA;
        }

        @Override
        public String name() {
            return "float_get_self";
        }
    });
    public static final CombineRule<Float, FloatValue> FLOAT_GET_MAX = register(new CombineRule<>() {
        @Override
        public Float combine(Float componentA, Float componentB) {
            return Math.max(componentA, componentB);
        }

        @Override
        public String name() {
            return "float_get_max";
        }
    });
    public static final CombineRule<Float, FloatValue> FLOAT_ADDITION = register(new CombineRule<>() {
        @Override
        public Float combine(Float componentA, Float componentB) {
            return componentA + componentB;
        }

        @Override
        public String name() {
            return "float_addition";
        }
    });

    public static final Codec<CombineRule<?, ?>> CODEC = Codec.STRING.xmap(RULES::get, CombineRule::name);

    public abstract T combine(T componentA, T componentB);

    public T combineValue(V valueA, V valueB) {
        return combine(valueA.get(), valueB.get());
    }

    public T combineFromValue(V valueA, T componentB) {
        return combine(valueA.get(), componentB);
    }

    public T combineWithValue(T componentA, V valueB) {
        return combine(componentA, valueB.get());
    }

    public abstract String name();

    public static <T, V extends PrimitiveValue<T>> CombineRule<T, V> register(CombineRule<T, V> combineRule) {
        RULES.put(combineRule.name(), combineRule);
        return combineRule;
    }
}
