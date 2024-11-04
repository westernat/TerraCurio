package org.confluence.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.util.Unit;

import java.util.function.Function;

public class UnitValue implements PrimitiveValue<Unit> {
    public static final UnitValue INSTANCE = new UnitValue();
    public static final Function<Unit, UnitValue> UNIT_2_VALUE = unit -> UnitValue.INSTANCE;
    public static final Codec<UnitValue> CODEC = Unit.CODEC.xmap(UNIT_2_VALUE, unitValue -> Unit.INSTANCE);

    @Override
    public Unit get() {
        return Unit.INSTANCE;
    }

    @Override
    public Codec<UnitValue> codec() {
        return CODEC;
    }
}
