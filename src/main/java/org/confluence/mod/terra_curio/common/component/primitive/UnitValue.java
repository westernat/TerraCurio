package org.confluence.mod.terra_curio.common.component.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.util.Unit;

public class UnitValue implements PrimitiveValue<Unit> {
    public static final UnitValue INSTANCE = new UnitValue();
    public static final Codec<UnitValue> CODEC = Unit.CODEC.xmap(unit -> UnitValue.INSTANCE, unit -> Unit.INSTANCE);

    @Override
    public Unit get() {
        return Unit.INSTANCE;
    }

    @Override
    public Codec<UnitValue> codec() {
        return CODEC;
    }
}
