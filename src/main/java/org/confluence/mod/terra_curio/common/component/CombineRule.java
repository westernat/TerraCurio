package org.confluence.mod.terra_curio.common.component;

import com.mojang.serialization.Codec;
import net.minecraft.Util;

import java.util.Hashtable;
import java.util.Optional;

public abstract class CombineRule<T> {
    public static final CombineRule<Integer> INTEGER_ADDITION = new CombineRule<>() {
        @Override
        public Integer combine(PrimitiveComponent<Integer> componentA, PrimitiveComponent<Integer> componentB, Integer defaultValue) {
            Optional<Integer> a = componentA.aInteger();
            Optional<Integer> b = componentB.aInteger();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() + b.get();
        }

        @Override
        public String name() {
            return "integer_addition";
        }
    };
    public static final CombineRule<Integer> INTEGER_SUBSTRACTION = new CombineRule<>() {
        @Override
        public Integer combine(PrimitiveComponent<Integer> componentA, PrimitiveComponent<Integer> componentB, Integer defaultValue) {
            Optional<Integer> a = componentA.aInteger();
            Optional<Integer> b = componentB.aInteger();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() - b.get();
        }

        @Override
        public String name() {
            return "integer_substraction";
        }
    };
    public static final CombineRule<Integer> INTEGER_MULTIPLICATION = new CombineRule<>() {
        @Override
        public Integer combine(PrimitiveComponent<Integer> componentA, PrimitiveComponent<Integer> componentB, Integer defaultValue) {
            Optional<Integer> a = componentA.aInteger();
            Optional<Integer> b = componentB.aInteger();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() * b.get();
        }

        @Override
        public String name() {
            return "integer_multiplication";
        }
    };
    public static final CombineRule<Integer> INTEGER_DIVISION = new CombineRule<>() {
        @Override
        public Integer combine(PrimitiveComponent<Integer> componentA, PrimitiveComponent<Integer> componentB, Integer defaultValue) {
            Optional<Integer> a = componentA.aInteger();
            Optional<Integer> b = componentB.aInteger();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() / b.get();
        }

        @Override
        public String name() {
            return "integer_division";
        }
    };

    public static final CombineRule<Float> FLOAT_ADDITION = new CombineRule<>() {
        @Override
        public Float combine(PrimitiveComponent<Float> componentA, PrimitiveComponent<Float> componentB, Float defaultValue) {
            Optional<Float> a = componentA.aFloat();
            Optional<Float> b = componentB.aFloat();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() + b.get();
        }

        @Override
        public String name() {
            return "integer_addition";
        }
    };
    public static final CombineRule<Float> FLOAT_SUBSTRACTION = new CombineRule<>() {
        @Override
        public Float combine(PrimitiveComponent<Float> componentA, PrimitiveComponent<Float> componentB, Float defaultValue) {
            Optional<Float> a = componentA.aFloat();
            Optional<Float> b = componentB.aFloat();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() - b.get();
        }

        @Override
        public String name() {
            return "float_substraction";
        }
    };
    public static final CombineRule<Float> FLOAT_MULTIPLICATION = new CombineRule<>() {
        @Override
        public Float combine(PrimitiveComponent<Float> componentA, PrimitiveComponent<Float> componentB, Float defaultValue) {
            Optional<Float> a = componentA.aFloat();
            Optional<Float> b = componentB.aFloat();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() * b.get();
        }

        @Override
        public String name() {
            return "float_multiplication";
        }
    };
    public static final CombineRule<Float> FLOAT_DIVISION = new CombineRule<>() {
        @Override
        public Float combine(PrimitiveComponent<Float> componentA, PrimitiveComponent<Float> componentB, Float defaultValue) {
            Optional<Float> a = componentA.aFloat();
            Optional<Float> b = componentB.aFloat();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() / b.get();
        }

        @Override
        public String name() {
            return "float_division";
        }
    };

    public static final CombineRule<Boolean> BOOLEAN_AND = new CombineRule<>() {
        @Override
        public Boolean combine(PrimitiveComponent<Boolean> componentA, PrimitiveComponent<Boolean> componentB, Boolean defaultValue) {
            Optional<Boolean> a = componentA.aBoolean();
            Optional<Boolean> b = componentB.aBoolean();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() && b.get();
        }

        @Override
        public String name() {
            return "boolean_and";
        }
    };
    public static final CombineRule<Boolean> BOOLEAN_OR = new CombineRule<>() {
        @Override
        public Boolean combine(PrimitiveComponent<Boolean> componentA, PrimitiveComponent<Boolean> componentB, Boolean defaultValue) {
            Optional<Boolean> a = componentA.aBoolean();
            Optional<Boolean> b = componentB.aBoolean();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() || b.get();
        }

        @Override
        public String name() {
            return "boolean_or";
        }
    };
    public static final CombineRule<Boolean> BOOLEAN_XOR = new CombineRule<>() {
        @Override
        public Boolean combine(PrimitiveComponent<Boolean> componentA, PrimitiveComponent<Boolean> componentB, Boolean defaultValue) {
            Optional<Boolean> a = componentA.aBoolean();
            Optional<Boolean> b = componentB.aBoolean();
            if (a.isEmpty() || b.isEmpty()) return defaultValue;
            return a.get() ^ b.get();
        }

        @Override
        public String name() {
            return "boolean_xor";
        }
    };

    public static final Hashtable<String, CombineRule<?>> RULES = Util.make(new Hashtable<>(), table -> {
        table.put(INTEGER_ADDITION.name(), INTEGER_ADDITION);
        table.put(INTEGER_SUBSTRACTION.name(), INTEGER_SUBSTRACTION);
        table.put(INTEGER_MULTIPLICATION.name(), INTEGER_MULTIPLICATION);
        table.put(INTEGER_SUBSTRACTION.name(), INTEGER_SUBSTRACTION);
        table.put(FLOAT_ADDITION.name(), FLOAT_ADDITION);
        table.put(FLOAT_SUBSTRACTION.name(), FLOAT_SUBSTRACTION);
        table.put(FLOAT_MULTIPLICATION.name(), FLOAT_MULTIPLICATION);
        table.put(FLOAT_DIVISION.name(), FLOAT_DIVISION);
        table.put(BOOLEAN_AND.name(), BOOLEAN_AND);
        table.put(BOOLEAN_OR.name(), BOOLEAN_OR);
        table.put(BOOLEAN_XOR.name(), BOOLEAN_XOR);
    });

    public static final Codec<CombineRule<?>> CODEC = Codec.STRING.xmap(RULES::get, CombineRule::name);

    public abstract T combine(PrimitiveComponent<T> componentA, PrimitiveComponent<T> componentB, T defaultValue);

    public abstract String name();

    @Override
    public String toString() {
        return "CombineRule{" + name() + '}';
    }
}
