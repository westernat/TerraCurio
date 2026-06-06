package org.confluence.terra_curio.api.event;

import net.minecraft.util.Unit;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.confluence.terra_curio.api.primitive.UnitValue;
import org.confluence.terra_curio.api.primitive.ValueType;

import java.util.function.ToIntFunction;

public class RegisterAccessoriesComponentUnitValueTypeLocalSyncEvent extends Event implements IModBusEvent {
    private final ToIntFunction<ValueType<Unit, UnitValue>> function;

    public RegisterAccessoriesComponentUnitValueTypeLocalSyncEvent(ToIntFunction<ValueType<Unit, UnitValue>> function) {
        this.function = function;
    }

    public int register(ValueType<Unit, UnitValue> type) {
        return function.applyAsInt(type);
    }
}
