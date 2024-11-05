package org.confluence.terra_curio.api.event;

import net.minecraft.util.Unit;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.confluence.terra_curio.api.primitive.PrimitiveValue;
import org.confluence.terra_curio.api.primitive.UnitValue;
import org.confluence.terra_curio.api.primitive.ValueType;

import java.util.ArrayList;

public abstract class RegisterAccessoriesComponentUpdateEvent extends Event implements IModBusEvent {
    public static class UnitType extends RegisterAccessoriesComponentUpdateEvent {
        private final ArrayList<ValueType<Unit, UnitValue>> list;

        public UnitType(ArrayList<ValueType<Unit, UnitValue>> list) {
            this.list = list;
        }

        public ArrayList<ValueType<Unit, UnitValue>> getList() {
            return list;
        }
    }

    public static class OtherType extends RegisterAccessoriesComponentUpdateEvent {
        private final ArrayList<ValueType<?, ? extends PrimitiveValue<?>>> list;

        public OtherType(ArrayList<ValueType<?, ? extends PrimitiveValue<?>>> list) {
            this.list = list;
        }

        public ArrayList<ValueType<?, ? extends PrimitiveValue<?>>> getList() {
            return list;
        }
    }
}
