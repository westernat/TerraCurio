package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public record MobEffectsValue(Set<Holder<MobEffect>> effects) implements PrimitiveValue<Set<Holder<MobEffect>>> {
    public static final Codec<MobEffectsValue> CODEC = MobEffect.CODEC.listOf().xmap(
            holders -> new MobEffectsValue(new HashSet<>(holders)),
            value -> new ArrayList<>(value.effects)
    );
    public static final CombineRule<Set<Holder<MobEffect>>, MobEffectsValue> EXPANSION = new CombineRule<>() {
        @Override
        public Set<Holder<MobEffect>> combine(Set<Holder<MobEffect>> componentA, Set<Holder<MobEffect>> componentB) {
            Set<Holder<MobEffect>> combined = new HashSet<>(componentA);
            combined.addAll(componentB);
            return combined;
        }

        @Override
        public String name() {
            return "mob_effects_expansion";
        }
    };

    @Override
    public Set<Holder<MobEffect>> get() {
        return effects;
    }

    @Override
    public Codec<MobEffectsValue> codec() {
        return CODEC;
    }
}
