package org.confluence.terra_curio.common.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_curio.TerraCurio;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

public class CuriosEquippedTrigger extends SimpleCriterionTrigger<CuriosEquippedTrigger.TriggerInstance> {
    @Diff
    public static final ResourceLocation ID = TerraCurio.asResource("curios_equipped");
    @Diff
    public static final CuriosEquippedTrigger INSTANCE = new CuriosEquippedTrigger();

    private CuriosEquippedTrigger() {}

    public void trigger(ServerPlayer player, ItemStack stack) {
        trigger(player, instance -> instance.matches(stack));
    }

    @Diff
    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext) {
        return new TriggerInstance(predicate, ItemPredicate.fromJson(json.get("item")));
    }

    @Diff
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final @Nullable ItemPredicate item;

        public TriggerInstance(ContextAwarePredicate player, @Nullable ItemPredicate item) {
            super(ID, player);
            this.item = item;
        }

        public boolean matches(ItemStack itemStack) {
            return item == null || item.matches(itemStack);
        }
    }
}
