package org.confluence.terra_curio.api.primitive;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record MobEffectsValue(Set<MobEffect> effects) implements PrimitiveValue<Set<MobEffect>> {
    public static final Codec<MobEffectsValue> CODEC = BuiltInRegistries.MOB_EFFECT.byNameCodec().listOf().xmap(
            holders -> new MobEffectsValue(new HashSet<>(holders)),
            value -> new ArrayList<>(value.effects)
    );
    public static final CombineRule<Set<MobEffect>, MobEffectsValue> MERGE = CombineRule.register((a, b) -> {
        Set<MobEffect> combined = new HashSet<>(a);
        combined.addAll(b);
        return combined;
    }, "mob_effects_merge");

    @Override
    public Set<MobEffect> get() {
        return effects;
    }

    @Override
    public Codec<MobEffectsValue> codec() {
        return CODEC;
    }

    @Override
    public List<String> getDescription() {
        List<String> list = new ArrayList<>();
        list.add("[");
        for (MobEffect effect : effects) {
            list.add("    " + ForgeRegistries.MOB_EFFECTS.getKey(effect).toString());
        }
        list.add("]");
        return list;
    }
}
