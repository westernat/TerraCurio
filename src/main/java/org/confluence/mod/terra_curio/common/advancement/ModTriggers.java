package org.confluence.mod.terra_curio.common.advancement;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.mod.terra_curio.TerraCurio;

import java.util.function.Supplier;

public final class ModTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TYPES = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, TerraCurio.MODID);

    public final static Supplier<CuriosEquippedTrigger> CURIOS_EQUIPPED = TYPES.register("curios_equipped", CuriosEquippedTrigger::new);
}
